/**
 * File: BaseAsteriskConfigurationFile.java
 * Created by: detro
 * Created at: Jun 25, 2006 */
package org.jpound.db.wrapper;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jpound.db.BaseConnectedClass;
import org.jpound.exception.NullDBConnectionException;
import org.jpound.exception.TableNotFoundException;
import org.jpound.exception.TooManyMatchException;
import org.jpound.exception.UnableToGetVariableException;
import org.jpound.exception.UnableToSetVariableException;
import org.jpound.exception.UnableToUnsetVariableException;
import org.jpound.exception.UnableToUpdateVariableException;
import org.jpound.exception.UnableToWrapConfigurationFileException;
import org.jpound.exception.VariableNotFoundException;
import org.jpound.wrapper.AsteriskConfigurationFile;

/**
 * Questa Classe e' utile come esempio concreto di Wrapper ai file di
 * configurazione. Si puo' pensare di estenderla per aggiungere delle
 * funzionalita' particolari, ma cosi' com'e' dovrebbe gia' essere sufficiente
 * per permettere di manipolare i file di configurazione di Asterisk in modalita
 * "Realtime-Static". Questi Wrapper servono ad operare sui file mappati nel DB
 * in modalita "Realtime-Static".
 * <code>http://voip-info.org/wiki/view/Asterisk+Realtime+Static</code>.
 * Tutti i file mappati in "Realtime-Static" hanno i loro parametri inseriti in
 * una tabella che dispone delle seguenti colonne principali:
 * <ul>
 * <li>filename</li>
 * <li>category</li>
 * <li>var_name</li>
 * <li>var_value</li>
 * </ul>
 * Questo permette di inserire tutti i parametri della configurazione in
 * un'unica tabella e poi differenziarli in base al parametro "filename"
 * (ovviamente si tratta di una "peculiarita'" del modello Asterisk
 * Realtime-Static, che qui sfruttiamo a nostro favore). Tutte le Variabili che
 * vengono inserite nel DB sono associate ad un identificativo univoco ("id")
 * utile per le operazioni di "Update", "Unset" e "Get": quindi, oltre alle
 * normali operaziono di recupero basate sulla coppia
 * <code>"filename"-"var_name"</code> 
 * ({@link AsteriskConfigurationFile#getVariable(String, String)}),
 * e' possibile accedere alle variabili tramite la chiave <code>"id"</code>.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.wrapper
 * @startdate Jun 25, 2006
 * @type BaseAsteriskConfigurationFile
 * @class BaseAsteriskConfigurationFile
 */
