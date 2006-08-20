/**
 * File: CoreServer.java
 * Created by: detro
 * Created at: Jul 3, 2006 */
package org.jpound;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.asteriskjava.fastagi.AgiServer;
import org.asteriskjava.fastagi.DefaultAgiServer;
import org.asteriskjava.fastagi.MappingStrategy;
import org.jpound.db.ConfigurationsConnectionsPool;
import org.jpound.db.wrapper.BaseAsteriskConfigurationFile;
import org.jpound.exception.NotADirectoryException;
import org.jpound.exception.NotInitiatedException;
import org.jpound.exception.NullDBConnection;
import org.jpound.exception.TableNotFoundException;
import org.jpound.exception.UnableToWrapConfigurationFileException;
import org.jpound.fastagi.DefaultAgiServerThread;
import org.jpound.fastagi.PluggableMappingStrategy;
import org.jpound.wrapper.AsteriskConfigurationFamily;
import org.jpound.wrapper.AsteriskConfigurationFile;

/**
 * Main Class di JPound.
 * Istanziare questa Classe, corrisponde all'istanziare il Server Principale
 * JPound.
 * 
 * Questo fornisce tutti i servizi di JPound: e' un Container di tutte le 
 * classi fondamentali per la gestione ed il mappaggio della configurazione 
 * di Asterisk, istanziando ed inizializando 
 * opportunamente tutti i componenti.
 * 
 * In questa implementazione, il Core Server si occupa di:
 * <ul>
 * <li>
 *  Istanziare le classi Wrapper per la 
 *  configurazione Realtime di Asterisk
 * </li>
 * <li>
 * 	Istanziare il Sotto-Server FastAGI e far partire il relativo Thread,
 * 	che si mette in ascolto sulla porta scelta
 * </li>
 * </ul>
 * 
 * Per la configurazione, si e' deciso di usare il progetto 
 * Apache Jakarta Commons Configuration: esso permette di generalizzare la
 * fase di configurazione, facendo si che tutte le classi che dipendono da tali
 * parametri, debbano solo esser capaci di gestire una classe Configuration.
 * Commons Configuration supporta vari formati per la configurazione: in
 * questo caso si e' scelto di preferire il linguaggio XML, realizzando
 * un formato abbastanza intuitivo ma sufficientemente espressivo e potente.
 * 
 * Le singole Classi avranno quindi solo bisogno di caricare i parametri di
 * configurazione, utilizzando espressioni XPath per la ricerca
 * in un file XML.
 * (<code>http://jakarta.apache.org/commons/configuration/index.html</code>}).
 * 
 * Per quanto riguarda invece il Logging, si e' puntato sul progetto
 * Log4J, poiche' in pratica "standard-de-facto", soprattutto nell'ambito dei
 * progetti Open Source.
 * (<code>http://logging.apache.org/log4j/docs/</code>)
 * 
 * @author detro
 * @project JPound
 * @package org.jpound
 * @startdate Jul 3, 2006
 * @type CoreServer */
public class CoreServer extends Thread {
	
	/** Nome di Default del file di Configurazione di jpound */
	private static final String DEFAULT_CONFIG_FILENAME = 
		"jpound.config.xml";
	
	/** @see org.apache.log4j.Logger */
	private static Logger logger = 
		Logger.getLogger(CoreServer.class);
	
	/** Questa HashMap e' un container di wrapper ai file di configurazione
	 * di Asterisk (@see AsteriskConfigurationFile).
	 * I file sono indicizzati in base al nome del file di 
	 * configurazione fisico: es. "<code>sip.conf</code>" o 
	 * "<code>voicemail.conf</code>". */
	private final HashMap<String, AsteriskConfigurationFile> 
			asteriskConfigurationFiles;
	
	/**
	 * Questa HashMap e' un container di wrapper alle "family" di 
	 * configurazione di Asterisk.
	 * I file sono indicizzati in base al nome della "family": 
	 * es. "<code>extensions</code>" o "<code>sipusers</code>" o 
	 * "<code>iaxusers</code>". */
	private final HashMap<String, AsteriskConfigurationFamily> 
			asteriskConfigurationFamilies;
	
	/** Connessione al DB */
	private Connection dbConnection;
	
	/**
	 * Agi Server Thread.
	 * E' una istanzia di un Thread che "wrappa" un Agi Server, cosi'
	 * da facilitarne la gestione ed inserirlo all'interno di un sistema
	 * multi-threaded. */
	private DefaultAgiServerThread agiServerThread;
	
