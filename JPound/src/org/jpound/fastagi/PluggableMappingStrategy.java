/**
 * File: PluggableMappingStrategy.java
 * Created by: detro
 * Created at: Jul 8, 2006 */
package org.jpound.fastagi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.asteriskjava.fastagi.MappingStrategy;
import org.jpound.exception.NotADirectoryException;
import org.jpound.exception.UnableToGetClassInstanceException;
import org.jpound.util.AbstractObjectFactory;

/**
 * {@link MappingStrategy} che permette la determinazione
 * e l'istanziazione degli {@link AgiScript}, usando una tecnica a
 * "Plug-IN".
 * 
 * Questa classe estende {@link AbstractObjectFactory}, ed utilizza
 * i Descriptor XML per poter aggiungere informazioni circa gli "alias"
 * da associare ad un {@link AgiScript}: questo e' possibile grazie al
 * package @link org.apache.commons.configuration, ed in particolare
 * alla classe @see org.apache.commons.configuration.XMLConfiguration.
 * 
 * Quindi, questa classe unisce il suo ruolo di {@link MappingStrategy},
 * al fatto di esser capace di eseguire il binding tra richiesta AGI
 * e Classe, caricando quest'ultima come un "Plug-IN".
 * 
 * Poiche' questa classe ha il compito di istanziare 
 * solo degli {@link AgiScript}, d'ora in poi si parlera' scambievolmente di 
 * "{@link AgiScript} istanziati" e di "Classi Istanziate": nel primo
 * caso si vuole mettere in evidenza la "specializzazione" di questa Classe
 * (dedicata alla programmazione Fast-AGI), nel secondo il fatto che si tratta
 * di una Factory che carica le definizioni delle Classi in maniera dinamica
 * prelevandoli da una directory.
 * 
 * Per capire come funziona questa particolare {@link MappingStrategy}, e'
 * necessario anche capire come sono strutturate le richieste di tipo
 * "Fast-AGI". Le richieste ad un server Fast-AGI sono nella forma
 * "agi://&lt;fast agi server name&gt;:&lt;fast agi server port&gt;/&lt;agi script name&gt;":
 * {@link PluggableMappingStrategy} usa l'"&lt;agi script name&gt;" come
 * alias per identificare l'{@link AgiScript} invocato dal server Asterisk.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.fastagi
 * @startdate Jul 8, 2006
 * @type PluggableMappingStrategy */
