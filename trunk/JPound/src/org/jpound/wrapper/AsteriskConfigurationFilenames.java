/**
 * File: AsteriskConfigurationFilenames.java
 * Created by: detro
 * Created at: Jun 25, 2006 */
package org.jpound.wrapper;

/**
 * Questa Interfaccia funge da "contenitore di costanti" per i nomi
 * dei file di configurazione di Asterisk.
 * Questo permette un certo grado di flessibilita' nel caso in cui questi
 * file cambino di nome e semplifica lo sviluppo, aggregandoli.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.wrapper
 * @startdate Jun 25, 2006
 * @type AsteriskConfigurationFilenames */
public interface AsteriskConfigurationFilenames {
	public final static String VOICEMAIL_CONF = "voicemail.conf";
	public final static String SIP_CONF = "sip.conf";
	public final static String IAX_CONF = "iax.conf";
	public final static String CDR_CONF = "cdr.conf";
	public final static String QUEUES_CONF = "queues.conf";
	public final static String EXTENSIONS_CONF = "extensions.conf";
	public final static String MANAGER_CONF = "manager.conf";
	public final static String RTP_CONF = "rtp.conf";	
}
