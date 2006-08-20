/**
 * File: AgiSipAddressBook.java
 * Created by: detro
 * Created at: Jul 17, 2006 */
package org.jpound.fastagi.sipaddressbook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;
import org.jpound.db.DBConstants;
import org.jpound.exception.NoMatchException;
import org.jpound.exception.NotInitiatedException;
import org.jpound.exception.UserSelectionErrorException;
import org.jpound.fastagi.AbstractConnectedJPoundAgiScript;
import org.jpound.fastagi.AbstractJPoundAgiScript;
import org.jpound.util.Digits;

/**
 * Questo {@link AbstractJPoundAgiScript} ha il compito 
 * di realizzare una "Rubrica Mnemonica" per gli utenti di Asterisk.
 * E' un esempio abbastanza evoluto di {@link AgiScript} che si puo' realizzare
 * con JPound.
 * 
 * In pratica, l'Utente viene invitato a digirare sulla tastiera
 * l'Username dell'Utente che si vuole chiamare: per farlo usa la Mappatura
 * alfabetica presente su quasi tutti i telefoni.
 * 
 * Esempio di mappatura: 
 * "ivan" (utente) -> 
 * 		"4826" (su phone keymap ITU) -> 
 * 		"[gh<b>i</b>4][tu<b>v</b>8][<b>a</b>bc2][m<b>n</b>o6]".
 * 
 * Poiche' piu' Utenti possono corrispondere alla stringa digitata,
 * tutte le possibilita' vengono elencate, permettendo all'Utente
 * di scegliere chi si vuole chiamare tra i possibili candidati.
 * 
 * E' importante notare che la mappatura e' assolutamente dinamica: viene
 * caricata a run-time in base al file di configurazione dell'AgiScript.
 * 
 * In questa implementazione, si prendono in considerazione sono gli Account
 * registrati su Sip.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.fastagi
 * @startdate Jul 17, 2006
 * @type AgiSipAddressBook */
public class AgiSipAddressBook extends AbstractConnectedJPoundAgiScript {
	
	/**
	 * Phone Key Pad Map.
	 * Mappa che associa ad ogni cifra una stringa contenente
	 * i relativi caratteri secondo la "Phone Key Pad" scelta. */
	private final Map<Character, String> phoneKeyMap;
	
	/**
	 * Factory che Costruisce le Phone Key Map. */
	private final KeyMapFactory keyMapFactory;
	
	/** Nome della KeyMap selezionata nel file di configurazione */
	private final String selectedPhoneKeyMap;
	
	private static Logger logger =
		Logger.getLogger( AgiSipAddressBook.class );
	
	/**
	 * Constructor.
	 * 
	 * @param jpoundConfiguration
	 * @param agiScriptConfiguration
	 * @throws NotInitiatedException */
	public AgiSipAddressBook(
			Configuration jpoundConfiguration, 
			Configuration agiScriptConfiguration) 
		throws NotInitiatedException {		
		super(jpoundConfiguration, agiScriptConfiguration);
		
		// Leggo dal File di Configurazione la KeyMap scelta
		this.selectedPhoneKeyMap = this.getAgiScriptConfiguration().getString(
				AgiSipAddressBookConfigurationXPaths.SELECTED_KEYMAP);
		
		// Istanzio la KeyMapFactory
		this.keyMapFactory = new KeyMapFactory(this.getAgiScriptConfiguration());
		
		// Carico la KeyMap scelta
		try {
			this.phoneKeyMap = 
				this.keyMapFactory.buildKeymap(this.selectedPhoneKeyMap);
		} catch (NoMatchException e) {
			logger.error(e);
			throw new NotInitiatedException(e);
		}
	}