	/**
	 * {@link MappingStrategy} per l'AgiServer. 
	 * E' un oggetto necessario per consentire al sotto-server FastAGI
	 * di mappare le {@link AgiRequest} (le richieste inoltrate al server Agi)
	 * alla Classe {@link AgiScript} capace di rispondere alla richiesta. 
	 * 
	 * JPound dispone di una speciale {@link MappingStrategy}
	 * denominata {@link PluggableMappingStrategy} */
	private PluggableMappingStrategy agiServerMappingStrategy;
	
	/**
	 * Configuration di JPound.
	 * In formato XML. */
	private Configuration configuration;
	
	/**
	 * Restituisce una istanza di JPound {@link CoreServer}.
	 * In questo caso, viene cercato il file di configurazione
	 * nella Current Work Directory.
	 * 
	 * @return Restituisce una istanza di JPound {@link CoreServer}. */
	public static CoreServer getInstance() {
		return getInstance(CoreServer.DEFAULT_CONFIG_FILENAME);
	}
	
	/**
	 * Restituisce una istanza di JPound {@link CoreServer}.
	 * 
	 * @param configurationFilePath Stringa contenente il path completo
	 * 		al file di configurazione di JPound.
	 * 
	 * @return Restituisce una istanza di JPound {@link CoreServer}. */
	public static CoreServer getInstance(String configurationFilePath) {
		XMLConfiguration config = null;
		try {
			// Inizializzo la configurazione di Log4J a quella di Base
			BasicConfigurator.configure();
			// Carico il file di Configurazione di JPound
			config = new XMLConfiguration( configurationFilePath );
			// Riconfiguro Log4J prendendo la configurazione dal file
			// indicato nel file di configurazio di JPound
			DOMConfigurator.configure( 
					config.getString( 
							CoreServerConfigurationXPaths.
								LOG4J_CONFIG_FILEPATH ) );
		} catch (ConfigurationException e) {
			logger.fatal(e);
			return null;
		} catch (FactoryConfigurationError e) {
			logger.fatal(e);
			return null;
		}
		
		return getIstance(config);
	}
	
	/**
	 * Restituisce una istanza di JPound {@link CoreServer}.
	 * 
	 * @param configFile File di configurazione di JPound.
	 * @return Restituisce una istanza di JPound {@link CoreServer}. */
	public static CoreServer getInstance(File configFile) {
		XMLConfiguration config = null;
		try {
			BasicConfigurator.configure();
			config = new XMLConfiguration( configFile );
			DOMConfigurator.configure( 
					config.getString( 
							CoreServerConfigurationXPaths.
								LOG4J_CONFIG_FILEPATH ) );
		} catch (ConfigurationException e) {
			logger.fatal(e);
			return null;
		} catch (FactoryConfigurationError e) {
			logger.fatal(e);
			return null;
		}
		
		return getIstance(config);
	}
	
	/**
	 * Restituisce una istanza di JPound {@link CoreServer}.
	 * 
	 * @param config Configurazione di JPound
	 * @return Restituisce una istanza di JPound {@link CoreServer}. */
	public static CoreServer getIstance(Configuration config) {
		try {
			return new CoreServer(config);
		} catch (NotInitiatedException e) {
			logger.fatal(e);
			return null;
		}
	}
	
