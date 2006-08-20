/**
 * File: AbstractObjectFactory.java
 * Created by: detro
 * Created at: Jul 8, 2006 */
package org.jpound.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.jpound.exception.NotADirectoryException;
import org.jpound.exception.UnableToGetClassInstanceException;

/**
 * Questa classe ha il compito di Istanziare Oggetti ({@link Object}).
 * All'atto della sua Creazione, prende come parametro una Directory: in essa
 * cerca classi da istanziare a Runtime.
 * <br>
 * Le Classi che possono essere istanziate, vanno accompagnate da un descrittore
 * in formato XML (i termini Configuration e Descriptor saranno qui usati
 * scambievolmente). Esempio:
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;root&gt;
 * 		&lt;class classname="HelloWorld"&gt;
 * 			&lt;filename&gt;HelloWorld.jar&lt;/filename&gt;
 * 			&lt;description&gt;Hello World Description&lt;/description&gt;
 * 		&lt;/class&gt;
 * 		...
 * &lt;/root&gt;
 * </pre>
 * <br> 
 * Come e' possibile notare dal descrittore, esso punta ad un file che contiene
 * la Classe (o le classi) che descrive: questo puo' sia essere un 
 * "<code>.class</code>" (nel caso di singole classi), 
 * sia un "<code>.jar</code>", nel caso di interi package. E' quindi 
 * necessario utilizzare file con la giusta estensione 
 * (o "<code>.class</code>" o "<code>.jar</code>", appunto).
 * <br>
 * Il Descrittore necessita dei campi sopra elencati, e permette al contempo
 * di aggiungerne di arbitrari. La "root" puo' avere qualsiasi nome, dato che
 * viene ignorata (importante e' che sia unica,  altrimenti non sarebbe un 
 * "Well Formed XML").
 * Le Sottoclassi di questa classe possono accedere alla Configurazione 
 * (cioe' ai Descriptor XML) delle classi rilevate utilizzando i metodi
 * appositi.
 * <br>
 * E' una classe Astratta perche' progettata per essere la base di
 * altre Factory con un maggior grado di specializzazione: questo si evince
 * analizzando il metodo 
 * {@link AbstractObjectFactory#getClassInstance(String, Class[], Object[])}.
 * <br>
 * E' importante notare che questa Classe indicizza le informazioni in base
 * al Nome delle Classi rilevate: poiche' usa delle HashMap, 
 * l'accesso alle informazioni risultera' performante. 
 * <br>
 * Per manipolare il ClassPath a Runtime, questa classe fa uso della classe
 * {@link ClassPathHacker}.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.util
 * @startdate Jul 8, 2006
 * @type AbstractObjectFactory
 * @class AbstractObjectFactory */
public abstract class AbstractObjectFactory {

	private static Logger logger = Logger.getLogger(
			AbstractObjectFactory.class);
	
	/** 
	 * Mappa delle Classi Rilevate.
	 * La Chiave di Indicizzazione e' il nome della Classe, mentre
	 * l'oggetto e' la Classe stessa */ 
	private final Map<String, Class> foundedClasses;
	
	private final Map<String, Configuration> foundedClassConfigurations;
	
	/** Directory dove cercare le Classi */
	private File classesRepositoryDirectory;
	
	/**
	 * Numero medio di Classi previste: serve solo per inizializzare la mappa. */
	private static int averageFoundedClasses = 15;
	
	/**
	 * Estensione dei file Descriptor. */
	private static String classDescriptorExtension = ".desc.xml";
	
	/**
	 * Estensione dei file Jar. */
	private static String jarExtension = ".jar";
	
	/**
	 * Constructor. 
	 * @param classesDirectory Directory dove cercare le Classi
	 * @throws NotADirectoryException
	 * @throws IOException */
	public AbstractObjectFactory( File classesDirectory ) 
		throws NotADirectoryException, IOException {
		
		super();
		
		this.setClassesRepositoryDirectory(classesDirectory);
		this.foundedClasses = new HashMap<String, Class>(
				AbstractObjectFactory.averageFoundedClasses);
		this.foundedClassConfigurations = new HashMap<String, Configuration>(
				AbstractObjectFactory.averageFoundedClasses);
		
		try {
			this.reloadClasses();
		} catch (ConfigurationException e) {
			logger.error(e);
		}
	}
	
