/**
 * File: UnableToWrapConfigurationFileException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type UnableToWrapConfigurationFileException */
public class UnableToWrapConfigurationFileException extends Exception {

	public static final long serialVersionUID = 5; 
	
	/**
	 * Constructor */
	public UnableToWrapConfigurationFileException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public UnableToWrapConfigurationFileException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public UnableToWrapConfigurationFileException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public UnableToWrapConfigurationFileException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