	/**
	 * @see AbstractJPoundAgiScript#service(AgiRequest, AgiChannel) */
	public void service(AgiRequest request, AgiChannel channel) throws AgiException {
		String usernameDigits = null;		
		StringBuffer queryBuffer = null;
		String usernameDigitsRegexp = null;
		ResultSet foundedUserAccounts = null;
		int selectedAccount;
		
		// Apro il Canale
		this.answer();
		
		// Leggo i numeri digitati
		try {
			usernameDigits = this.getUserToCall(20);
		} catch (UserSelectionErrorException e) {
			logger.debug(e);
			this.hangup();
			return;
		}
		
		// Traduco i numeri digitati in una Regular Expression che permetta
		// di individuare i relativi utenti.
		usernameDigitsRegexp = this.digitsToPOSIXRegexp(usernameDigits);
		
		// Costruisco la Query per cercare gli Account corrispondenti
		queryBuffer = new StringBuffer(
				"SELECT " +
				DBConstants.ACCOUNT_USERNAME_COLUMN 
					+ " AS "+ DBConstants.ACCOUNT_USERNAME_COLUMN_ALIAS +", " +
				DBConstants.SIP_NAME_COLUMN 
					+ " AS "+ DBConstants.SIP_NAME_COLUMN_ALIAS +
				" FROM "+ DBConstants.ACCOUNT_TABLE +
				" JOIN "+ DBConstants.SIP_TABLE 
					+" ON "+ DBConstants.ACCOUNT_FK_SIP_ID 
						+" = "+ DBConstants.SIP_ID_COLUMN +
				" WHERE lower("+ DBConstants.ACCOUNT_USERNAME_COLUMN +") " +
				" SIMILAR TO '"+ usernameDigitsRegexp + "';");
		
		// Eseguo la Query per cercare gli Account
		try {
			foundedUserAccounts = this.executeSelectQuery( queryBuffer.toString() );
		} catch (SQLException e) {
			logger.error(e);
		}
		
		try {
			// Faccio selezionare all'Utete l'Account che vuole chiamare
			selectedAccount = this.getSelectedAccount(foundedUserAccounts, 30);
			// Posizione il cursore del ResultSet sull'Account Scelto
			foundedUserAccounts.absolute( selectedAccount );
			
			// Inoltro la chiamata
			this.execDial(
					"SIP", 
					foundedUserAccounts.getString(
							DBConstants.SIP_NAME_COLUMN_ALIAS), 
					20, 
					"rT");
		} catch (SQLException e) {
			logger.error(e);
		} catch (UserSelectionErrorException e) {
			logger.debug(e);
			this.hangup();
			return;
		}
	}
	
	/**
	 * Recupera l'Account che l'Utente decide di chiamare.
	 * 
	 * @param accounts ResultSet degli Account trovati
	 * @param timeoutSeconds Timeout in Secondi
	 * @param retry Numero di Tentativi. Usare valori positivi.
	 * @return Intero relativo all'utente scelto nel ResultSet
	 * @throws SQLException
	 * @throws AgiException */
	private int getSelectedAccount ( 
			ResultSet accounts, 
			int timeoutSeconds,
			int retry ) 
		throws SQLException, AgiException, UserSelectionErrorException {
		char selectedAccountDigit = 0x0;
		int counter = 0;
		
		// Rinnova la richiesta fino all'esaurimento dei Tentativi
		// o fino a quando l'utente non ha inserito un valore valido
		do {
			retry--;
			// Resetto il cursore del ResultSet per ripartire dall'inizio
			accounts.beforeFirst();
			
			while ( accounts.next() ) {
				counter++;
				
				this.streamFile("press");
				this.sayNumber( String.valueOf( accounts.getRow() ) );
				this.streamFile("for");
				this.sayAlpha( accounts.getString(DBConstants.ACCOUNT_USERNAME_COLUMN_ALIAS) );
				this.streamFile("at");
				this.streamFile("extension");
				this.sayDigits( accounts.getString(DBConstants.SIP_NAME_COLUMN_ALIAS) );
				
				if ( logger.isDebugEnabled() ) {
					logger.debug(
						"FOUNDED ACCOUNT =>" +
						" "+DBConstants.ACCOUNT_USERNAME_COLUMN_ALIAS+": " 
						+ accounts.getString(DBConstants.ACCOUNT_USERNAME_COLUMN_ALIAS) +
						" - "+DBConstants.SIP_NAME_COLUMN_ALIAS+": " 
						+ accounts.getString(DBConstants.SIP_NAME_COLUMN_ALIAS) );
				}
			}
			
			// Verifica che sia stato trovato almeno un Utente: e' un workaround
			// per una mancanza abbastanza inspiegabile nella Classe
			// Connection di java.sql: un contatore delle righe.
			if ( counter == 0 )
				throw new UserSelectionErrorException("No Users Match!");
			
			// Legge un Digit dall'Utente
			selectedAccountDigit = this.waitForDigit(timeoutSeconds * 1000);
			
			if ( logger.isDebugEnabled() ) {
				logger.debug("Selected Account Digit: " +selectedAccountDigit);
			}
			
			// Se il tasto digitato non e' corretto, viene ignorato
			if ( !Digits.isNumericalDigit(selectedAccountDigit) ) {
				if ( logger.isDebugEnabled() ) {
					logger.debug("Selected Account Digit is NOT Numerical!");
				}
				selectedAccountDigit = 0x0;
			}
		} while( retry > 0 && selectedAccountDigit == 0x0 );
		
		// Selezione Errata o Timeout raggiunto, lancio l'eccezione
		if ( selectedAccountDigit == 0x0 )
			throw new UserSelectionErrorException();
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("Selected Account to Call: " + selectedAccountDigit);
		}
		