	/**
	 * Constructor 
	 * @param classesRepositoryDirectory Directory dove cercare le Classi
	 * @throws NotADirectoryException
	 * @throws IOException */
	public AbstractObjectFactory( String classesDirectory ) 
		throws NotADirectoryException, IOException {
		
		this( new File(classesDirectory) );
	}
	
	/**
	 * Imposta la Directory dove cercare le Classi.
	 * Questa deve essere necessariamente una Directory.
	 * 
	 * @param classesDirectory
	 * @throws NotADirectoryException
	 * @throws IOException */
	private synchronized void setClassesRepositoryDirectory( File classesDirectory ) 
		throws NotADirectoryException, IOException {
		
		// Controllo che la Directory... sia tale
		if ( classesDirectory.isDirectory() ) {
			// Aggiungo la directory al ClassPath (tecnica sperimentale) XXX
			ClassPathHacker.addFile(classesDirectory);
			this.classesRepositoryDirectory = classesDirectory;
		} else {
			throw new NotADirectoryException("filename: " + 
					classesDirectory.getName());
		}
	}
	
	/** 
	 * Ricarica le Classi a partire dalla directory scelta.
	 * Cerca in essa i descrittori e, in base ad essi, carica le Classi ed i Jar
	 * relativi.
	 * E' possibile usare questo metodo anche a Runtime, cosi' da aggiornare
	 * "on-the-fly" la lista delle Classi disponibili.
	 * 
	 * @throws ConfigurationException */
	public synchronized void reloadClasses() 
		throws ConfigurationException {
		
		String currentClassName = null;
		String currentClassFilename = null;
		Class currentClass = null;
		Configuration currentConfiguration;
		
		FilenameFilter filter;
		File[] classDescriptorFiles;
		
		// Svuota la Mappa delle classi trovate, se necessario
		if ( this.foundedClasses.size() != 0 )
			this.foundedClasses.clear();
		
		// Costruisco un filtro per sui Nomi di file in base all'estensione per
		// individuare i Class Descriptor
		filter = new FilenameSuffixFilter(
				this.classesRepositoryDirectory,
				AbstractObjectFactory.classDescriptorExtension);
		
		// Costruisco la lista dei File descrittori
		classDescriptorFiles = this.classesRepositoryDirectory.listFiles(filter);
		
		// Ciclo su tutti i file descrittori e carico tutte le classi ivi rilevate
		// In caso di errori, vengono lanciate delle eccezioni ClassNotFoundException
		for ( int i = 0; i < classDescriptorFiles.length; i++ ) {
			// Carico la configurazione da un Descrittore in XML
			currentConfiguration = new XMLConfiguration(classDescriptorFiles[i]);
			
			// Leggo le varie informazioni
			// 		Nome della Classe
			currentClassName = currentConfiguration.getString( 
					AbstractObjectFactoryDescriptorXPaths.className );
			currentClassFilename = currentConfiguration.getString( 
					AbstractObjectFactoryDescriptorXPaths.classFilename );
			
			try {
				// Se si tratta di un "JAR" file, lo aggiunge al ClassPath
				if ( currentClassFilename.endsWith(AbstractObjectFactory.jarExtension) ) {
					ClassPathHacker.addFile(
							this.classesRepositoryDirectory.getPath() + 
							"/" + 
							currentClassFilename);
				}
				
				// Carica la Classe
				currentClass  = Class.forName( currentClassName );
				
				// Inserisce le varie informazioni nelle HashMap
				// 		indicizzate in base al nome della Classe
				this.foundedClasses.put(currentClassName, currentClass);
				this.foundedClassConfigurations.put(
						currentClassName, 
						currentConfiguration);
				
				// Se tutto e' andato a buon fine
				if ( logger.isDebugEnabled() )
					logger.debug("Class Founded: " + currentClassName);
			} catch (IOException e) {
				logger.error(e);
				throw new ConfigurationException ( e );
			} catch (ClassNotFoundException e) {
				logger.error(e);
				throw new ConfigurationException ( e );
			}
		}
	}
	
