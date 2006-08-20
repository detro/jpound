/**
 * File: PluggableMappingStrategyConfigurationXPaths.java
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
 *  (<code>http://jakarta.apache.org/commons/configuration/index.html</code>)
 *  </li>
 * </ol>
 * 
 * @author detro
 * @project JPound
 * @package org.jpound
 * @startdate Jul 3, 2006
 * @type PluggableMappingStrategyConfigurationXPaths */
public interface PluggableMappingStrategyConfigurationXPaths {
	public final static String AGISCRIPT_DIRECTORY = 
		"fastagi.mapping.scriptsdir";
	public final static String AGISCRIPT_DESCRIPTOR_ALIASES = 
		"aliases.alias";
}
