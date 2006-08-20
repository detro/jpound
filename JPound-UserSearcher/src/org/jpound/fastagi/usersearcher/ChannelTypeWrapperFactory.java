/**
 * File: ChannelTypeWrapperFactory.java
 * Created by: detro
 * Created at: Aug 4, 2006 */
package org.jpound.fastagi.usersearcher;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.jpound.fastagi.usersearcher.exception.UnknownChannelType;


/**
 * Questa Factory analizza il file di Configurazione/Descrittore
 * dell'AgiScript {@link AgiUserSearcher} e permette di istanziare
 * i relativi ChannelTypeWrapper.
 * 
 * Facciamo un esempio, fornendo un esempio di file XML di configurazione
 * fornito alla Factory:
 * <pre>
 * ...
 * &lt;channels&gt;
 *		&lt;channel type="sip" 
 *			classname="org.jpound.fastagi.usersearcher.SipChannelTypeWrapper" /&gt;
 *		&lt;channel type="iax" 
 *			classname="org.jpound.fastagi.usersearcher.IaxChannelTypeWrapper" /&gt;
 * &lt;/channels&gt;
 * ...
 * </pre>
 * In base ad essa, la Factory rileva che sono "disponibili" due tipi di
 * ChannelType, uno per Sip ed uno per Iax, permettendo di istanziarli.
 * 
 * @author detro
 * @project JPound-AgiUserSearcher
 * @package org.jpound.fastagi.usersearcher
 * @startdate Aug 4, 2006
 * @type ChannelTypeWrapperFactory
 * @class ChannelTypeWrapperFactory */
public class ChannelTypeWrapperFactory {
	
	private static Logger logger =
		Logger.getLogger(ChannelTypeWrapperFactory.class);
	
	private final Configuration agiScriptConfiguration;
	
	private final Connection dbConnection;
	
	private final Map<String, String> channelsInfo;
	
	/**
	 * Construccor
	 * 
	 * @param agiScriptConfiguration
	 * @param dbConnection */
	public ChannelTypeWrapperFactory(
			Configuration agiScriptConfiguration,
			Connection dbConnection) {
		this.agiScriptConfiguration = agiScriptConfiguration;
		this.dbConnection = dbConnection;
		
		// Carico i ChanellType dal file di Configurazione dell'AgiScript
		Iterator channelTypesIter =
			this.agiScriptConfiguration.getList(
					ChannelTypeWrapperFactoryConfigurationXPaths.
						CHANNEL_TYPES).iterator();
		Iterator channelClassnamesIter =
			this.agiScriptConfiguration.getList(
					ChannelTypeWrapperFactoryConfigurationXPaths.
						CHANNEL_CLASSNAMES).iterator();
		
		this.channelsInfo = new HashMap<String, String>();
		
		// Riempio la HashMap [tipo,nome-classe]
		while( channelTypesIter.hasNext() && channelClassnamesIter.hasNext() ) {
			this.channelsInfo.put(
					(String)channelTypesIter.next(),
					(String)channelClassnamesIter.next() );
		}
	}
	
	/**
	 * Fornisce una istanzia di ChannelType, in base al Type richiesto,
	 * se corretto e disponibile.
	 * 
	 * @param channelType
	 * @return una istanza di ChannelType
	 * @throws UnknownChannelType se il ChannelType non e' corretto
	 * @throws ClassNotFoundException */
	public ChannelTypeWrapper getInstance(String channelType) 
		throws UnknownChannelType, ClassNotFoundException {
		
		Class[] constructorFormalParams = new Class[1];
		Object[] constructorInitParams = new Object[1];
		Class classChannelTypeWrapper;
		ChannelTypeWrapper resultInstance = null;
		
		if ( logger.isDebugEnabled() )
			logger.debug("Required Channel Type: "+channelType);
		
		// Creazione parametri per il Costruttore
		constructorFormalParams[0] = Connection.class; // Firma
		constructorInitParams[0] = this.dbConnection;  // Parametri
		
		// Controllo se il ChannelType esiste
		if ( !this.channelsInfo.containsKey(channelType) )
			throw new UnknownChannelType("Channel Type: "+channelType);
		
		try {
			// Istanziazione della Classe
			classChannelTypeWrapper = 
				Class.forName( this.channelsInfo.get(channelType) );
			
			// Istanziazione del nuovo Oggetto ChannelTypeWrapper
			resultInstance = (ChannelTypeWrapper)classChannelTypeWrapper.
				getConstructor(constructorFormalParams).
					newInstance(constructorInitParams);
		} catch (IllegalArgumentException e) {
			logger.error(e);
		} catch (SecurityException e) {
			logger.error(e);
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		} catch (NoSuchMethodException e) {
			logger.error(e);
		}
		
		return resultInstance;
	}
	
	/**
	 * Fornisce la lista dei ChannelType rilevati in base al file di
	 * configurazione, cosi' da poter richiedere il ChannelType desiderato
	 * @return Un Set contentente i nomi dei ChannelType disponibili. */
	public Set<String> getAvailableChannelWrapperTypes () {
		return this.channelsInfo.keySet();
	}
}