public class BaseAsteriskConfigurationFile extends BaseConnectedClass implements
		AsteriskConfigurationFile {
	
	
	/**
	 * Nome della Tabella del Database che Mappa il file di configurazione
	 */
	private final String		tableName;
	
	
	
	/**
	 * Nome del file di configurazione Wrappato da questa classe.
	 */
	private final String		configurationFilename;
	
	
	
	/**
	 * Valore stringa di Default. E' il valore di default per i campi di tipo
	 * Stringa.
	 */
	public final static String	DEFAULT_STRING_VALUE	= "";
	
	
	
	/**
	 * Valore intero di Indefinito. E' il valore numerico che rappresenta
	 * "Indefinito" per i campi di tipo Intero.
	 */
	public final static int		UNDEFINED_INT_VALUE		= -999;
	
	
	
	/**
	 * Valore intero di Default. E' il valore di default per i campi di tipo
	 * Intero.
	 */
	public final static int		DEFAULT_INT_VALUE		= 0;
	
	
	
	/**
	 * Logger basato su Log4j.
	 */
	private static Logger		logger					= Logger
																.getLogger ( BaseAsteriskConfigurationFile.class );
	
	
	
	
	/**
	 * Constructor
	 * 
	 * @param dbConnection
	 *            Connessione al DB
	 * @param tableName
	 *            Nome della Tabella che contiene i valori
	 * @param configurationFilename
	 *            Nome del file di Configurazione Wrappato
	 * @throws TableNotFoundException
	 * @throws UnableToWrapConfigurationFileException
	 */
	public BaseAsteriskConfigurationFile(Connection dbConnection,
			String tableName, String configurationFilename)
			throws TableNotFoundException,
			UnableToWrapConfigurationFileException, NullDBConnectionException {
		
		
		// Inizializzo la classe di Base BaseConnectedClass
		super ( dbConnection );
		
		this.tableName = tableName;
		this.configurationFilename = configurationFilename;
		
		
		// Controllo che la Tabella che contiene la Configurazione esista
		try {
			ResultSet searchedTable = dbConnection.getMetaData ().getTables (
					null, null, this.tableName, null );
			
			try {
				if (!searchedTable.next ()
						|| !searchedTable.getString ( "TABLE_NAME" ).equals (
								this.tableName )) {
					throw new TableNotFoundException ( "table name: "
							+ this.tableName );
				}
			} catch (Exception e) {
				throw new TableNotFoundException ( "table name: "
						+ this.tableName );
			}
		} catch (SQLException e) {
			throw new UnableToWrapConfigurationFileException ( "table name: "
					+ this.tableName );
		}
		
		if (logger.isDebugEnabled ()) {
			logger.debug ( "Wrapper for Asterisk Configuration File initiated"
					+ " => tablename: " + this.tableName + " - "
					+ BaseAsteriskConfigurationColumnNames.COL_FILENAME + ": "
					+ this.configurationFilename );
		}
	}
	
	
	
	/**
	 * @see AsteriskConfigurationFile#setVariable(String, String, String, int,
	 *      int, int)
	 */
	public void setVariable(String category, String varName, String varValue,
			int categoryMetric, int varMetric, int commented)
			throws UnableToSetVariableException {
		
		
		// Preparazione della query
		String query = "INSERT INTO " + this.tableName + " ("
				+ BaseAsteriskConfigurationColumnNames.COL_FILENAME + ", "
				+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY + ", "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_NAME + ", "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_VALUE
				+ ", "
				+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY_METRIC
				+ ", "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_METRIC
				+ ", " + BaseAsteriskConfigurationColumnNames.COL_COMMENTED
				+ ") " + "VALUES (" + "'" + this.configurationFilename + "', "
				+ "'" + category + "', " + "'" + varName + "', " + "'"
				+ varValue + "', " + categoryMetric + ", " + varMetric + ", "
				+ commented + ")";
		
		
		// Esecuzione della Query
		try {
			this.executeUpdateQuery ( query );
		} catch (SQLException e) {
			logger.error ( e );
			throw new UnableToSetVariableException ( e.toString () );
		}
	}
	
	
	
	/**
	 * @see AsteriskConfigurationFile#setVariable(String, String, String)
	 */
	public void setVariable(String category, String varName, String varValue)
			throws UnableToSetVariableException {
		
		
		// Richiamo il metodo "overloadato", ma con i valori di
		// "metrica" e "commented" settati a
		// "AsteriskConfigurationFile.DEFAULT_INT_VALUE"
		this.setVariable ( category, varName, varValue,
				BaseAsteriskConfigurationFile.DEFAULT_INT_VALUE,
				BaseAsteriskConfigurationFile.DEFAULT_INT_VALUE,
				BaseAsteriskConfigurationFile.DEFAULT_INT_VALUE );
	}
	
	
	
	/**
	 * @see AsteriskConfigurationFile#updateVariable(String, String, String,
	 *      int, int, int)
	 */
	public void updateVariable(String category, String varName,
			String varValue, int categoryMetric, int varMetric, int commented)
			throws UnableToUpdateVariableException, VariableNotFoundException {
		
		
		// Preparazione della query
		String query = "UPDATE " + this.tableName + " SET "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_VALUE
				+ " = '" + varValue + "' ";
		
		
		// Controlla quali valori sono "Indefiniti"
		query += (categoryMetric != BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE) ? ", "
				+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY_METRIC
				+ " = " + categoryMetric
				: "";
		query += (varMetric != BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE) ? ", "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_METRIC
				+ " = " + varMetric
				: "";
		query += (commented != BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE) ? ", "
				+ BaseAsteriskConfigurationColumnNames.COL_COMMENTED
				+ " = "
				+ commented
				: "";
		
		query += "WHERE " + BaseAsteriskConfigurationColumnNames.COL_FILENAME
				+ " = '" + this.configurationFilename + "' AND "
				+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY + " = '"
				+ category + "' AND "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_NAME
				+ " = '" + varName + "' ";
		
		
		// Esecuzione della Query
		try {
			// Controlla il numero di righe affette da questa Query
			if (this.executeUpdateQuery ( query ) == 0) {
				throw new VariableNotFoundException (
						BaseAsteriskConfigurationColumnNames.COL_FILENAME
								+ ": "
								+ this.tableName
								+ " - "
								+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY
								+ ": "
								+ category
								+ " - "
								+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_NAME
								+ ": " + varName );
			}
		} catch (SQLException e) {
			logger.error ( e );
			throw new UnableToUpdateVariableException ( e.toString () );
		}
	}
	
	
	
	/**
	 * @see AsteriskConfigurationFile#updateVariable(String, String, String)
	 */
	public void updateVariable(String category, String varName, String varValue)
			throws UnableToUpdateVariableException, VariableNotFoundException {
		
		
		// Richiamo il metodo "overloadato", ma con i valori di
		// "metrica" e "commented" settati a
		// "AsteriskConfigurationFile.DEFAULT_INT_VALUE"
		this.updateVariable ( category, varName, varValue,
				BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE,
				BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE,
				BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE );
	}
	
	
	
	/**
	 * Aggiorna il valore di una Variabile. La variabile e' identificata in base
	 * all'"id" univoco.
	 * 
	 * @param varValue
	 * @param categoryMetric
	 * @param varMetric
	 * @param commented
	 * @param id
	 * @throws UnableToUpdateVariableException
	 * @see AsteriskConfigurationFile#updateVariable(String, int, int, int,
	 *      long)
	 */
	public void updateVariable(String varValue, int categoryMetric,
			int varMetric, int commented, long id)
			throws UnableToUpdateVariableException, VariableNotFoundException {
		
		
		// Preparazione della query
		String query = "UPDATE " + this.tableName + " SET "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_VALUE
				+ " = '" + varValue + "' ";
		
		
		// Controlla quali valori sono "Indefiniti"
		query += (categoryMetric != BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE) ? ", "
				+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY_METRIC
				+ " = " + categoryMetric
				: "";
		query += (varMetric != BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE) ? ", "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_METRIC
				+ " = " + varMetric
				: "";
		query += (commented != BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE) ? ", "
				+ BaseAsteriskConfigurationColumnNames.COL_COMMENTED
				+ " = "
				+ commented
				: "";
		
		query += " WHERE " + BaseAsteriskConfigurationColumnNames.COL_FILENAME
				+ " = '" + this.configurationFilename + "' AND "
				+ BaseAsteriskConfigurationColumnNames.COL_ID + " = " + id;
		
		
		// Esecuzione della Query
		try {
			// Controlla il numero di righe affette da questa Query
			if (this.executeUpdateQuery ( query ) == 0) {
				throw new VariableNotFoundException (
						BaseAsteriskConfigurationColumnNames.COL_FILENAME
								+ ": " + this.tableName + " - "
								+ BaseAsteriskConfigurationColumnNames.COL_ID
								+ ": " + id );
			}
		} catch (SQLException e) {
			logger.error ( e );
			throw new UnableToUpdateVariableException ( e.toString () );
		}
	}
	
	
	
	/**
	 * Aggiorna il valore di una Variabile. La variabile e' identificata in base
	 * all'"id" univoco. Preferire l'uso di questo metodo.
	 * 
	 * @param varValue
	 * @param id
	 * @throws UnableToUpdateVariableException
	 * @see AsteriskConfigurationFile#updateVariable(String, long)
	 */
	public void updateVariable(String varValue, long id)
			throws UnableToUpdateVariableException, VariableNotFoundException {
		
		
		// Richiamo il metodo "overloadato", ma con i valori di
		// "metrica" e "commented" settati a
		// "AsteriskConfigurationFile.DEFAULT_INT_VALUE"
		this.updateVariable ( varValue,
				BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE,
				BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE,
				BaseAsteriskConfigurationFile.UNDEFINED_INT_VALUE, id );
	}
	
	
	
	/**
	 * @see AsteriskConfigurationFile#unsetVariable(String, String)
	 */
	public void unsetVariable(String category, String varName)
			throws UnableToUnsetVariableException, VariableNotFoundException {
		
		
		// Preparazione della query
		String query = "DELETE FROM " + this.tableName + " WHERE "
				+ BaseAsteriskConfigurationColumnNames.COL_FILENAME + " = '"
				+ this.configurationFilename + "' AND "
				+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY + " = '"
				+ category + "' AND "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_NAME
				+ " = '" + varName + "'";
		
		
		// Esecuzione della Query
		try {
			// Controlla il numero di righe affette da questa Query
			if (this.executeUpdateQuery ( query ) == 0) {
				throw new VariableNotFoundException (
						BaseAsteriskConfigurationColumnNames.COL_FILENAME
								+ ": "
								+ this.tableName
								+ " - "
								+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY
								+ ": "
								+ category
								+ " - "
								+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_NAME
								+ ": " + varName );
			}
		} catch (SQLException e) {
			logger.error ( e );
			throw new UnableToUnsetVariableException ( e.toString () );
		}
	}
	
	
	
	/**
	 * Elimina una Variable dal file di Configurazione. La variabile e'
	 * identificata in base all'"id" univoco.
	 * 
	 * @param id
	 * @throws UnableToUnsetVariableException
	 * @throws VariableNotFoundException
	 * @see AsteriskConfigurationFile#unsetVariable(long)
	 */
	public void unsetVariable(long id) throws UnableToUnsetVariableException,
			VariableNotFoundException {
		
		
		// Preparazione della query
		String query = "DELETE FROM " + this.tableName + " WHERE "
				+ BaseAsteriskConfigurationColumnNames.COL_ID + " = " + id;
		
		
		// Esecuzione della Query
		try {
			// Controlla il numero di righe affette da questa Query
			if (this.executeUpdateQuery ( query ) == 0) {
				throw new VariableNotFoundException (
						BaseAsteriskConfigurationColumnNames.COL_FILENAME
								+ ": " + this.tableName + " - "
								+ BaseAsteriskConfigurationColumnNames.COL_ID
								+ ": " + id );
			}
		} catch (SQLException e) {
			logger.error ( e );
			throw new UnableToUnsetVariableException ( e.toString () );
		}
	}
	
	
	
	/**
	 * @see AsteriskConfigurationFile#getVariable(String, String)
	 */
	public String getVariable(String category, String varName)
			throws UnableToGetVariableException, VariableNotFoundException,
			TooManyMatchException {
		
		
		// ResultSet della query
		ResultSet queryResultSet = null;
		
		
		// Preparazione della query
		String query = "SELECT "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_VALUE
				+ " FROM " + this.tableName + " WHERE "
				+ BaseAsteriskConfigurationColumnNames.COL_FILENAME + " = '"
				+ this.configurationFilename + "' AND "
				+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY + " = '"
				+ category + "' AND "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_NAME
				+ " = '" + varName + "'";
		
		
		// Esecuzione della Query
		try {
			queryResultSet = this.executeSelectQuery ( query );
			
			try {
				// Recupero il valore desiderato
				queryResultSet.next ();
				
				
				// Mi assicuro che sia l'ultima riga disponibile
				if (!queryResultSet.isLast ()) {
					throw new TooManyMatchException (
							BaseAsteriskConfigurationColumnNames.COL_FILENAME
									+ ": "
									+ this.tableName
									+ " - "
									+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY
									+ ": "
									+ category
									+ " - "
									+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_NAME
									+ ": " + varName );
				}
				
				return queryResultSet.getString ( "var_val" );
			} catch (SQLException e) {
				throw new VariableNotFoundException (
						BaseAsteriskConfigurationColumnNames.COL_FILENAME
								+ ": "
								+ this.tableName
								+ " - "
								+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY
								+ ": "
								+ category
								+ " - "
								+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_NAME
								+ ": " + varName );
			}
		} catch (SQLException e) {
			logger.error ( e );
			throw new UnableToGetVariableException ( e.toString () );
		}
	}
	
	
	
	/**
	 * Recupera il valore di una variabile. La variabile e' identificata in base
	 * all'"id" univoco.
	 * 
	 * @param id
	 * @return Stringa contenente il valore della Variabile
	 * @throws UnableToGetVariableException
	 * @throws VariableNotFoundException
	 * @see AsteriskConfigurationFile#getVariable(long)
	 */
	public String getVariable(long id) throws UnableToGetVariableException,
			VariableNotFoundException, TooManyMatchException {
		
		
		// ResultSet della query
		ResultSet queryResultSet = null;
		
		
		// Preparazione della query
		String query = "SELECT "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_VALUE
				+ " FROM " + this.tableName + " WHERE "
				+ BaseAsteriskConfigurationColumnNames.COL_FILENAME + " = '"
				+ this.configurationFilename + "' AND "
				+ BaseAsteriskConfigurationColumnNames.COL_ID + " = " + id;
		
		
		// Esecuzione della Query
		try {
			queryResultSet = this.executeSelectQuery ( query );
			
			try {
				// Recupero il valore desiderato
				queryResultSet.next ();
				
				
				// Mi assicuro che sia l'ultima riga disponibile
				if (!queryResultSet.isLast ()) {
					throw new TooManyMatchException (
							BaseAsteriskConfigurationColumnNames.COL_FILENAME
									+ ": "
									+ this.tableName
									+ " - "
									+ BaseAsteriskConfigurationColumnNames.COL_ID
									+ ": " + id );
				}
				
				return queryResultSet.getString ( "var_val" );
			} catch (SQLException e) {
				throw new VariableNotFoundException (
						BaseAsteriskConfigurationColumnNames.COL_FILENAME
								+ ": " + this.tableName + " - "
								+ BaseAsteriskConfigurationColumnNames.COL_ID
								+ ": " + id );
			}
		} catch (SQLException e) {
			logger.error ( e );
			throw new UnableToGetVariableException ( e.toString () );
		}
	}
	
	
	
	/**
	 * @see AsteriskConfigurationFile#getAllVariables()
	 */
	public ResultSet getAllVariables() throws UnableToGetVariableException {
		return this.getAllVariables ( null );
	}
	
	
	
	/**
	 * @see AsteriskConfigurationFile#getAllVariables(String)
	 */
	public ResultSet getAllVariables(String category)
			throws UnableToGetVariableException {
		
		
		// Preparazione della query
		String query = "SELECT * FROM " + this.tableName + " WHERE "
				+ BaseAsteriskConfigurationColumnNames.COL_FILENAME + " = '"
				+ this.configurationFilename + "' ";
		
		query += (category != null) ? " AND "
				+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY + " = '"
				+ category + "' " : "";
		
		query += " ORDER BY "
				+ BaseAsteriskConfigurationColumnNames.COL_CATEGORY + ", "
				+ BaseAsteriskConfigurationColumnNames.COL_VARIABLE_NAME;
		
		
		// Esecuzione della Query
		try {
			return this.executeSelectQuery ( query );
		} catch (SQLException e) {
			logger.error ( e );
			throw new UnableToGetVariableException ( e.toString () );
		}
	}
	
	
	
	/**
	 * @return Ritorna una stringa che descrive l'oggetto: tutte le variabili
	 *         presenti in questo AsteriskConfigurationFile.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer ();
		
		try {
			// Recupero tutte le Variabili
			ResultSet allVars = this.getAllVariables ();
			
			
			// Preparo il buffer del risultato
			while (allVars.next ()) {
				buffer.append ( allVars.getRow () + " => " + "id: "
						+ allVars.getLong ( "id" ) + " \t " + "filename: "
						+ allVars.getString ( "filename" ) + " \t "
						+ "category: " + allVars.getString ( "category" )
						+ " \t " + "var_name: "
						+ allVars.getString ( "var_name" ) + " \t "
						+ "var_value: " + allVars.getString ( "var_val" )
						+ " \t " + "cat_metric: "
						+ allVars.getInt ( "cat_metric" ) + " \t "
						+ "var_metric: " + allVars.getInt ( "var_metric" )
						+ " \t " + "commented: "
						+ allVars.getInt ( "commented" ) + " \r\n" );
			}
		} catch (UnableToGetVariableException e) {
			logger.error ( e );
		} catch (SQLException e) {
			logger.error ( e );
		}
		
		return buffer.toString ();
	}
	
	
	
	/**
	 * TODO Non ancora implementato.
	 * 
	 * @see AsteriskConfigurationFile#toPhysicalFile(File)
	 */
	public void toPhysicalFile(File physicalFile) {
	}
}
