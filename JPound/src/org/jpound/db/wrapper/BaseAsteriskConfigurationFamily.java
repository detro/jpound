/**
 * File: BaseAsteriskConfigurationFamily.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.db.wrapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;

import org.jpound.db.BaseConnectedClass;
import org.jpound.exception.NullDBConnection;
import org.jpound.exception.UnableToDeleteMember;
import org.jpound.exception.UnableToInsertMember;
import org.jpound.exception.UnableToSelectMember;
import org.jpound.exception.UnableToUpdateMember;
import org.jpound.wrapper.AsteriskConfigurationFamily;
import org.jpound.wrapper.AsteriskConfigurationFile;

/**
 * Wrapper di Base alle Configuration Family. E' pensata per l'utilizzo nella
 * configurazione Realtime di Asterik.
 * 
 * Dato che ogni Family ha le sue tabelle particolari, con colonne e righe
 * diverse a seconda della Family, non si e' potuto pensare ad una soluzione
 * piu' "generica" come nel caso di {@link AsteriskConfigurationFile}: e'
 * quindi una Abstract Class, cosi' da forzarne l'estensione.
 * 
 * Le righe sono descritte attraverso Map: la chiave (key) indica la colonna, il
 * valore (value) il relativo valore. Data la semplicita' intrinseca delle
 * operazioni che bisognera' effettuare sulle righe, e' opportuno pensare a
 * delle condizioni (clausola "WHERE", per esempio) molto semplice, in cui si
 * predilige l'uso dell'uguaglianza (es.
 * <code>"WHERE column1 = value1 AND column2 = value2 ..."</code>) per
 * esprimere dei parametri di selezione.
 * 
 * TODO La classe non e' ancora implementata. E' qui descritta SOLO per
 * completezza.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.wrapper
 * @startdate Jul 23, 2006
 * @type BaseAsteriskConfigurationFamily
 */
public abstract class BaseAsteriskConfigurationFamily extends
		BaseConnectedClass implements AsteriskConfigurationFamily {

	/**
	 * Valore intero di Default. E' il valore di default per i campi di tipo
	 * Intero.
	 */
	public final static int DEFAULT_INT_VALUE = 0;

	/**
	 * Valore intero di Indefinito. E' il valore numerico che rappresenta
	 * "Indefinito" per i campi di tipo Intero.
	 */
	public final static int UNDEFINED_INT_VALUE = -999;

	/**
	 * Valore stringa di Default. E' il valore di default per i campi di tipo
	 * Stringa.
	 */
	public final static String DEFAULT_STRING_VALUE = "";

	/**
	 * @param newDbConnection
	 */
	public BaseAsteriskConfigurationFamily(Connection newDbConnection)
			throws NullDBConnection {
		super(newDbConnection);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see org.jpound.wrapper.AsteriskConfigurationFamily#deleteMember(java.util.Map)
	 */
	public int deleteMember(Map conditions) throws UnableToDeleteMember {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.jpound.wrapper.AsteriskConfigurationFamily#deleteMember(java.lang.String)
	 */
	public int deleteMember(String conditions) throws UnableToDeleteMember {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.jpound.wrapper.AsteriskConfigurationFamily#insertMember(java.util.Map)
	 */
	public void insertMember(Map values) throws UnableToInsertMember {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.jpound.wrapper.AsteriskConfigurationFamily#isValidMember(java.util.Map)
	 */
	public boolean isValidMember(Map valuesToValidate) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see org.jpound.wrapper.AsteriskConfigurationFamily#selectMember(java.util.Map)
	 */
	public ResultSet selectMember(Map conditions) throws UnableToSelectMember {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jpound.wrapper.AsteriskConfigurationFamily#selectMember(java.lang.String)
	 */
	public ResultSet selectMember(String conditions)
			throws UnableToSelectMember {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.jpound.wrapper.AsteriskConfigurationFamily#updateMember(java.util.Map,
	 *      java.util.Map)
	 */
	public int updateMember(Map values, Map conditions)
			throws UnableToUpdateMember {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see org.jpound.wrapper.AsteriskConfigurationFamily#updateMember(java.util.Map,
	 *      java.lang.String)
	 */
	public int updateMember(Map values, String conditions)
			throws UnableToUpdateMember {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
