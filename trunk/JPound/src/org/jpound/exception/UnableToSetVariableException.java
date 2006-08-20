/**
 * File: UnableToSetVariableException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type UnableToSetVariableException */
public class UnableToSetVariableException extends Exception {

	public static final long serialVersionUID = 2; 
	
	/**
	 * Constructor */
	public UnableToSetVariableException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public UnableToSetVariableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public UnableToSetVariableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public UnableToSetVariableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