	/**
	 * Private Constructor.
	 * 
	 * Questo costruttore e' stato reso "privato" per coerenza con i metodi
	 * "getInstance": per far si di inglobare nella classe stessa 
	 * l'istanziazione della Classe XMLConfiguration, si e' pensato ad un
	 * Pattern "Factory", con i metodi "<code>getInstance(...)</code>".
	 * 
	 * @param config Configuration file di tipo {@link XMLConfiguration}
	 * @throws NotInitiatedException Nel caso si verifichino problemi durante
	 * 		l'inizializzazione dell'Oggetto. */
	private CoreServer(Configuration config) 
		throws NotInitiatedException {
		super();
		
		this.configuration = config;
		
		// Inizializzazione delle HashMap che contengono le classi di Wrapping
		this.asteriskConfigurationFiles = 
			new HashMap<String, AsteriskConfigurationFile>( 
				this.configuration.getInt( 
						CoreServerConfigurationXPaths.
							AVERAGE_CONFIG_FILE_NUMBER ) );
		this.asteriskConfigurationFamilies = 
			new HashMap<String, AsteriskConfigurationFamily>(
				this.configuration.getInt( 
						CoreServerConfigurationXPaths.
							AVERAGE_CONFIG_FAMILY_NUMBER ) );
		
		// Inizializzazione della Connessione al DB
		this.dbConnection = null;
		try {
			this.dbConnection = ConfigurationsConnectionsPool.getConnection(config);
		} catch (ClassNotFoundException e) {
			logger.fatal(e);
			throw new NotInitiatedException(
				"Error during DB Connection Initialization: " + e);
		} catch (SQLException e) {
			logger.fatal(e);
			throw new NotInitiatedException(
				"Error during DB Connection Initialization: " + e);
		} catch (InstantiationException e) {
			logger.fatal(e);
			throw new NotInitiatedException(
				"Error during DB Connection Initialization: " + e);
		} catch (IllegalAccessException e) {
			logger.fatal(e);
			throw new NotInitiatedException(
				"Error during DB Connection Initialization: " + e);
		} catch (NotInitiatedException e) {
			logger.fatal(e);
			throw new NotInitiatedException(
				"Error during DB Connection Initialization: " + e);
		}
		
		// Carico i Wrapper alla Configurazione Real-Time e Real-Time-Static
		//  di Asterisk: gli AsteriskConfigurationFiles e 
		//  gli AsteriskConfigurationFamilies
		this.loadAsteriskConfigurationFiles();
		this.loadAsteriskConfigurationFamilies();
		
		// Inizializzo l'AgiServer
		this.initAgiServer();
	}
	
	/**
	 * Legge dal file di Configurazione la lista delle Classi
	 * {@link AsteriskConfigurationFile} da Istanziare e le Istanzia, 
	 * inserendole nella HashMap adibita a contenere queste istanze, 
	 * indicizzate in base al nome del file che "descrivono/wrappano".
	 * 
	 * @throws NotInitiatedException Se il file di Configurazione non e'
	 * 		presente. */
	private void loadAsteriskConfigurationFiles() 
		throws NotInitiatedException {
		
		Iterator fileNamesIterator;
		Iterator tableNamesIterator;
		
		// Controlla che la Configurazione sia stata caricata
		if ( this.configuration == null || this.configuration.isEmpty() ) {
			throw new NotInitiatedException(
					"JPound XML Configuration File UNAVAILABLE");
		}
		
		// Svuota, se necessario, la HashMap dei ConfigFile gia' presenti
		if ( !this.asteriskConfigurationFiles.isEmpty() ) {
			this.asteriskConfigurationFiles.clear();
		}
		
		// Istanzio gli Iterator sui nodi del File di Configurazione
		//	che raccolgono i parametri per istanziare 
		//  le classi AsteriskConfigurationFile
		fileNamesIterator = this.configuration.getList( 
				CoreServerConfigurationXPaths.FILE_NAMES ).iterator();
		tableNamesIterator = this.configuration.getList( 
				CoreServerConfigurationXPaths.TABLE_NAMES ).iterator(); 
		
		// Itero sui nodi del file di configurazione
		while ( fileNamesIterator.hasNext() && tableNamesIterator.hasNext() ) {
			String currentFileName = (String)fileNamesIterator.next();
			String currentTableName = (String)tableNamesIterator.next();
			
			try {
				// Istanzio le classi AsteriskConfigurationfile
				this.asteriskConfigurationFiles.put(
						currentFileName, // Key: Nome File 
						new BaseAsteriskConfigurationFile(	
								this.dbConnection,
								currentTableName,
								currentFileName
								) // Value: AsteriskConfigurationfFile Wrapper
						);
				
				if( logger.isDebugEnabled() ) {
					logger.debug("AsteriskConfigurationFile Loaded => "+
							currentFileName + 
							" - " +
							currentTableName );
				}
			} catch (TableNotFoundException e) {
				throw new NotInitiatedException(e);
			} catch (UnableToWrapConfigurationFileException e) {
				throw new NotInitiatedException(e);
			} catch (NullDBConnection e) {
				throw new NotInitiatedException(e);
			}
		}
	}
	
