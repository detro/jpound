/**
 * File: AgiUserSearcher.java
 * Created by: detro
 * Created at: Aug 2, 2006 */
package org.jpound.fastagi.usersearcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.jpound.db.DBConstants;
import org.jpound.exception.NoMatchException;
import org.jpound.exception.NotEnoughParametersException;
import org.jpound.exception.NotInitiatedException;
import org.jpound.exception.NullDBConnection;
import org.jpound.fastagi.AbstractConnectedJPoundAgiScript;
import org.jpound.fastagi.AbstractJPoundAgiScript;
import org.jpound.fastagi.usersearcher.exception.UnableToMakeCallException;
import org.jpound.fastagi.usersearcher.exception.UnknownChannelType;

/**
 * {@link AgiUserSearcher} e' un {@link AbstractJPoundAgiScript } che ha la
 * caratteristica di "cercare" un Account.
 * In base ai parametri ricevuti, l'{@link AgiScript} ricerca un Utente 
 * in base ai servizi a cui il suo Account risulta
 * registrato (SIP, IAX, Jingle, ecc...): tutti i client (telefoni fisici,
 * telefoni software) vengono fatti squillare contemporaneamente fino a quando
 * l'Utente non risponde ad uno di essi o va in Timeout (20 secondi).
 * <br>
 * L'Account di un utente puo' essere o meno registrato ad un Channel. Ne
 * esistono alcuni tipi distribuiti direttamente con Asterisk, altri in fase di
 * sviluppo (come Jingle, disponibile nel futuro Asteirk 1.4), 
 * altri compilabili grazie al codice di terze parti.
 * <br>
 * Ogni Channel e' quindi associato ad un tipo {@link ChannelTypeWrapper}: per
 * poter gestire la ricerca di un Utente registrato su un particolare "Type" di
 * Channel, e' necessario estendere questa Classe Astratta.
 * <br>
 * La struttura dell'AgiScript in questione e' realizzata cosi' da poter
 * aumentare la tipologia dei Servizi attraverso cui un utente puo' essere
 * ricercato.
 * <br>
 * Alcuni esempi pratici sono quelli realizzati: {@link SipChannelTypeWrapper},
 * {@link IaxChannelTypeWrapper}.
 * 
 * @author detro
 * @project JPound-AgiUserSearcher
 * @package org.jpound.fastagi.usersearcher
 * @startdate Aug 2, 2006
 * @type AgiUserSearcher */
public class AgiUserSearcher extends AbstractConnectedJPoundAgiScript {

	private static final String PARAM_NAME_USER = "user";

	private static final String PARAM_NAME_CHANNEL_NAME = "channel_name";

	private static final String PARAM_NAME_CHANNEL_TYPE = "channel_type";

	Logger logger = Logger.getLogger(AgiUserSearcher.class);

	/**
	 * Costruttore
	 * 
	 * @param jpoundConfiguration
	 * @param agiScriptConfiguration
	 * @throws NotInitiatedException */
	public AgiUserSearcher(Configuration jpoundConfiguration,
			Configuration agiScriptConfiguration) throws NotInitiatedException {
		super(jpoundConfiguration, agiScriptConfiguration);
	}

