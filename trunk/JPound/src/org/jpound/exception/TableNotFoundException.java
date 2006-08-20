/**
 * File: TableNotFoundException.java
 * Created by: detro
 * Created at: Jun 24, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jun 24, 2006
 * @type TableNotFoundException */
public class TableNotFoundException extends Exception {

	public static final long serialVersionUID = 8; 
	
	/**
	 * Constructor */
	public TableNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message */
	public TableNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param message
	 * @param cause */
	public TableNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param cause */
	public TableNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