public class PluggableMappingStrategy 
	extends AbstractObjectFactory	 
	implements MappingStrategy {

	private static Logger logger =
		Logger.getLogger (PluggableMappingStrategy.class);
	
	/**
	 * Mappa degli Alias associati agli AgiScript
	 * rilevati. */
	private Map<String, String> aliasesAgiScriptMap = null; 
	
	/**
	 * Configurazione utile agli script */
	private final Configuration configuration;
	
	/**
	 * 
	 * Constructor.
	 *
	 * @param config Configurazione di JPound
	 * @throws NotADirectoryException Nel caso in cui il la directory
	 * 		dove cercare gli AgiScript non sia una directory.
	 * 
	 * @throws IOException */
	public PluggableMappingStrategy(Configuration config)
			throws NotADirectoryException, IOException {
		// Inizializza la Superclasse "AbstractObjectFactory"
		super(config.getString( 
				PluggableMappingStrategyConfigurationXPaths.AGISCRIPT_DIRECTORY ));
		
		this.configuration = config;
		
		// Costruisco la Mappa
		this.rebuildMap();
	}
    
	/**
	 * Ricostruisce la Mappa delle Associazioni tra Alias
	 * e relativa Classe {@link AgiScript}.
	 * 
	 * Per farlo sfrutta le caratteristiche della superclasse
	 * {@link AbstractObjectFactory}: alla particolare Classe {@link AgiScript}
	 * vengono associati tutti gli Alias rilevati nei relativi file
	 * di configurazione.
	 * Questo permette una enorme flessibilita' nella gestione
	 * dei nomi degli script Fast-AGI utilizzabili.
	 * 
	 * E' importante tenere presente che questa classe non gestisce
	 * collisioni sugli Alias: in caso di due {@link AgiScript} con lo stesso
	 * Alias, il risultato e' imprevedibile. */
	private void rebuildMap() {
		String[] classNames;
		Configuration currentConfiguration;
		String currentAlias;
		Iterator aliases;
		
		// Inizializzo o Svuolo la Mappa degli Alias
		if ( this.aliasesAgiScriptMap == null )
			this.aliasesAgiScriptMap = new HashMap<String, String>();
		else
			this.aliasesAgiScriptMap.clear();
		
		// Recupero l'Array dei nomi delle Classi Caricate
		classNames = this.getClassNames();
		
		// Costruisco la Mappa in base agli Alias associati ad ogni Classe
		for ( int i = 0; i < classNames.length; i++ ) {
			currentConfiguration = this.getClassConfiguration(classNames[i]);
			
			aliases = currentConfiguration.getList(
					PluggableMappingStrategyConfigurationXPaths.
						AGISCRIPT_DESCRIPTOR_ALIASES).iterator();
			
			while ( aliases.hasNext() ) {
				currentAlias = (String)aliases.next();
				this.aliasesAgiScriptMap.put(currentAlias, classNames[i]);
				
				if ( logger.isDebugEnabled() ) {
					logger.debug("New AgiScript =>" +
							" scriptname: " +
							currentAlias + 
							" - classname: " +
							classNames[i] );
				}
			}
		}
	}
	
	/**
	 * Ricarica le Classi / {@link AgiScript} rilevati e ricostruisce
	 * la mappa che associa gli Alias alle relative classi.
	 * 
	 * @see org.jpound.util.AbstractObjectFactory#reloadClasses() 
	 * @throws ConfigurationException */
	public void reloadClasses () throws ConfigurationException {
		// Ricarica le Classi dalla Directory a cui e' associata la Classe
		super.reloadClasses ();
		// Ricostruisce la Mappa che associa Alias e Classe
		this.rebuildMap ();
	}
	
    /** 
     * Questo Metodo ha il compito di determinare quale {@link AgiScript}
     * sara' invocato, in base all'{@link AgiRequest} inviata.
     * 
	 * @see MappingStrategy#determineScript(AgiRequest) 
	 * @param request AgiRequest ricevuta 
	 * @return L'AgiScript che deve gestire l'AgiRequest, 
	 * 		"null" se inesistente (non ci sono alias 
	 * 		associati allo script richiesto ) */
	public AgiScript determineScript(AgiRequest request) {
		String className = null;
		Class[] constructorFormalParams = null;
		Object[] constructorInitParams = null;
		AgiScript resultAgiScript = null;
		
		// Cerco l'AgiScript che dovrebbe corrispondere alla richiesta...
		className = (String)this.aliasesAgiScriptMap.get(
				request.getScript() );
		
		// Costruisco i Parametri Formali del Costruttore
		constructorFormalParams = new Class[2];
		constructorFormalParams[0] = Configuration.class;
		constructorFormalParams[1] = Configuration.class;
		
		// Costruisco i Parametri di Inizializzazione del Costruttore
		constructorInitParams = new Object[2];
		constructorInitParams[0] = this.configuration;
		constructorInitParams[1] = this.getClassConfiguration(className);
		
		
		// ... se non lo trova, ritorna "null", altrimenti...
		if ( className == null ) {
			return null;
		} else {
			// ... Istanzio lo Script
			try {
				resultAgiScript = (AgiScript)this.getClassInstance(
						className, 
						constructorFormalParams, 
						constructorInitParams);
				
				if ( logger.isDebugEnabled() )
					logger.debug("New AgiScript Loaded: " + className);
			} catch (UnableToGetClassInstanceException e) {
				logger.fatal(e);
				return null;
			}
			
			return resultAgiScript;
		}
	}
}
