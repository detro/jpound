/**
 * File: TooManyMatchException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type TooManyMatchException */
public class TooManyMatchException extends Exception {

	public static final long serialVersionUID = 7; 
	
	/**
	 * Constructor */
	public TooManyMatchException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public TooManyMatchException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public TooManyMatchException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public TooManyMatchException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