	/**
	 * Legge dal file di Configurazione la lista delle Classi
	 * {@link AsteriskConfigurationFamily} da Istanziare e le Istanzia, 
	 * inserendole nella HashMap adibita a contenere queste istanze, 
	 * indicizzate in base al nome della Family che "descrivono/wrappano".
	 * 
	 * In questa versione, il metodo non e' implementato.
	 * 
	 * @throws NotInitiatedException Se il file di Configurazione non e'
	 * 		presente. */
	private void loadAsteriskConfigurationFamilies() 
		throws NotInitiatedException {
		// TODO Da Implementare
	}
	
	/**
	 * Inizializza il Sotto-Server {@link DefaultAgiServer}.
	 * Questo metodo si occupa di eseguire le necessarie operazioni
	 * per poter caricare una {@link MappingStrategy}, Istanizare un 
	 * {@link DefaultAgiServer} (In questo caso, Wrappato in un 
	 * Thread indipendente: {@link DefaultAgiServerThread}) 
	 * e fornire l'{@link AgiServer} cosi' creato della necessaria 
	 * {@link MappingStrategy}.
	 * 
	 * JPound dispone di una speciale {@link MappingStrategy}, denominata
	 * {@link PluggableMappingStrategy}.
	 * 
	 * @throws NotInitiatedException Se il file di Configurazione non e'
	 * 		presente. */
	private void initAgiServer() 
		throws NotInitiatedException {
		
		// Controlla che la Configurazione sia stata caricata
		if ( this.configuration == null || this.configuration.isEmpty() ) {
			throw new NotInitiatedException(
					"JPound XML Configuration File UNAVAILABLE");
		}
		
		// Istanzio un AgiServerThread
		this.agiServerThread = new DefaultAgiServerThread( this.configuration );
		
		// Istanzio una MappingStrategy: in questo caso, una
		//  PluggableMappingStrategy
		this.agiServerMappingStrategy = null;
		try {
			// Istanzio una PluggableMappingStragegy
			this.agiServerMappingStrategy = new PluggableMappingStrategy( 
					this.configuration );
		} catch (NotADirectoryException e) {
			logger.fatal(e);
		} catch (IOException e) {
			logger.fatal(e);
		}
		
		// Setto la MappingStrategy per il Server
		this.agiServerThread.setMappingStrategy( 
				this.agiServerMappingStrategy );
	}
	
	/** 
	 * Mette in esecuzione il Server.
	 * In questa implementazione, ha solo il compito di mettere in esecuzione
	 * il {@link DefaultAgiServerThread}.
	 * 
	 * @see java.lang.Thread#run() */
	public void run() {
		// Faccio partire il Thread dell'Agi Server
		try {
			this.agiServerThread.startup();
			
			if ( logger.isDebugEnabled() )
				logger.debug("*** Agi Server Started ***");
		} catch (IllegalStateException e) {
			logger.fatal(e);
		} 
	}

	/**
	 * Precedura di "Chiusura" del CoreServer.
	 * Ripulisce tutte le strutture inizializzate e blocca i
	 * sotto-server in esecuzione (in questa implementazione, solo
	 * il server {@link DefaultAgiServerThread}).
	 * 
	 * @see java.lang.Object#finalize() */
	public void finalize() {
		if ( logger.isDebugEnabled() ) {
			logger.info( "JPound Core Server Stopping..." );
		}
		
		// Stoppo l'Agi Server
		this.agiServerThread.shutdown();
		
		// Chiudo la connessione al DB
		try {
			this.dbConnection.close();
		} catch (SQLException e) {
			logger.error(e);
		}
		
		if ( logger.isDebugEnabled() ) {
			logger.info( "JPound Core Server Stopped!" );
		}
	}
	
	/**
	 * Ricarica la {@link MappingStrategy} ({@link PluggableMappingStrategy}) 
	 * attualmente usata dal {@link DefaultAgiServer}
	 * incorporato nel {@link CoreServer} di JPound.
	 * 
	 * @return "true" se il reload e' andato a buon fine, "false" altrimenti */
	public boolean reloadMappingStrategy() {
		logger.info("REQUEST => Mapping Strategy RELOAD");
		
		try {
			this.agiServerMappingStrategy.reloadClasses();
			return true;
		} catch (ConfigurationException e) {
			logger.error(e);
			return false;
		}
	}
	
	/**
	 * Mette in esecuzione il {@link DefaultAgiServerThread} incorporato
	 * nel {@link CoreServer}.
	 * 
	 * @return "true" se lo startup e' andato a buon fine, "false" altimenti. */
	public boolean startupAgiServer() {
		logger.info("REQUEST => Agi Server STARTUP");
		
		if ( !this.agiServerThread.isRunning() ) {
			this.agiServerThread.startup();
			return true;
		}
		return false;
	}
	
