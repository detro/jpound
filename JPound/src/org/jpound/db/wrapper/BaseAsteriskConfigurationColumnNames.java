/**
 * File: AsteriskConfigurationFilenames.java
 * Created by: detro
 * Created at: Jun 25, 2006 */
package org.jpound.db.wrapper;

/**
 * Quesa Interfaccia funge da "contenitore di costanti" per i nomi
 * delle colonne della tabella che contiene gli "AsteriskConfigurationFile".
 * Questo permette un certo grado di flessibilita' nel caso in cui queste
 * colonne cambino di nome.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.wrapper
 * @startdate Jun 25, 2006
 * @type AsteriskConfigurationFilenames */
public interface BaseAsteriskConfigurationColumnNames {
	public final static String COL_FILENAME = "filename";
	public final static String COL_CATEGORY = "category";
	public final static String COL_VARIABLE_NAME = "var_name";
	public final static String COL_VARIABLE_VALUE = "var_val";
	public final static String COL_CATEGORY_METRIC = "cat_metric";
	public final static String COL_VARIABLE_METRIC = "var_metric";
	public final static String COL_COMMENTED = "commented";
	public final static String COL_ID = "id";
}
