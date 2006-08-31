/**
 * File: AsteriskConfigurationFileTest.java
 * Created by: detro
 * Created at: Jun 25, 2006 */
package org.jpound.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jpound.db.wrapper.BaseAsteriskConfigurationFile;
import org.jpound.exception.NullDBConnectionException;
import org.jpound.exception.TableNotFoundException;
import org.jpound.exception.TooManyMatchException;
import org.jpound.exception.UnableToGetVariableException;
import org.jpound.exception.UnableToUnsetVariableException;
import org.jpound.exception.UnableToUpdateVariableException;
import org.jpound.exception.UnableToWrapConfigurationFileException;
import org.jpound.exception.VariableNotFoundException;
import org.jpound.wrapper.AsteriskConfigurationFilenames;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.test.wrapper
 * @startdate Jun 25, 2006
 * @type AsteriskConfigurationFileTest */
public class AsteriskConfigurationFileTest {

	private static Logger logger = 
		Logger.getLogger(AsteriskConfigurationFileTest.class);
	
	/**
	 * Constructor */
	public AsteriskConfigurationFileTest() {
		super();
	}

	/**
	 * @param args */
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		
		Connection dbConnection = null;
		try {
			Class driverClass = Class.forName("org.postgresql.Driver");
			DriverManager.registerDriver((Driver)driverClass.newInstance());
			dbConnection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/asterisk_db",
					"asterisk_user",
					"asterisk_user");
			
			logger.debug("Connected to the DB");
		} catch (ClassNotFoundException e) {
			logger.fatal(e);
		} catch (SQLException e) {
			logger.fatal(e);
		} catch (InstantiationException e) {
			logger.fatal(e);
		} catch (IllegalAccessException e) {
			logger.fatal(e);
		}
		
		// Istanzio una classe BaseAsteriskConfigurationFile
		BaseAsteriskConfigurationFile astConfigFile = null;
		try {
			astConfigFile = new BaseAsteriskConfigurationFile(
					dbConnection,
					"asterisk_configuration",
					AsteriskConfigurationFilenames.VOICEMAIL_CONF);
		} catch (TableNotFoundException e1) {
			logger.error(e1);
		} catch (UnableToWrapConfigurationFileException e1) {
			logger.error(e1);
		} catch (NullDBConnectionException e1) {
			logger.error(e1);
		}
		
//		try {
//			astConfigFile.setVariable("cat", "var", "val", 0, 1, 2);
//		} catch (UnableToSetVariableException e) {
//			logger.fatal(e);
//		}
		
		try {
			astConfigFile.updateVariable("cat", "var", "valore alterato");
//			astConfigFile.updateVariable("blea", 100);
		} catch (UnableToUpdateVariableException e) {
			logger.error(e);
		} catch (VariableNotFoundException e) {
			logger.error(e);
		}
		
		try {
			astConfigFile.unsetVariable("cat", "var");
		} catch (UnableToUnsetVariableException e) {
			logger.error(e);
		} catch (VariableNotFoundException e) {
			logger.error(e);
		}
		
		try {
			logger.debug("cat: general - var_name: format => " + 
					astConfigFile.getVariable("general", "format") );
		} catch (UnableToGetVariableException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (VariableNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (TooManyMatchException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		try {
			ResultSet allVars = astConfigFile.getAllVariables("general");
			
			while ( allVars.next() ) {
				logger.debug("filename => "+ allVars.getString("filename") + " category => " + allVars.getString("category") + " var_name => " + allVars.getString("var_name") + " var_value => " + allVars.getString("var_val") );
			}
		} catch (UnableToGetVariableException e) {
			logger.error(e);
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		logger.debug(astConfigFile.toString());
	}

}
