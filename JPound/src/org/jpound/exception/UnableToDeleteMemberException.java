/**
 * File: UnableToDeleteMemberException.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jul 23, 2006
 * @type UnableToDeleteMemberException
 */
public class UnableToDeleteMemberException extends Exception {

	public static final long serialVersionUID = 76521; 
	
	/**
	 * 
	 */
	public UnableToDeleteMemberException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnableToDeleteMemberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnableToDeleteMemberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnableToDeleteMemberException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
