/**
 * File: NotADirectoryException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type NotADirectoryException */
public class NotADirectoryException extends Exception {

	public static final long serialVersionUID = 8; 
	
	/**
	 * Constructor */
	public NotADirectoryException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public NotADirectoryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public NotADirectoryException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public NotADirectoryException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