	/**
	 * @see AbstractConnectedJPoundAgiScript#service(AgiRequest, AgiChannel) */
	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {
		String username = null;
		String channelName = null;
		String channelType = null;
		long accountId;
		ChannelTypeWrapper currentChannelTypeWrapper;
		ChannelTypeWrapperFactory channelTypeWrapperFactory;
		MultiChannelTypeWrapper multiChannelTypeWrapper;
		Iterator<String> channelTypesIter;
		
		// Recupero la Mappa dei parametri passati all'AgiScript
		Map parameters = request.getParameterMap();

		// Istanzio la Factory di ChannelTypeWrapper
		channelTypeWrapperFactory = new ChannelTypeWrapperFactory(
				this.getAgiScriptConfiguration(), this.getDbConnection());
		// Iterator sulla lista dei ChannelType Rilevati
		channelTypesIter = channelTypeWrapperFactory
				.getAvailableChannelWrapperTypes().iterator();

		try {
			// Inizio recupero AccountID:
			if (parameters.containsKey(PARAM_NAME_USER)) {
				// Username ricevuto direttamente dal Dialplan di Asterisk
				username = ((String[]) parameters.get(PARAM_NAME_USER))[0];

				// Recuper l'ID dell'Account che si vuole Cercare
				accountId = this.getAccountIdByUsername(username);

				if (logger.isDebugEnabled()) {
					logger.debug("USERNAME RECEIVED: " + username);
				}
			} else {
				if (parameters.containsKey(PARAM_NAME_CHANNEL_NAME)
						&& parameters.containsKey(PARAM_NAME_CHANNEL_TYPE)) {

					// ChannelName ricevuto direttamente dal Dialplan di
					// Asterisk
					channelName = ((String[]) parameters
							.get(PARAM_NAME_CHANNEL_NAME))[0];
					// ChannelType ricevuto direttamente dal Dialplan di
					// Asterisk
					channelType = ((String[]) parameters
							.get(PARAM_NAME_CHANNEL_TYPE))[0];

					// Recuper l'ID dell'Account che si vuole Cercare:
					// Il servizio e' fornito dal ChannelTypeWrapper
					// corrispondente, se esiste.
					accountId = channelTypeWrapperFactory.getInstance(
							channelType).getAccountId(channelName);

					if (logger.isDebugEnabled()) {
						logger.debug("CHAN.NAME RECEIVED: " + channelName
								+ " CHAN.TYPE RECEIVED: " + channelType);
					}
				} else {
					// L'AgiScritp richiede dei parametri per
					// il suo corretto funzionamento.
					throw new NotEnoughParametersException();
				}
			}
		} catch (NoMatchException e) {
			logger.error(e);
			return;
		} catch (SQLException e) {
			logger.error(e);
			return;
		} catch (NotEnoughParametersException e) {
			logger.error(e);
			return;
		} catch (UnknownChannelType e) {
			logger.error(e);
			return;
		} catch (ClassNotFoundException e) {
			logger.error(e);
			return;
		}

		if (logger.isDebugEnabled())
			logger.debug("Retrieved Account ID: " + accountId);

		// Istanzio un MultiChannelTypeWrapper,
		// per eseguire chiamate concorrenti
		try {
			multiChannelTypeWrapper = new MultiChannelTypeWrapper(this
					.getDbConnection());
		} catch (NullDBConnection e) {
			logger.error(e);
			return;
		}

		// Inserisco ogni ChannelTypeWrapper nel MultiChannelTypeWrapper
		while (channelTypesIter.hasNext()) {
			try {
				currentChannelTypeWrapper = channelTypeWrapperFactory
						.getInstance(channelTypesIter.next());

				multiChannelTypeWrapper
						.appendChannelTypeWrapper(currentChannelTypeWrapper);
			} catch (UnknownChannelType e) {
				logger.error(e);
				return;
			} catch (ClassNotFoundException e) {
				logger.error(e);
				return;
			}
		}

		// Eseguo la chiamata Multipla Contemporanea
		try {
			multiChannelTypeWrapper.call(channel, accountId, 20, "rT");
		} catch (UnableToMakeCallException e) {
			logger.error(e);
			return;
		}
	}

	/**
	 * Recupera l'AccountId di un Utente in base al suo username.
	 * 
	 * @param username
	 * @return l'AccountId in base all'username fornito
	 * @throws NoMatchException Se l'Username non corrisponde ad alcun Account
	 * @throws SQLException */
	private long getAccountIdByUsername(String username)
			throws NoMatchException, SQLException {

		ResultSet userIdResultSet = null;
		String query = "SELECT " + DBConstants.ACCOUNT_ID_COLUMN + " AS "
				+ DBConstants.ACCOUNT_ID_COLUMN_ALIAS + " FROM "
				+ DBConstants.ACCOUNT_TABLE + " " + " WHERE "
				+ DBConstants.ACCOUNT_USERNAME_COLUMN + " = '" + username + "'";

		userIdResultSet = this.executeSelectQuery(query.toString());
		userIdResultSet.next();
		if (userIdResultSet.getRow() == 0) {
			throw new NoMatchException();
		} else {
			return userIdResultSet.getLong(DBConstants.ACCOUNT_ID_COLUMN_ALIAS);
		}
	}
}
