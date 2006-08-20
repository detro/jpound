/**
 * File: KeyMapFactory.java
 * Created by: detro
 * Created at: Jul 19, 2006 */
package org.jpound.fastagi.sipaddressbook;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.jpound.exception.NoMatchException;
import org.jpound.fastagi.AbstractJPoundAgiScript;

/**
 * Factory di Phone Keymaps: utile per separare il caricamento delle KeyMap
 * dal loro reale utilizzo.
 * La Factory legge le Keymap dal file di Configurazione XML che accompagna
 * {@link AbstractJPoundAgiScript} (che infatti deve essere fornito al 
 * costruttore della factory).
 * 
 * @author detro
 * @project JPound-AgiSipAddressBook
 * @package org.jpound.fastagi.sipaddressbook
 * @startdate Jul 19, 2006
 * @type KeyMapFactory
 * @class KeyMapFactory */
public class KeyMapFactory {

	private final Configuration agiScriptConfiguration;
	private final String[] availableKeymaps;
	
	/**
	 * Constructor
	 * @param agiScriptConfiguration Configurazione dell'AgiScript dove
	 * 		cercare le KeyMap */
	public KeyMapFactory(Configuration agiScriptConfiguration) {
		this.agiScriptConfiguration = agiScriptConfiguration;
		
		this.availableKeymaps = this.agiScriptConfiguration.getStringArray(
				KeyMapFactoryConfigurationXPaths.KEYMAPS_NAMES);
	}
	
	/**
	 * Costruisce la KeyMap in base al nome di KeyMap richiesto.
	 * Se non esiste, lancia un'eccezione.
	 * 
	 * @param keymapName
	 * @return Una Mappa di tipo {@link Map}<Character, String> che associa ogni
	 * carattere della tastiera del telefono al set di caratteri (una
	 * stringa) in base allo Standard che la tastiera segue
	 * (ad esempio, l'"itu-e1.161").
	 * 
	 * @throws NoMatchException Se la KeyMap richiesta non esiste. */
	public Map<Character, String> buildKeymap(String keymapName) 
		throws NoMatchException {
		// Inizializzo la Mappa dei Caratteri
		Map<Character, String> resultPhoneKeyMap = 
			new HashMap<Character, String>(10);
		
		// Cerco la KeyMap richiesta
		for ( int i = 0; i < this.availableKeymaps.length; i++ ) {
			// Se la KeyMap esiste
			if ( this.availableKeymaps[i].equalsIgnoreCase(keymapName) ) {
				// Inizializzo gli Iteratori che dovranno scorrere
				// sui vai "Key"
				Iterator keyNamesIter = this.agiScriptConfiguration.
					getList(
						KeyMapFactoryConfigurationXPaths.KEYMAPS+
						"("+i+")"+
						KeyMapFactoryConfigurationXPaths.KEY_NAMES).iterator();
				Iterator keyValuesIter = this.agiScriptConfiguration.
				getList(
					KeyMapFactoryConfigurationXPaths.KEYMAPS+
					"("+i+")"+
					KeyMapFactoryConfigurationXPaths.KEY_VALUES).iterator();
				
				// Inserisco gli elementi nella Mappa
				while( keyNamesIter.hasNext() && keyValuesIter.hasNext() ) {
					resultPhoneKeyMap.put(
							((String)keyNamesIter.next()).charAt(0), // es. '2'
							(String)keyValuesIter.next() ); // es. "abc2"
				}
				
				return resultPhoneKeyMap;
			}
		}
		
		throw new NoMatchException("Keymap Unavailable: "+keymapName);
	}
}
