/**
 * File: UnableToUnsetVariableException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type UnableToUnsetVariableException */
public class UnableToUnsetVariableException extends Exception {

	public static final long serialVersionUID = 3; 
	
	/**
	 * Constructor */
	public UnableToUnsetVariableException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public UnableToUnsetVariableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public UnableToUnsetVariableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public UnableToUnsetVariableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
