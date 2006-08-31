/**
 * File: NullDBConnectionException.java
 * Created by: detro
 * Created at: Aug 14, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Aug 14, 2006
 * @type NullDBConnectionException
 */
public class NullDBConnectionException extends Exception {
	public static final long serialVersionUID = 734093482;
	/**
	 * 
	 */
	public NullDBConnectionException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NullDBConnectionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NullDBConnectionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NullDBConnectionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
