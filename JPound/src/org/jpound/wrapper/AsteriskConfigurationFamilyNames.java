/**
 * File: AsteriskConfigurationFamilyNames.java
 * Created by: detro
 * Created at: Aug 17, 2006 */
package org.jpound.wrapper;

/**
 * Questa Interfaccia funge da "contenitore di costanti" per i nomi
 * delle Families di Asterisk.
 * Questo permette un certo grado di flessibilita' nel caso in cui questi
 * nomi cambino e semplifica lo sviluppo, aggregandoli.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.wrapper
 * @startdate Aug 17, 2006
 * @type AsteriskConfigurationFamilyNames */
public interface AsteriskConfigurationFamilyNames {
	public static final String VOICEMAILS = "voicemail";
	public static final String SIPS = "sip";
	public static final String IAXS = "iax";
	public static final String EXTENSIONS = "extension";
	public static final String QUEUES = "queue";
	public static final String QUEUES_MEMBERS = "queue_member";
	public static final String CDRS = "cdr";
}
