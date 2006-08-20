/**
 * File: NullDBConnection.java
 * Created by: detro
 * Created at: Aug 14, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Aug 14, 2006
 * @type NullDBConnection
 */
public class NullDBConnection extends Exception {
	public static final long serialVersionUID = 734093482;
	/**
	 * 
	 */
	public NullDBConnection() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NullDBConnection(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NullDBConnection(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NullDBConnection(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
