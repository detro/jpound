/**
 * File: NoMatchException.java
 * Created by: detro
 * Created at: Aug 03, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Aug 03, 2006
 * @type NoMatchException */
public class NoMatchException extends Exception {

	public static final long serialVersionUID = 21873689; 
	
	/**
	 * Constructor */
	public NoMatchException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public NoMatchException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public NoMatchException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public NoMatchException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
