/**
 * File: UnableToGetClassInstanceException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type UnableToGetClassInstanceException */
public class UnableToGetClassInstanceException extends Exception {

	public static final long serialVersionUID = 12; 
	
	/**
	 * Constructor */
	public UnableToGetClassInstanceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public UnableToGetClassInstanceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public UnableToGetClassInstanceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public UnableToGetClassInstanceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
