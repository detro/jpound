/**
 * File: ChannelTypeWrapper.java
 * Created by: detro
 * Created at: Aug 4, 2006 */
package org.jpound.fastagi.usersearcher;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.jpound.db.BaseConnectedClass;
import org.jpound.exception.NoMatchException;
import org.jpound.exception.NullDBConnectionException;
import org.jpound.fastagi.usersearcher.exception.UnableToMakeCallException;

/**
 * Questa Classe e' stata pensata per astrarre l'insieme delle operazioni
 * necessarie per poter operare su un particolare Channel Type:
 * <ul>
 * <li>inoltro di una chiamata</li>
 * <li>ricerca di account registrati sul Channel</li>
 * <li>altre operazioni</li>
 * </ul>
 * <br>
 * Per vederne alcuni esempi concreti, vedere {@link SipChannelTypeWrapper} o
 * {@link IaxChannelTypeWrapper}.
 * <br>
 * Inoltre offre una funzionalita' sperimentale: la possibilita' di
 * iniziare una telefonata in maniera Asincrona 
 * ({@link ChannelTypeWrapper#callAsync(AgiChannel, long, int, String)}). 
 * Va pero' meglio testata per capire quali conseguenze comporta 
 * rispetto al Channel fornito.
 * 
 * @author detro
 * @project JPound-AgiUserSearcher
 * @package org.jpound.fastagi.usersearcher
 * @startdate Aug 4, 2006
 * @type ChannelTypeWrapper
 * @class ChannelTypeWrapper */
public abstract class ChannelTypeWrapper extends BaseConnectedClass {	
	private class AsyncCaller extends Thread {
		
		Logger logger =
			Logger.getLogger(AsyncCaller.class);
		
		private final ChannelTypeWrapper channelTypeWrapper;
		private final AgiChannel channel;
		private final String identifier;
		private final int timeout;
		private final String options;
		private final long accountId;
		
		/**
		 * Costruttore.
		 * TODO Documentare
		 * 
		 * @param channelTypeWrapper
		 * @param channel
		 * @param identifier
		 * @param timeout
		 * @param options */
		private AsyncCaller(
				ChannelTypeWrapper channelTypeWrapper,
				AgiChannel channel,
				String identifier,
				int timeout,
				String options) {
			
			this.channelTypeWrapper = channelTypeWrapper;
			this.channel = channel;
			this.identifier = identifier;
			this.timeout = timeout;
			this.options = options;
			this.accountId = 0;
		}
		
		/**
		 * Costruttore.
		 * TODO Documentare
		 * 
		 * @param channelTypeWrapper
		 * @param channel
		 * @param accountId
		 * @param timeout
		 * @param options */
		private AsyncCaller(
				ChannelTypeWrapper channelTypeWrapper,
				AgiChannel channel,
				long accountId,
				int timeout,
				String options) {
			
			this.channelTypeWrapper = channelTypeWrapper;
			this.channel = channel;
			this.identifier = null;
			this.timeout = timeout;
			this.options = options;
			this.accountId = accountId;
		}
		
		public void run() {
			try {
				if ( this.accountId == 0 ) {
					// L'Account Id non e' stato fornito
					this.channelTypeWrapper.call(
							this.channel, 
							this.identifier, 
							this.timeout, 
							this.options);
				} else {
					// L'Account Id E' stato fornito
					this.channelTypeWrapper.call(
							this.channel, 
							this.accountId, 
							this.timeout, 
							this.options);
				}
			} catch (UnableToMakeCallException e) {
				logger.error(e);
			}
		}
	}
	
	/**
	 * Constructor
	 * @param dbConnection Connessione al DB
	 * @throws NullDBConnectionException Se la connessione al DB e' "null" */
	public ChannelTypeWrapper(Connection dbConnection) 
		throws NullDBConnectionException {
		super(dbConnection);
	}
	
	/**
	 * Esegue una Chiamata verso l'identifier fornito.
	 * <br>
	 * L'identifier viene anche detto ChannelName: quando un Account
	 * e' registrato su un Channel, gli viene fornito un "Name" che, nella
	 * maggior parte dei casi, e' il suo numero telefonico "interno".
	 * 
	 * @param channel Channel da cui Parte la telefonata
	 * @param identifier Identificativo verso cui dirigere la telefonata
	 * @param timeout Timeout
	 * @param options Opzioni sulla chiamata 
	 * 		(vedere la documentazione di Asterisk)
	 * 
	 * @return Risultato della Chiamata: "0" se ok.
	 * @throws UnableToMakeCallException */
	public abstract int call(
			AgiChannel channel,
			String identifier,
			int timeout,
			String options) throws UnableToMakeCallException;
	
