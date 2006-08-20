/**
 * File: NotInitiatedException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type NotInitiatedException */
public class NotInitiatedException extends Exception {

	public static final long serialVersionUID = 8; 
	
	/**
	 * Constructor */
	public NotInitiatedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public NotInitiatedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public NotInitiatedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public NotInitiatedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