		return Integer.parseInt( Character.toString(selectedAccountDigit) );
	}

	/**
	 * Recupera l'Account che l'Utente decide di chiamare.
	 * @see AgiSipAddressBook#getSelectedAccount(ResultSet, int, int)
	 * 
	 * @param accounts ResultSet degli Account trovati
	 * @param timeoutSeconds Timeout in Secondi
	 * @param retry Numero di Tentativi. Usare valori positivi.
	 * @return Intero relativo all'utente scelto nel ResultSet
	 * @throws SQLException
	 * @throws AgiException */
	private int getSelectedAccount ( 
			ResultSet accounts, 
			int timeoutSeconds ) 
		throws SQLException, AgiException, UserSelectionErrorException {
		
		return this.getSelectedAccount(accounts, timeoutSeconds, 3);
	}
	
	/**
	 * Richiede all'Utente di digitare l'Username della persona
	 * che si vuole chiamare.
	 * Attende fino alla fine del timout.
	 * 
	 * @param timeoutSeconds Timeout in Secondi
	 * @param retry Numero di volte che deve chiedere all'utente 
	 * 		in caso di errore di digitazione o timeout. Usare valori positivi.
	 * 
	 * @return Sequenza dei Tasti digitati (che deve essere tradotta in base
	 * 		alla Phone Key Pad schelta)
	 * 
	 * @throws UserSelectionErrorException
	 * @throws AgiException */
	private String getUserToCall(int timeoutSeconds, int retry) 
		throws UserSelectionErrorException, AgiException {
		
		String selectedUser = null;
		
		// Rinnova la richiesta fino all'esaurimento dei Tentativi previsti
		// o fino a che l'utente non ha inserito un valore valido.
		do {
			retry--;
			
			this.streamFile("please-enter-the");
			this.streamFile("user");
			this.streamFile("to-dial-by-name");
			// ... timeout in millisecondi
			selectedUser = this.getData("then-press-pound", timeoutSeconds * 1000);
		} while ( retry > 0 && (selectedUser == null || selectedUser.equals("(timeout)")) );
		
		// Selezione Errata o Timeout raggiunto, lancio l'eccezione
		if ( selectedUser == null || selectedUser.equals("(timeout)") )
			throw new UserSelectionErrorException();
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("User-to-Call's Digits: " + selectedUser);
		}
		
		return selectedUser;
	}
	
	/**
	 * @see AgiSipAddressBook#getUserToCall(int, int)
	 * @param timeoutSeconds
	 * @return Sequenza dei Tasti digitati (che deve essere tradotta in base
	 * 		alla Phone Key Pad schelta)
	 * 
	 * @throws UserSelectionErrorException
	 * @throws AgiException */
	private String getUserToCall(int timeoutSeconds) 
		throws UserSelectionErrorException, AgiException {
		return this.getUserToCall(timeoutSeconds, 3);
	}

	/**
	 * Traduce una sequenza dei Digits in una Regular Expression, usando
	 * la phoneKeyMap scelta (vedere il Costruttore).
	 * 
	 * @param usernameDigits
	 * @return Una regexp cosi' creata (nel caso di ITU Phone Keymap)
	 * "ivan" -> "4826" -> "[gh<b>i</b>][tu<b>v</b>][<b>a</b>bc][m<b>n</b>o]".*/
	private String digitsToPOSIXRegexp (String usernameDigits) {
		StringBuffer buffer = new StringBuffer();
		
		for ( int i = 0; i < usernameDigits.length(); i++ ) {
			buffer.append('[' +phoneKeyMap.get(usernameDigits.charAt(i))+ ']');
		}
		
		return buffer.toString();
	}
}
