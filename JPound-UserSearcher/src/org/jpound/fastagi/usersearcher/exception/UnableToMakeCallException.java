/**
 * File: UnableToMakeCallException.java
 * Created by: detro
 * Created at: Aug 4, 2006 */
package org.jpound.fastagi.usersearcher.exception;

/**
 * @author detro
 * @project JPound-AgiUserSearcher
 * @package org.jpound.fastagi.usersearcher.exception
 * @startdate Aug 4, 2006
 * @type UnableToMakeCallException
 */
public class UnableToMakeCallException extends Exception {
	private static final long serialVersionUID = 87623817;
	/**
	 * 
	 */
	public UnableToMakeCallException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnableToMakeCallException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnableToMakeCallException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnableToMakeCallException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