	/**
	 * Ricarica le Classi a partire da una Nuova Directory.
	 * Questo metodo permette di cambiare la "sorgente" delle Classi
	 * a Run-Time.
	 *  
	 * @see AbstractObjectFactory#reloadClasses()
	 * @throws ClassNotFoundException */
	public synchronized void reloadClasses( File classesDirectory ) 
		throws ClassNotFoundException, ConfigurationException, 
		NotADirectoryException, IOException {
		
		this.setClassesRepositoryDirectory(classesDirectory);
		this.reloadClasses();
	}
	
	/**
	 * @see AbstractObjectFactory#reloadClasses(File)
	 * @throws ConfigurationException */
	public synchronized void reloadClasses( String pluginDirectory ) 
		throws ConfigurationException, 
		NotADirectoryException, IOException {
		
		this.setClassesRepositoryDirectory(new File(pluginDirectory));
		this.reloadClasses();
	}
	
	/**
	 * @return Array di Stringhe con i nomi delle Classi trovate */
	public synchronized String[] getClassNames () {
		return this.foundedClasses.keySet().toArray(new String[0]);
	}
	
	/**
	 * @return Mappa delle Classi indicizzate in base al Nome delle Classe */
	public synchronized Map getClassMap () {
		return this.foundedClasses;
	}
	
	/**
	 * @return Array di Configuration (Descriptor) delle Classi trovate */
	public synchronized Configuration[] getClassConfigurations () {
		return this.foundedClassConfigurations.values().toArray(
				new Configuration[0] );
	}
	
	/**
	 * @return Mappa delle Configurazioni (Descriptor) delle Classi indicizzate 
	 * 		in base al Nome della Classe */
	public synchronized Map getClassConfigurationsMap () {
		return this.foundedClassConfigurations;
	}
	
	/**
	 * @param className
	 * @return Configurazione (Descriptor) della Classe */
	public synchronized Configuration getClassConfiguration (String className) { 
		return this.foundedClassConfigurations.get(className);
	}

	/** 
	 * Istanzia una Classe.
	 * E' il metodo piu' importante di questa AbstractFactory, poiche' gestisce
	 * l'istanziazione per intero, attendendo solo una "descrizione" della firma
	 * del Costruttore della classe desiderata.
	 * 
	 * Questo metodo e' protetto perche' pensato per essere usato da Factory
	 * maggiormente specializzate, e non direttamente.
	 * 
	 * @param className Nome della Classe da Istanziare
	 * @param constructorFormalParams 
	 * 		Array di Classi che descrivono la Firma del Costruttore
	 * 
	 * @param constructorInitParams 
	 * 		Array di Oggetti dei valori di Input del Costruttore
	 * 
	 * @return Oggetto Istanziato, su cui va eseguito un "cast".	 * 
	 * @throws UnableToGetClassInstanceException In caso di problemai
	 * 		durante l'istanziazione. */
	protected Object getClassInstance(
			String className, 
			Class[] constructorFormalParams, 
			Object[] constructorInitParams) 
		throws UnableToGetClassInstanceException {
		
		// Recupero la Classe
		Class currClass = (Class)this.foundedClasses.get(className);
		Object result = null;
		
		// Tento di Istanziare la Classe, usando il costruttore desiderato
		try {
			result = currClass
				.getConstructor(constructorFormalParams)
					.newInstance(constructorInitParams);
			
			if ( logger.isDebugEnabled() )
				logger.debug("New Class Instance: " + className);
		} catch (IllegalArgumentException e) {
			logger.error(e); throw new UnableToGetClassInstanceException(e);
		} catch (SecurityException e) {
			logger.error(e); throw new UnableToGetClassInstanceException(e);
		} catch (InstantiationException e) {
			logger.error(e); throw new UnableToGetClassInstanceException(e);
		} catch (IllegalAccessException e) {
			logger.error(e); throw new UnableToGetClassInstanceException(e);
		} catch (InvocationTargetException e) {
			logger.error(e); throw new UnableToGetClassInstanceException(e);
		} catch (NoSuchMethodException e) {
			logger.error(e); throw new UnableToGetClassInstanceException(e);
		}
		 
		return result;
	}
}