/**
 * File: ConfigurationsConnectionsPool.java
 * Created by: detro
 * Created at: Aug 16, 2006 */
package org.jpound.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.jpound.CoreServerConfigurationXPaths;
import org.jpound.exception.NotInitiatedException;

/**
 * Questa classe svolge il ruolo di contenitore di {@link Connection} al
 * Data Base. 
 * 
 * E' un Pool di Connessioni, ognuna associata ad una particolare 
 * {@link Configuration}, creato secondo il formato di JPound.
 * Permette quindi di avere tante connessioni per quanti
 * oggetti {@link Configuration} vengono inseriti nel Pool.
 * 
 * E' importante notare che la prima Configurazione fornita diverra' quella
 * di Default, quindi, dopo la prima Configuration inserita, ogni chiamata
 * al metodo {@link ConfigurationsConnectionsPool#getConnection()} ritorna
 * la stessa {@link Connection}.
 * 
 * E' una Classe con solo metodi Statici, cosi' da comportarsi come
 * una Static Singleton Class e poter quindi essere condivisa 
 * tra tutti gli Oggetti attualmente istanziati. Questo permette di snellire 
 * la stesura del codice, evitando inutili
 * passaggi della connessione al DB di classe in classe.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound
 * @startdate Jul 8, 2006
 * @type ConfigurationsConnectionsPool
 * @class ConfigurationsConnectionsPool */
public class ConfigurationsConnectionsPool {

	private static Logger logger = Logger
			.getLogger(ConfigurationsConnectionsPool.class);

	/**
	 * Prima {@link Configuration} Inserita nel Pool. Dopo la prima
	 * inizializzazione, sara' quella di Default. */
	private static Configuration firstConfiguration = null;

	/** 
	 * Pool delle {@link Connection}.
	 * Ogni Connection e' associata ad una Configuration. */
	private static HashMap<Configuration, Connection> connectionsPool = 
		new HashMap<Configuration, Connection>();

	/**
	 * Realizza una {@link Connection} al DB.
	 * 
	 * @param config Configurazione con cui costruire la Connection.
	 * @return una Connection costruita in base alla Configuration fornita
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException */
	private static Connection makeConnection(Configuration config)
			throws ClassNotFoundException, SQLException,
			InstantiationException, IllegalAccessException {

		Connection resultConnection;

		// Registrazione del DriverManager
		Class driverClass = Class.forName(config
				.getString(CoreServerConfigurationXPaths.DB_CONNECTION_DRIVER));
		DriverManager.registerDriver((Driver) driverClass.newInstance());
		// Istanziazione della Connessione
		resultConnection = DriverManager
				.getConnection(
						config.getString(
								CoreServerConfigurationXPaths.
									DB_CONNECTION_DSN),
						config.getString(
								CoreServerConfigurationXPaths.
									DB_CONNECTION_USERNAME),
						config.getString(
								CoreServerConfigurationXPaths.
									DB_CONNECTION_PASSWORD));

		if (logger.isDebugEnabled()) {
			logger
					.debug("New DB Connection Created - "
							+ config.getString(
									CoreServerConfigurationXPaths.
										DB_CONNECTION_DSN));
		}

		return resultConnection;
	}

	/**
	 * Controlla se una {@link Configuration} e' gia' stata inserita nel Pool.
	 * 
	 * @param config 
	 * @return "true" se gia' presente, "false" altrimenti */
	public synchronized static boolean isConfigurationAlreadyUsed(
			Configuration config) {
		return connectionsPool.containsKey(config);
	}

	/**
	 * Aggiunge una {@link Connection} al Pool.
	 * La aggiunge SOLO se questa {@link Configuration} non risulta gia'
	 * utilizzata.
	 * 
	 * Inoltre, se questa e' la PRIMA {@link Connection} aggiunta, diventera'
	 * la {@link Connection}Êdi Default.
	 * 
	 * @param config {@link Configuration} da utilizzare per creare la
	 * 		{@link Connection}
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException */
	public synchronized static void addConnection(Configuration config)
			throws ClassNotFoundException, SQLException,
			InstantiationException, IllegalAccessException {

		// Verifica che questa Configurazione non sia gia' stata inserita
		// nel ConnectionPool
		if (!config.isEmpty() && !isConfigurationAlreadyUsed(config)) {
			// Creo la Connection e la inserisco nel Pool
			connectionsPool.put( config, makeConnection(config) );

			// Se e' la prima Configuration, la conserva come
			// "Configuration di default"
			if (firstConfiguration == null)
				firstConfiguration = config;
		}
	}

	/**
	 * Controlla che il {@link ConfigurationsConnectionsPool} sia stato
	 * gia' inizializzato.
	 * In pratica, controlla se sia gia' stata creata ALMENO una 
	 * {@link Connection}.
	 * 
	 * @return "true" in caso affermativo, "false" altrimenti */
	public synchronized static boolean isInitiated() {
		if (!connectionsPool.isEmpty())
			return true;
		else
			return false;
	}

	/**
	 * Ritorna la PRIMA {@link Connection} creata e aggiunta al Pool.
	 * 
	 * @return {@link Connection} di Default (la prima)
	 * @throws NotInitiatedException */
	public static Connection getConnection() 
		throws NotInitiatedException {

		if (!isInitiated()) {
			throw new NotInitiatedException();
		} else {
			return connectionsPool.get(firstConfiguration);
		}
	}

	/**
	 * Ritorna  una {@link Connection}, registrando la {@link Configuration}
	 * fornita, se non gia' presente.
	 * 
	 * @param config
	 * @return {@link Connection} costruita in base alla {@link Configuration}
	 * 		fornita (evitando di creare duplicazioni).
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NotInitiatedException */
	public static Connection getConnection(Configuration config)
			throws ClassNotFoundException, SQLException,
			InstantiationException, IllegalAccessException,
			NotInitiatedException {

		addConnection(config);
		return connectionsPool.get(config);
	}
}
