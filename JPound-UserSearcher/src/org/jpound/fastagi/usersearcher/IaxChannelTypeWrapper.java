/**
 * File: IaxChannelTypeWrapper.java
 * Created by: detro
 * Created at: Aug 4, 2006 */
package org.jpound.fastagi.usersearcher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.jpound.db.DBConstants;
import org.jpound.exception.NoMatchException;
import org.jpound.exception.NullDBConnectionException;
import org.jpound.fastagi.usersearcher.exception.UnableToMakeCallException;

/**
 * Classe ChannelTypeWrapper per il Channel Type "IAX".
 * Vedere {@link ChannelTypeWrapper}.
 * 
 * @author detro
 * @project JPound-AgiUserSearcher
 * @package org.jpound.fastagi.usersearcher
 * @startdate Aug 4, 2006
 * @type IaxChannelTypeWrapper
 * @class IaxChannelTypeWrapper */
public class IaxChannelTypeWrapper extends ChannelTypeWrapper {
	
	private static Logger logger =
		Logger.getLogger(IaxChannelTypeWrapper.class);
	
	public static final String CHANNEL_TYPE_FOR_DIAL_IAX = "IAX2";
	
	/**
	 * Constructor
	 * @param dbConnection
	 * @throws NullDBConnectionException */
	public IaxChannelTypeWrapper(Connection dbConnection) 
		throws NullDBConnectionException {
		super(dbConnection);
	}
	
	/**
	 * @see ChannelTypeWrapper#call(AgiChannel, String, int, String) */
	public int call(
			AgiChannel channel,
			String identifier,
			int timeout,
			String options) throws UnableToMakeCallException {
		String execString = null;
		int result;
		
		if ( logger.isDebugEnabled() )
			logger.debug("Iax2 Call Initiating... -> "+identifier);
		
		try {
			// Preparo la Stringa da Eseguire
			execString = 
				this.getDialString(identifier) + '|' + // ex. IAX2/001
				timeout + 						// ex. 20
				( (options.equals("")) ? "" : '|' + options );
			
			// Esecuzione della applicazione Dial
			result = channel.exec("Dial", execString); // ex. rT
		} catch (AgiException e) {
			throw new UnableToMakeCallException("Dial => "+execString);
		}
		
		if ( logger.isDebugEnabled() )
			logger.debug("Iax2 Call Ended -> "+identifier);
		
		return result;
	}
	
	/**
	 * @see ChannelTypeWrapper#call(AgiChannel, long, int, String) */
	public int call(
			AgiChannel channel,
			long accountId,
			int timeout,
			String options) throws UnableToMakeCallException {
		
		// Controlla se questo Account Id risulta registrato su questo Channel
		if ( this.isAccountRegistered(accountId) ) {
			// Recuper dell'Identificativo associato a questo Account 
			// su questo Channel
			String identifier = this.getAccountChannelRegistration(accountId);
			
			// Chiamata
			return this.call(channel, identifier, timeout, options);
		} else {
			throw new UnableToMakeCallException(
					"Not Registered Account Id: "+accountId);
		}
	}
	
	/**
	 * 
	 * @see ChannelTypeWrapper#getDialString(String) */
	public String getDialString(String identifier) {
		// ex. IAX/001
		return 
			IaxChannelTypeWrapper.CHANNEL_TYPE_FOR_DIAL_IAX + '/' + 
			identifier;
	}
	
	/**
	 * @see ChannelTypeWrapper#getDialString(long) */
	public String getDialString(long accountId) {
		return this.getDialString(
				this.getAccountChannelRegistration(accountId)
				);
	}
	
	/**
	 * @see ChannelTypeWrapper#isAccountRegistered(long) */
	public boolean isAccountRegistered(long accountId) {
		if ( this.getAccountChannelRegistration(accountId) == null )
			return false;
		else
			return true;
	}
	
	/**
	 * Questo metodo e' un "utility" che viene utilizzato per poter
	 * recuperare le informazioni sull'eventuale registrazione di un
	 * Account su di un Channel.
	 * 
	 * @param accountId
	 * @return Il campo "name" Sip dell'Account richiesto se
	 * 		esiste, "null" altrimenti. */
	private String getAccountChannelRegistration(long accountId) {
		String query =
			"SELECT "+
				DBConstants.IAX_NAME_COLUMN + " AS "+DBConstants.IAX_NAME_COLUMN_ALIAS+
			" FROM "+DBConstants.ACCOUNT_TABLE+
			" JOIN "+DBConstants.IAX_TABLE+ 
				" ON "+DBConstants.IAX_ID_COLUMN+" = "+DBConstants.ACCOUNT_FK_IAX_ID+
			" WHERE "+DBConstants.ACCOUNT_ID_COLUMN+" = "+accountId;
		
		try {
			ResultSet accountRegistration =	this.executeSelectQuery(query);
			
			accountRegistration.next();
			// Recuper dell'Identificativo associato a questo Account 
			// su questo Channel
			return accountRegistration.
				getString(DBConstants.IAX_NAME_COLUMN_ALIAS);
		} catch (SQLException e) {
			logger.error(e);
			return null;
		}		
	}
	
	/**
	 * @see ChannelTypeWrapper#getAccountId(String) */
	public long getAccountId(String channelName)
			throws NoMatchException, SQLException {

		ResultSet accountIdResultSet = null;
		// Preparazione della Query
		StringBuffer query = new StringBuffer("SELECT "
				+ DBConstants.ACCOUNT_ID_COLUMN + " AS "
				+ DBConstants.ACCOUNT_ID_COLUMN_ALIAS + " FROM "
				+ DBConstants.ACCOUNT_TABLE + " "+
				" JOIN " + DBConstants.IAX_TABLE + " ON "
				+ DBConstants.IAX_ID_COLUMN + " = "
				+ DBConstants.ACCOUNT_FK_IAX_ID + " WHERE "
				+ DBConstants.IAX_NAME_COLUMN + " = '" + channelName + "'");

		// Recupero Account ID
		accountIdResultSet = this.executeSelectQuery(query.toString());
		accountIdResultSet.next();
		if (accountIdResultSet.getRow() == 0) {
			throw new NoMatchException();
		} else {
			return accountIdResultSet
					.getLong(DBConstants.ACCOUNT_ID_COLUMN_ALIAS);
		}
	}
}
