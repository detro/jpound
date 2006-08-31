/**
 * File: BaseConnectedClass.java
 * Created by: detro
 * Created at: Jun 29, 2006 */

package org.jpound.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;
import org.jpound.exception.NullDBConnectionException;

/**
 * Questa Classe e' la Base da cui estendere per creare classi
 * che necessitino del DB per normali query, 
 * tipo SELECT, INSERT, UPDATE e DELETE. 
 * Poiche' sono metodi che verranno usati spesso, ho pensato di inserirli in 
 * una classe comune da cui ereditare.
 * 
 * E' integrata con Log4J: utilizzarla permette di tener facilmente traccia
 * delle query eseguite, per semplificare e velocizzare il debug.
 * 
 * La classe e' anche utile per essere wrappata ed usata come fornitore 
 * di Servizi.
 * 
 * @author detro
 * @project JPound
 * @package 
 * @startdate Jun 29, 2006
 * @type BaseConnectedClass 
 * @class BaseConnectedClass */
public class BaseConnectedClass {

	/**
	 * Logger basato su Log4j. */
	private static Logger logger = 
		Logger.getLogger(BaseConnectedClass.class);
	
	/**
	 * Handler alla connessione verso il DB che contiene le tabelle
	 * per Asterisk Realtime-Static */
	protected final Connection dbConnection;
	
	/**
	 * Constructor 
	 * @param newDbConnection Connessione al DB
	 * @throws NullArgumentException Se il parametro {@link Connection} e'
	 * 		"null". */
	public BaseConnectedClass(Connection newDbConnection) 
		throws NullDBConnectionException {
		super();
		
		if ( newDbConnection == null )
			throw new NullDBConnectionException();
		
		this.dbConnection = newDbConnection;
	}
	
	/**
	 * @return Connessione al DB */
	public Connection getDbConnection() { return this.dbConnection; }
	
	/**
	 * Funzione di supporto utile per query di tipo INSERT, UPDATE e DELETE.
	 * 
	 * @param query Query da Eseguire
	 * @return Ritorna il numero di righe "affette" dalla query
	 * @throws SQLException */
	public int executeUpdateQuery (String query) throws SQLException {	
		Statement statement = this.dbConnection.createStatement();
		
		if( logger.isDebugEnabled() ) {
			logger.debug(query);	// Logging
		}
		
		return statement.executeUpdate(query);
	}
	
	/**
	 * Funzione di supporto utile per query di tipo SELECT.
	 * 
	 * @param query
	 * @return Ritorna il ResultSet prodotto dalla Query
	 * @throws SQLException */
	public ResultSet executeSelectQuery (String query) throws SQLException {
		Statement statement = this.dbConnection.createStatement(
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	
		if( logger.isDebugEnabled() ) {
			logger.debug(query);	// Logging
		}
		
		return statement.executeQuery(query);
	}
	
	/**
	 * @see Connection#setAutoCommit(boolean)
	 * @param autoCommit
	 * @throws SQLException */
	public void setAutoCommit (boolean autoCommit) throws SQLException {
		this.dbConnection.setAutoCommit(autoCommit);
	}
	
	/**
	 * @see Connection#setTransactionIsolation(int)
	 * @param transactionIsolationLevel
	 * @throws SQLException */
	public void setTransactionIsolation (int transactionIsolationLevel) 
		throws SQLException {
		this.dbConnection.setTransactionIsolation(transactionIsolationLevel);
	}
}
