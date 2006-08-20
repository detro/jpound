/**
 * File: AsteriskConfigurationFile.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.wrapper;

import java.io.File;
import java.sql.ResultSet;

import org.jpound.exception.TooManyMatchException;
import org.jpound.exception.UnableToGetVariableException;
import org.jpound.exception.UnableToSetVariableException;
import org.jpound.exception.UnableToUnsetVariableException;
import org.jpound.exception.UnableToUpdateVariableException;
import org.jpound.exception.VariableNotFoundException;

/**
 * Definisce l'interfaccia necessaria per implementare un wrapper ai file di
 * configurazione di Asterisk.
 * 
 * L'interfaccia puo' essere implementata in piu modi. Ad esempio:
 * <ol>
 * <li> per realizzare un interfacciamento al DB quando Asterisk e' in modalita'
 * Realtime </li>
 * <li> per manipolare i normali file di configurazione di Asterisk (che sono in
 * formato "INI") </li>
 * </ol>
 * 
 * Questo e' possibile perche' si usa il nome del file come un "handler"
 * verso le informazioni contenute nello stesso, che esso esista fisicamente
 * (sul filesystem) o meno (una tabella nel db).
 * 
 * @author detro
 * @type AsteriskConfigurationFile
 * @class AsteriskConfigurationFile */
public interface AsteriskConfigurationFile {

	/**
	 * Setta una Variabile nel file di Configurazione Wrappato.
	 * 
	 * @param category
	 *            Categoria in cui inserire la variabile. La crea se non esiste
	 *            ancora.
	 * 
	 * @param varName
	 *            Nome variabile
	 * @param varName
	 *            Valore variabile
	 * @param categoryMetric
	 *            Serve per ordinare le categorie in base alla prioritˆ. Minore
	 *            il valore, maggiore la priorita'. Anche valori negativi sono
	 *            validi. E' consigliato comunque di usare sempre il valore 0.
	 * 
	 * @param varMetric
	 *            Stesse caratteristiche del parametro "categoryMetric"
	 * @param commented
	 *            Indica se questo valore e' attuamente commentato: serve per ad
	 *            eliminare una variabile senza doverla cancellare dal file di
	 *            configurazione, al fine di agevolare usi futuri.
	 * 
	 * @throws UnableToSetVariableException
	 */
	public void setVariable(String category, String varName, String varValue,
			int categoryMetric, int varMetric, int commented)
			throws UnableToSetVariableException;

	/**
	 * Come per "setVariable(String, String, String, int, int, int)", dove pero'
	 * i parametri "categoryMetric", "varMetric" e "commented" sono settati a
	 * "0". E' consigliato e preferibile l'uso di questo metodo per settare una
	 * variabile.
	 * 
	 * @param category
	 * @param varName
	 * @param varValue
	 * @throws UnableToSetVariableException
	 * @see AsteriskConfigurationFile#setVariable(String, String, String, int,
	 *      int, int)
	 */
	public void setVariable(String category, String varName, String varValue)
			throws UnableToSetVariableException;

	/**
	 * Aggiorna il valore di una Variabile. La variabile e' identificata in base
	 * alla coppia "categoria"-"nome".
	 * 
	 * @param category
	 * @param varName
	 * @param varValue
	 * @param categoryMetric
	 * @param varMetric
	 * @param commented
	 * @throws UnableToUpdateVariableException
	 */
	public void updateVariable(String category, String varName,
			String varValue, int categoryMetric, int varMetric, int commented)
			throws UnableToUpdateVariableException, VariableNotFoundException;

	/**
	 * Aggiorna il valore di una Variabile. La variabile e' identificata in base
	 * alla coppia "categoria"-"nome".
	 * 
	 * @see AsteriskConfigurationFile#updateVariable(String, String, String,
	 *      int, int, int)
	 * 
	 * @param category
	 * @param varName
	 * @param varValue
	 * @throws UnableToUpdateVariableException
	 */
	public void updateVariable(String category, String varName, String varValue)
			throws UnableToUpdateVariableException, VariableNotFoundException;

	/**
	 * Elimina una Variable dal file di Configurazione. La variabile e'
	 * identificata in base alla coppia "categoria"-"nome".
	 * 
	 * @param category
	 * @param varName
	 * @throws UnableToUnsetVariableException
	 * @throws VariableNotFoundException
	 */
	public void unsetVariable(String category, String varName)
			throws UnableToUnsetVariableException, VariableNotFoundException;

	/**
	 * Recupera il valore di una variabile. La variabile e' identificata in base
	 * alla coppia "categoria"-"nome".
	 * 
	 * @param category
	 * @param varName
	 * @return Stringa contenente il valore della Variabile
	 * @throws UnableToGetVariableException
	 * @throws VariableNotFoundException
	 */
	public String getVariable(String category, String varName)
			throws UnableToGetVariableException, VariableNotFoundException,
			TooManyMatchException;

	/**
	 * @return Tutte le Variabili presenti in questo file di configurazione
	 *         nella forma di un ResultSet.
	 * 
	 * @throws UnableToGetVariableException
	 */
	public ResultSet getAllVariables() throws UnableToGetVariableException;

	/**
	 * @see AsteriskConfigurationFile#getAllVariables()
	 * 
	 * @param category
	 *            Se "null", ritorna tutte le variabili.
	 * @return Tutte le Variabili presenti in questo file di configurazione, con
	 *         categoria uguale a "category".
	 * 
	 * @throws UnableToGetVariableException
	 */
	public ResultSet getAllVariables(String category)
			throws UnableToGetVariableException;

	/** @see java.lang.Object#toString() */
	public String toString();

	/**
	 * Scrive la configurazione di questo AsteriskConfigurationFile su un File
	 * nella forma attesa da Asterisk per i suoi file di configurazione, quando
	 * non e' settato nella modalita' Realtime. In pratica, nel formato "INI".
	 * 
	 * @param physicalFile
	 *            dove scrivere la configurazione
	 */
	public void toPhysicalFile(File physicalFile);
}
