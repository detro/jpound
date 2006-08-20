/**
 * File: DefaultAgiServerThreadConfigurationXPaths.java
 * Created by: detro
 * Created at: Jul 3, 2006 */
package org.jpound.fastagi;

/**
 * Questa Interfaccia raccoglie gli XPath verso le chiavi del file di configurazione
 * XML based che usa JPound.
 * Questo tipo di tecnica per la configurazione si e' preferita perche':
 * <ol>
 *  <li>e' XML Based</li>
 *  <li>
 *  il codice necessario era gia' disponibile dal progetto 
 *  Jakarta Commons Configuration
 *  (@link http://jakarta.apache.org/commons/configuration/index.html)
 *  </li>
 * </ol>
 * 
 * @author detro
 * @project JPound
 * @package org.jpound
 * @startdate Jul 3, 2006
 * @type DefaultAgiServerThreadConfigurationXPaths */
public interface DefaultAgiServerThreadConfigurationXPaths {
	public static String Port = "fastagi.server.port";
	public static String ConnectionPoolSize = "fastagi.server.poolsize";
	public static String ConnectionPoolMaxSize = "fastagi.server.poolmaxsize";
}
