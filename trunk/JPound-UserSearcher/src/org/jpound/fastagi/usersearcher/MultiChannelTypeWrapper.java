/**
 * File: MultiChannelTypeWrapper.java
 * Created by: detro
 * Created at: Aug 6, 2006 */
package org.jpound.fastagi.usersearcher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.jpound.exception.NoMatchException;
import org.jpound.exception.NullDBConnectionException;
import org.jpound.fastagi.usersearcher.exception.UnableToMakeCallException;

/**
 * Questo e' uno speciale tipo di {@link ChannelTypeWrapper}, utilizzato solo
 * all'interno di {@link AgiUserSearcher}: serve a chiamare un Utente su piu'
 * ChannelType contemporaneamente.
 * <br>
 * Si istanziano i vari {@link ChannelTypeWrapper} su cui si vuole cercare un
 * Utente e si aggiungono in questa classe 
 * ({@link MultiChannelTypeWrapper#appendChannelTypeWrapper(ChannelTypeWrapper)}): 
 * l'invocazione del metodo
 * {@link MultiChannelTypeWrapper#call(AgiChannel, long, int, String)}
 * coincide con l'inolto di una chiamata simultanea su tutti i Channel
 * a cui l'AccountId fornito risulta registrato (e solo su essi).
 * <br>
 * Estende la classe {@link ChannelTypeWrapper}, ma al solo copo di fornire una
 * interfaccia coerente con i normali {@link ChannelTypeWrapper}: alcuni metodi
 * non sono pero' realmente implementabili (non esiste
 * una corrispondenza "uno a uno" tra Classe e Registrazione su un Channel)
 *  e sono indicati come deprecati.
 * 
 * @author detro
 * @project JPound-AgiUserSearcher
 * @package org.jpound.fastagi.usersearcher
 * @startdate Aug 6, 2006
 * @type MultiChannelTypeWrapper */
public class MultiChannelTypeWrapper extends ChannelTypeWrapper {

	Logger logger =
		Logger.getLogger(MultiChannelTypeWrapper.class);
	
	/**
	 * Lista dei ChannelTypeWrapper su cui chiamare un Utente */
	List<ChannelTypeWrapper> channelTypeWrappersPool = 
		new ArrayList<ChannelTypeWrapper>();
	
	/**
	 * Constructor
	 * @param dbConnection
	 * @throws NullDBConnectionException */
	public MultiChannelTypeWrapper(Connection dbConnection) 
		throws NullDBConnectionException {
		super(dbConnection);
	}
	
	/**
	 * Aggiunge un {@link ChannelTypeWrapper}.
	 * Le chiamate su questo {@link MultiChannelTypeWrapper} saranno in realta'
	 * eseguite su tutti i {@link ChannelTypeWrapper} che esso contiene.
	 * 
	 * @param channelTypeWrapper */
	public void appendChannelTypeWrapper(
			ChannelTypeWrapper channelTypeWrapper) {
		this.channelTypeWrappersPool.add(channelTypeWrapper);
	}

	/**
	 * @see ChannelTypeWrapper#call(AgiChannel, String, int, String) 
	 * 
	 * @deprecated
	 * 
	 * @return "-1"
	 * 
	 * Metodo FITTIZIO: non fa nulla.
	 * Questa Classe non e' associata ad alcun Channel Type, quindi
	 * non e' possibile associare l'identifier fornito ad alcun Channel. */
	public int call(
			AgiChannel channel,
			String identifier,
			int timeout,
			String options) throws UnableToMakeCallException {
		
		return -1;
	}

	/**
	 * @see ChannelTypeWrapper#call(AgiChannel, long, int, String) 
	 * 
	 * La Chiamata su un ChannelType viene effettuata SOLO se
	 * l'Account risulta registrato sullo stesso. */
	public int call(AgiChannel channel, long accountId, int timeout,
			String options) throws UnableToMakeCallException {
		String execString = null;
		int result;
		
		if ( logger.isDebugEnabled() )
			logger.debug("Multi Call Initiating... ");
		
		try {
			// Preparo la Stringa da Eseguire
			execString = 
				this.getDialString(accountId) + '|' + // ex. SIP/001&IAX2/002
				timeout + 						// ex. 20
				( (options.equals("")) ? "" : '|' + options );
			
			// Esecuzione della applicazione Dial
			result = channel.exec("Dial", execString); // ex. rT
		} catch (AgiException e) {
			throw new UnableToMakeCallException("Dial => "+execString);
		}
		
		if ( logger.isDebugEnabled() )
			logger.debug("Multi Call Ended");

		return result;
	}

	/**
	 * Metodo FITTIZIO: non ritorna nulla.
	 * Questa Classe non e' associata ad alcun Channel Type, quindi
	 * non e' possibile associare l'identifier fornito ad alcun Channel. 
	 * Lo stesso vale per 
	 * {@link MultiChannelTypeWrapper#callAsync(AgiChannel, String, int, String)}
	 * 
	 * @see ChannelTypeWrapper#getDialString(String) 
	 * @deprecated
	 * 
	 * @return "null" */
	public String getDialString(String identifier) {
		return null;
	}

	/**
	 * @see ChannelTypeWrapper#getDialString(long) 
	 * 
	 * Viene previamente verificato per ogni ChannelType inserito se
	 * l'Account vi e' registrato. */
	public String getDialString(long accountId) {
		StringBuffer dialString = new StringBuffer();
		Iterator<ChannelTypeWrapper> channelTypeWrappers = 
			this.channelTypeWrappersPool.iterator();
		ChannelTypeWrapper currentChannelTypeWrapper;
		
		// Costruzione della Dial String
		while ( channelTypeWrappers.hasNext() ) {
			currentChannelTypeWrapper = channelTypeWrappers.next();
			
			// Controlla che l'Account sia registrato su questo ChannelType
			if ( currentChannelTypeWrapper.isAccountRegistered(accountId) ) {
				if ( dialString.length() == 0 ) {
					// E' il primo
					dialString.append(
							currentChannelTypeWrapper.getDialString(accountId)
							);
				} else {
					// NON e' il primo
					dialString.append('&' +
							currentChannelTypeWrapper.getDialString(accountId)
							);
				}
			}
		}
		
		return dialString.toString();
	}

	/**
	 * @see ChannelTypeWrapper#isAccountRegistered(long) 
	 * 
	 * Controlla che l'AccountId sia Registrato su TUTTI i ChannelTypeWrapper
	 * che sono stati "aggiunti" a questo MultiChannelTypeWrapper. */
	public boolean isAccountRegistered(long accountId) {
		Iterator<ChannelTypeWrapper> channelTypeWrappers = 
			this.channelTypeWrappersPool.iterator();
		
		while ( channelTypeWrappers.hasNext() ) {
			if ( !channelTypeWrappers.next().isAccountRegistered(accountId) )
				return false;
		}
		return true;
	}

	/**
	 * Metodo FITTIZIO: non ritorna nulla.
	 * Questa Classe non e' associata ad alcun Channel Type, quindi
	 * non e' possibile associare l'identifier fornito ad alcun Channel. 
	 * Lo stesso vale per 
	 * {@link MultiChannelTypeWrapper#callAsync(AgiChannel, String, int, String)}
	 * 
	 * @see ChannelTypeWrapper#getAccountId(String)
	 * @deprecated
	 * 
	 * @return "-1" */
	public long getAccountId(String channelName)
			throws NoMatchException, SQLException {

		return -1;
	}
}
