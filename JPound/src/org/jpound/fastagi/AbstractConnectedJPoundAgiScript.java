/**
 * File: AbstractConnectedJPoundAgiScript.java
 * Created by: detro
 * Created at: Jul 19, 2006 */
package org.jpound.fastagi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.configuration.Configuration;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.jpound.db.BaseConnectedClass;
import org.jpound.db.ConfigurationsConnectionsPool;
import org.jpound.exception.NotInitiatedException;
import org.jpound.exception.NullDBConnection;

/**
 * Questa Classe eredita da {@link AbstractJPoundAgiScript} ed 
 * ha la caratteristica di essere utile come base di AgiScript
 * che necessitano della connessione al DB.
 * 
 * E' infatti composta anche della classe {@link BaseConnectedClass}, che
 * incorpora e di cui imita l'interfaccia, cosi' da fornire funzionionalita'
 * di accesso al DB ad un {@link AgiScript}.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.fastagi
 * @startdate Jul 19, 2006
 * @type AbstractConnectedJPoundAgiScript
 * @class AbstractConnectedJPoundAgiScript */
public abstract class AbstractConnectedJPoundAgiScript 
	extends AbstractJPoundAgiScript {
	
	/**
	 * Wrapper alla Connessione al DB che a sua volta e' wrappato 
	 * cosi' da fornire i suoi metodi alle sottoclassi di questa classe
	 * (ovvero ai {@link AbstractJPoundAgiScript} che necessitano 
	 * di una {@link Connection} verso il DB) */
	private final BaseConnectedClass connectedObject;
	
	/**
	 * Constructor.
	 * 
	 * @param jpoundConfiguration Configurazione di JPound 
	 * @param agiScriptConfiguration Configurazione dell'AgiScript 
	 * @throws NotInitiatedException */
	public AbstractConnectedJPoundAgiScript(
			Configuration configuration,
			Configuration agiScriptConfiguration ) 
		throws NotInitiatedException {
		
		super(configuration, agiScriptConfiguration);
		
		try {
			// Istanzio la classe connessa al DB
			this.connectedObject = new BaseConnectedClass( 
					ConfigurationsConnectionsPool.getConnection() );
		} catch (NullDBConnection e) {
			throw new NotInitiatedException(e);
		}
	}
	
	/**
	 * @return Connessione al DB */
	public Connection getDbConnection() {
		return this.connectedObject.getDbConnection();
	}

	/**
	 * @see AbstractJPoundAgiScript#service(AgiRequest, AgiChannel) */
	public abstract void service(AgiRequest request, AgiChannel channel)
			throws AgiException;
	
	/**
	 * @see BaseConnectedClass#executeUpdateQuery(String)
	 * 
	 * @param query Query da Eseguire
	 * @return Ritorna il numero di righe "affette" dalla query
	 * @throws SQLException */
	protected int executeUpdateQuery (String query) throws SQLException {	
		return this.connectedObject.executeUpdateQuery(query);
	}
	
	/**
	 * @see BaseConnectedClass#executeSelectQuery(String)
	 * 
	 * @param query
	 * @return Ritorna il ResultSet prodotto dalla Query
	 * @throws SQLException */
	protected ResultSet executeSelectQuery (String query) throws SQLException {
		return this.connectedObject.executeSelectQuery(query);
	}
	
	/**
	 * @see BaseConnectedClass#setAutoCommit(boolean)
	 * @param autoCommit
	 * @throws SQLException */
	protected void setAutoCommit (boolean autoCommit) throws SQLException {
		this.connectedObject.setAutoCommit(autoCommit);
	}
	
	/**
	 * @see BaseConnectedClass#setTransactionIsolation(int)
	 * @param transactionIsolationLevel
	 * @throws SQLException */
	protected void setTransactionIsolation (int transactionIsolationLevel) 
		throws SQLException {
		this.connectedObject.setTransactionIsolation(transactionIsolationLevel);
	}
}
