/**
 * File: DefaultAgiServerThread.java
 * Created by: detro
 * Created at: Jul 7, 2006 */
package org.jpound.fastagi;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiServer;
import org.asteriskjava.fastagi.DefaultAgiServer;
import org.asteriskjava.fastagi.MappingStrategy;

/**
 * Questo Thread serve da Wrapper ad un normale {@link DefaultAgiServer}: cio'
 * permette di eseguire il {@link DefaultAgiServer} in un {@link Thread} 
 * indipendente ma controllabile tramite l'interfaccia di questa classe.
 * 
 * Con questa classe e' possibile quindi istanziare un {@link AgiServer} 
 * in maniera non bloccante per il resto del {@link Thread} principale.
 * 
 * Per maggiori informazioni sulla tecnologia Fast-AGI riferirsi alla
 * documentazione online del progetto Asterisk-Java e, piu' in generale,
 * alla documentazione di Asterisk (ufficiale e non).
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.fastagi
 * @startdate Jul 7, 2006
 * @type DefaultAgiServerThread
 * @class DefaultAgiServerThread */
public class DefaultAgiServerThread extends Thread {
	
	private static Logger logger = 
		Logger.getLogger( DefaultAgiServerThread.class );
	
	/** 
	 * {@link DefaultAgiServer} wrappato da questa classe */
	private final DefaultAgiServer agiServer;
	
	
	/**
	 * Constructor */
	public DefaultAgiServerThread( Configuration config ) {
		super();
		
		// Istanzio un DefaultAgiServer
		this.agiServer = new DefaultAgiServer();
		
		// Setto la configurazione dell'Agi Server usando i metodi di Wrapping
		this.setPort( config.getInt( DefaultAgiServerThreadConfigurationXPaths.Port ) );
		this.setPoolSize( config.getInt( DefaultAgiServerThreadConfigurationXPaths.ConnectionPoolSize ) );
		this.setPoolMaxSize( config.getInt( DefaultAgiServerThreadConfigurationXPaths.ConnectionPoolMaxSize ) );
	}

	/**
	 * @see DefaultAgiServer#setPort(int)
	 * @param port */
	public void setPort (int port) {
		this.agiServer.setPort( port );
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("DefaultAgiServer - listen port: " + port);
		}
	}
	
	/**
	 * @see DefaultAgiServer#setPoolSize(int)
	 * @param poolSize	 */
	public void setPoolSize (int poolSize) {
		this.agiServer.setPoolSize( poolSize );
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("DefaultAgiServer - connection pool size: " + poolSize);
		}
	}
	
	/**
	 * @see DefaultAgiServer#setMaximumPoolSize(int)
	 * @param poolMaxSize */
	public void setPoolMaxSize (int poolMaxSize) {
		this.agiServer.setMaximumPoolSize( poolMaxSize );
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("DefaultAgiServer - max connection pool size: " + 
					poolMaxSize);
		}
	}
	
	/**
	 * @see DefaultAgiServer#setMappingStrategy(MappingStrategy)
	 * @param mappingStrategy */
	public void setMappingStrategy ( MappingStrategy mappingStrategy ) {
		this.agiServer.setMappingStrategy( mappingStrategy );
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("DefaultAgiServer - new MappingStrategy registered");
		}
	}
	
	/**
	 * @see java.lang.Thread#run() */
	public void run() {
		try {
			this.agiServer.startup();
		} catch (IllegalStateException e) {
			logger.fatal(e);
		} catch (IOException e) {
			logger.fatal(e);
		}
	}
	
	/**
	 * Metodo deprecato: preferire {@link DefaultAgiServerThread#startup()}
	 * @deprecated
	 * @see java.lang.Thread#start() */
	public void start() {
		super.start();
	}
	
	/**
	 * Alias del metodo Thread#start().
	 * E' stato creato per coerenza con l'interfaccia di {@link AgiServer}. 
	 * E' consigliato in favore del 
	 * metodo {@link DefaultAgiServerThread#start()} 
	 * per questioni di Logging. */
	public synchronized void startup() { 
		if ( logger.isDebugEnabled() ) {
			logger.debug("DefaultAgiServer - Starting...");
		}
		
		// Starting Agi Server
		super.start();
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("DefaultAgiServer - Started");
		}
	}
	
	/**
	 * Blocca il {@link DefaultAgiServer} wrappat, provocando 
	 * quindi anche la fine di questo Thread. */
	public synchronized void shutdown() {
		if ( logger.isDebugEnabled() ) {
			logger.debug("DefaultAgiServer - Shutingdown...");
		}
		// Stopping Agi Server
		this.agiServer.shutdown();
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("DefaultAgiServer - Shuted down");
		}
	}
	
	/**
	 * @return "true" se in esecuzione, "false" altrimenti.	 */
	public synchronized boolean isRunning() { return this.isAlive(); }
	
	/**
	 * Se il Thread corrente e' ancora attivo, vuol dire che lo e' il
	 * {@link DefaultAgiServer} wrappato.
	 * E' necessario quindi bloccarlo prima di finalizzare questa classe. 
	 * @see java.lang.Object#finalize() */
	public void finalize() {
		if ( this.isRunning() )
			this.shutdown();
	}
}