	/**
	 * Stoppa il {@link DefaultAgiServerThread} incorporato
	 * nel {@link CoreServer}.
	 * 
	 * @return "true" se lo shutdown e' andato a buon fine, 
	 * 		"false" altimenti. */
	public boolean shutdownAgiServer() {
		logger.info("REQUEST => Agi Server SHUTDOWN");
		
		if ( this.agiServerThread.isRunning() ) {
			this.agiServerThread.shutdown();
			return true;
		}
		return false;
	}
	
	/**
	 * Riavvia il {@link DefaultAgiServerThread} incorporato
	 * nel {@link CoreServer}.
	 * 
	 * @return "true" se il riavvio e' andato a buon fine, "false" altimenti. */
	public boolean restartAgiServer() {
		logger.info("REQUEST => Agi Server RESTART");
		
		if ( this.shutdownAgiServer() ) {
			if ( this.reloadMappingStrategy() ) {
				if ( this.startupAgiServer() ) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Ritorna una {@link List} di {@link String} contenente i nomi
	 * degli {@link AsteriskConfigurationFile} 
	 * istanziati dal {@link CoreServer}.
	 *
	 * @return Ritorna una {@link List} di {@link String} contenente i nomi
	 * 		degli {@link AsteriskConfigurationFile}
	 * 		istanziati dal {@link CoreServer}. */
	public List<String> getAsteriskConfigurationFilesNames () {
		return new ArrayList<String>( this.asteriskConfigurationFiles.keySet() );
	}
	
	/**
	 * Recupera un {@link AsteriskConfigurationFile} in base al nome
	 * del Configuration File.
	 *
	 * @param configurationFilename
	 * @return L'{@link AsteriskConfigurationFile} corrispondente al
	 * 		nome del Configuration File fornito. "null" se non esiste. */
	public AsteriskConfigurationFile getAsteriskConfigurationFile (
			String configurationFilename ) {
		return this.asteriskConfigurationFiles.get( configurationFilename );
	}
	
	/**
	 * Ritorna una {@link List} di {@link String} contenente i nomi
	 * delle {@link AsteriskConfigurationFamily} 
	 * istanziate dal {@link CoreServer}.
	 *
	 * @return Ritorna una {@link List} di {@link String} contenente i nomi
	 * 		delle {@link AsteriskConfigurationFamily} 
	 * 		istanziate dal {@link CoreServer}. */
	public List<String> getAsteriskConfigurationFamiliesNames () {
		return new ArrayList<String>( this.asteriskConfigurationFamilies.keySet() );
	}
	
	/**
	 * Recupera una {@link AsteriskConfigurationFamily} in base al nome
	 * della Configuration Family
	 *
	 * @param configurationFamilyName
	 * @return L'{@link AsteriskConfigurationFamily} corrispondente al
	 * 		nome della Configuration Family fornita. "null" se non esiste. */
	public AsteriskConfigurationFamily getAsteriskConfigurationFamily (
			String configurationFamilyName ) {
		return this.asteriskConfigurationFamilies.get( configurationFamilyName );
	}
	
	/**
	 * Main method del Core Server.
	 * 
	 * Permette di incorporare l'intero JPound in un file Jar ed eseguirlo.
	 * Se vengono passati i	parametri 
	 * "<code>-c &lt;jpound_xml_configuration_file&gt;</code>",
	 * il CoreServer cerca la sua configurazione in una posizione
	 * alternativa a quella di default.
	 * 
	 * Se il file non esiste, cerca il file nella posizione di default.
	 * 
	 * @param args Argomenti della riga di comando. */
	public static void main(String[] args) {
		// Verifico se sono stati passati parametri dalla riga di comando:
		// se si tratta di "-c", allora controllo che il file esista
		if ( 
				args.length == 2 && 
				args[0].equalsIgnoreCase("-c") && 
				new File(args[1]).exists() 
			) {
			// Istanzio ed Eseguo il CoreServer: 
			//   il path NON e' quello di default
			CoreServer.getInstance(args[1]).start();
		} else {
			// Istanzio ed Eseguo il CoreServer:
			//   il path e' quello di default
			CoreServer.getInstance().start();
		}		
	}
}
