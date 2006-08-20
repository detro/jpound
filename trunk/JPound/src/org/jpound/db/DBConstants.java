/**
 * File: DBConstants.java
 * Created by: detro
 * Created at: Aug 2, 2006 */
package org.jpound.db;

/**
 * Interfaccia che raccoglie i nomi delle tabelle e delle colonne usate in
 * JPound. Sono inoltre presenti gli <code>"_ALIAS"</code> utili nei casi di
 * query tra tabelle con campi omonimi.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound
 * @startdate Aug 2, 2006
 * @type DBConstants */
public interface DBConstants {
	public static final String CONTEXT_COLUMN = "context";

	public static final String ACCOUNT_TABLE = "account";

	public static final String SIP_TABLE = "sip";

	public static final String IAX_TABLE = "iax";

	public static final String VOICEMAIL_TABLE = "voicemail";

	public static final String ACCOUNT_ID_COLUMN = ACCOUNT_TABLE + ".id";

	public static final String ACCOUNT_ID_COLUMN_ALIAS = ACCOUNT_TABLE + "_id";

	public static final String ACCOUNT_USERNAME_COLUMN = ACCOUNT_TABLE
			+ ".username";

	public static final String ACCOUNT_USERNAME_COLUMN_ALIAS = ACCOUNT_TABLE
			+ "_username";

	public static final String ACCOUNT_FK_SIP_ID = ACCOUNT_TABLE
			+ ".sip_account_id";

	public static final String ACCOUNT_FK_SIP_ID_ALIAS = ACCOUNT_TABLE
			+ "_sip_account_id";

	public static final String ACCOUNT_FK_IAX_ID = ACCOUNT_TABLE
			+ ".iax_account_id";

	public static final String ACCOUNT_FK_IAX_ID_ALIAS = ACCOUNT_TABLE
			+ "_iax_account_id";

	public static final String ACCOUNT_FK_VOICEMAIL_ID = ACCOUNT_TABLE
			+ ".voicemail_account_id";

	public static final String ACCOUNT_FK_VOICEMAIL_ID_ALIAS = ACCOUNT_TABLE
			+ "_voicemail_account_id";

	public static final String SIP_NAME_COLUMN = SIP_TABLE + ".name";

	public static final String SIP_NAME_COLUMN_ALIAS = SIP_TABLE + "_name";

	public static final String SIP_ID_COLUMN = SIP_TABLE + ".id";

	public static final String SIP_ID_COLUMNS_ALIAS = SIP_TABLE + "_id";

	public static final String IAX_NAME_COLUMN = IAX_TABLE + ".name";

	public static final String IAX_NAME_COLUMN_ALIAS = IAX_TABLE + "_name";

	public static final String IAX_ID_COLUMN = IAX_TABLE + ".id";

	public static final String IAX_ID_COLUMN_ALIAS = IAX_TABLE + "_id";

	public static final String VOICEMAIL_MAILBOX_COLUMN = VOICEMAIL_TABLE
			+ ".mailbox";

	public static final String VOICEMAIL_MAILBOX_COLUMN_ALIAS = VOICEMAIL_TABLE
			+ "_mailbox";

	public static final String VOICEMAIL_CONTEXT_COLUMN = VOICEMAIL_TABLE + "."
			+ CONTEXT_COLUMN;

	public static final String VOICEMAIL_CONTEXT_COLUMN_ALIAS = VOICEMAIL_TABLE
			+ "_" + CONTEXT_COLUMN;

	public static final String VOICEMAIL_ID_COLUMN = VOICEMAIL_TABLE + ".id";

	public static final String VOICEMAIL_ID_COLUMN_ALIAS = VOICEMAIL_TABLE
			+ "_id";
}
