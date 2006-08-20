/**
 * File: NotEnoughParametersException.java
 * Created by: detro
 * Created at: Agu 03, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Agu 03, 2006
 * @type NotEnoughParametersException */
public class NotEnoughParametersException extends Exception {

	public static final long serialVersionUID = 998732918; 
	
	/**
	 * Constructor */
	public NotEnoughParametersException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public NotEnoughParametersException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public NotEnoughParametersException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public NotEnoughParametersException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
