/**
 * File: CoreServerConfigurationXPaths.java
 * Created by: detro
 * Created at: Jul 3, 2006 */
package org.jpound;

/**
 * @author detro
 * @project JPound
 * @package org.jpound
 * @startdate Jul 3, 2006
 * @type CoreServerConfigurationXPaths
 * 
 * Questa Interfaccia raccoglie gli XPath verso le chiavi del file di
 * configurazione XML based che usa JPound. Questo tipo di tecnica per la
 * configurazione si e' preferita perche': a) e' XML based e b) il codice
 * necessario era gia' disponibile dal progetto Jakarta Commons Configuration
 * (<code>http://jakarta.apache.org/commons/configuration/index.html</code>) */
public interface CoreServerConfigurationXPaths {
	public final static String TABLE_NAMES = 
		"coreserver.configfiles.file.tablename";

	public final static String FILE_NAMES = 
		"coreserver.configfiles.file.filename";

	public final static String DB_CONNECTION_USERNAME = 
		"coreserver.dbconnection.username";

	public final static String DB_CONNECTION_PASSWORD = 
		"coreserver.dbconnection.password";

	public final static String DB_CONNECTION_DSN = 
		"coreserver.dbconnection.dsn";

	public final static String DB_CONNECTION_DRIVER = 
		"coreserver.dbconnection.driver";

	public final static String CONFIG_FILES = 
		"coreserver.configfiles.file";

	public final static String LOG4J_CONFIG_FILEPATH = 
		"coreserver.logging.configfilename";

	public final static String AVERAGE_CONFIG_FILE_NUMBER = 
		"coreserver.configfiles[@averagenum]";

	public final static String AVERAGE_CONFIG_FAMILY_NUMBER = 
		"coreserver.configfamilies[@averagenum]";
}
