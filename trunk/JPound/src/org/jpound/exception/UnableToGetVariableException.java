/**
 * File: UnableToGetVariableException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type UnableToGetVariableException */
public class UnableToGetVariableException extends Exception {

	public static final long serialVersionUID = 1; 
	
	/**
	 * Constructor */
	public UnableToGetVariableException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public UnableToGetVariableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public UnableToGetVariableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public UnableToGetVariableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
