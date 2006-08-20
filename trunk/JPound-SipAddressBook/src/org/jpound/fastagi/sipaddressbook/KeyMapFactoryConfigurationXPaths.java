/**
 * File: KeyMapFactoryConfigurationXPaths.java
 * Created by: detro
 * Created at: Aug 12, 2006 */
package org.jpound.fastagi.sipaddressbook;

import org.asteriskjava.fastagi.AgiScript;

/**
 * XPaths per il file di Configurazione 
 * dell'{@link AgiScript} {@link AgiSipAddressBook}.
 * Servono a caricare le Keymap.
 * 
 * @author detro
 * @project JPound-AgiSipAddressBook
 * @package org.jpound.fastagi.sipaddressbook
 * @startdate Aug 12, 2006
 * @type KeyMapFactoryConfigurationXPaths */
public interface KeyMapFactoryConfigurationXPaths {
	public static final String KEYMAPS = "keymaps.keymap";
	public static final String KEYMAPS_NAMES = KEYMAPS + "[@name]";
	public static final String KEYMAPS_DESCRIPTIONS = KEYMAPS + ".description";
	public static final String KEY_NAMES = ".key[@name]";
	public static final String KEY_VALUES = ".key[@value]";
}