	/**
	 * Esegue una Chiamata verso l'AccountId fornito.
	 * Questa chiamata verra' effettuata <b>se e solo se</b> l'Account Id
	 * e' registrato su questo Channel Type con un qualche ChannelName.
	 * <br>
	 * Evita quindi di dover prima cercare il ChannelName.
	 * 
	 * E' consigliato preferire questo metodo al metodo
	 * {@link ChannelTypeWrapper#call(AgiChannel, String, int, String)}
	 * 
	 * @param channel Channel da cui Parte la telefonata
	 * @param accountId Id dell'Account da chiamare
	 * @param timeout Timeout
	 * @param options Opzioni sulla chiamata 
	 * 		(vedere la documentazione di Asterisk)
	 * 
	 * @return Risultato della Chiamata: "0" se ok.
	 * @throws UnableToMakeCallException */
	public abstract int call(
			AgiChannel channel,
			long accountId,
			int timeout,
			String options) throws UnableToMakeCallException;
	
	/**
	 * Ritorna la Stringa necessaria per eseguire una Chiamata (Dial) verso
	 * l'identifier fornito.
	 * <br>
	 * In Asterisk, l'inoltro di una chiamata coincide con l'esecuzione
	 * della Application "Dial": per poterla eseguire, e' necessaria una
	 * "stringa" che "punta" all'utente desiderato.
	 * Un esempio di "DialString" e' "SIP/ivan".
	 * <br>
	 * Ovviamente, questa "DialString" cambia da ChannelType a ChannelType
	 * (solitamente, cambia solo il prefisso).
	 *
	 * @param identifier Identifier o ChannelName.
	 * @return la Dial String verso l'identifier fornito secondo la grammatica
	 * 		di questo ChannelType. */
	public abstract String getDialString(String identifier);
	
	
	/**
	 * 
	 * Ritorna la Stringa necessaria per eseguire una Chiamata (Dial) verso
	 * l'accountId fornito.
	 * <br>
	 * In Asterisk, l'inoltro di una chiamata coincide con l'esecuzione
	 * della Application "Dial": per poterla eseguire, e' necessaria una
	 * "stringa" che "punta" all'utente desiderato.
	 * Un esempio di "DialString" e' "SIP/ivan".
	 * <br>
	 * Ovviamente, questa "DialString" cambia da ChannelType a ChannelType
	 * (solitamente, cambia solo il prefisso).
	 * 
	 * @param accountId
	 * @return la Dial String verso l'identifier fornito secondo la grammatica
	 * 		di questo ChannelType. */
	public abstract String getDialString(long accountId);
	
	/**
	 * Esegue una Chiamata Asincrona verso l'identifier fornito.
	 * Questo metodo va testato in profondita', quindi per ora e'
	 * deprecato il suo uso.
	 * Per il resto, e' equivalente al metodo 
	 * {@link ChannelTypeWrapper#call(AgiChannel, String, int, String)}.
	 * 
	 * @deprecated
	 * 
	 * @param channel
	 * @param identifier
	 * @param timeout
	 * @param options */
	public void callAsync (
			AgiChannel channel,
			String identifier,
			int timeout,
			String options) {
		new AsyncCaller(this, channel, identifier, timeout, options).start();
	}
	
	/**
	 * Esegue una Chiamata Asincrona verso l'identifier fornito.
	 * Questo metodo va testato in profondita', quindi per ora e'
	 * deprecato il suo uso.
	 * Per il resto, e' equivalente al metodo 
	 * {@link ChannelTypeWrapper#call(AgiChannel, long, int, String)}.
	 * 
	 * @deprecated
	 * 
	 * @param channel
	 * @param accountId
	 * @param timeout
	 * @param options */
	public void callAsync (
			AgiChannel channel,
			long accountId,
			int timeout,
			String options) {
		new AsyncCaller(this, channel, accountId, timeout, options).start();
	}
	
	/**
	 * Controlla che un AccountId sia registrato su questo Channel con un
	 * qualche ChannelName.
	 * 
	 * @param accountId Id dell'Account di cui controllare la registrazione
	 * @return "true" se registrato, "false" altrimenti */
	public abstract boolean isAccountRegistered(long accountId);
	
	/**
	 * Dato un ChannelName ritorna, se esiste, il corrispondente AccountId.
	 * 
	 * @param channelName ChannelName di cui cercare il corrispondente AccountId
	 * @return l'AccountId cercato, se esiste
	 * @throws NoMatchException Nel caso non esista
	 * @throws SQLException */
	public abstract long getAccountId(String channelName) 
		throws NoMatchException, SQLException;
}