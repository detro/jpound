/**
 * File: UnableToUpdateVariableException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type UnableToUpdateVariableException */
public class UnableToUpdateVariableException extends Exception {

	public static final long serialVersionUID = 4; 
	
	/**
	 * Constructor */
	public UnableToUpdateVariableException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public UnableToUpdateVariableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public UnableToUpdateVariableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public UnableToUpdateVariableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
