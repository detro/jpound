/**
 * File: UnableToSelectMemberException.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jul 23, 2006
 * @type UnableToSelectMemberException
 */
public class UnableToSelectMemberException extends Exception {

	public static final long serialVersionUID = 76522; 
	
	/**
	 * 
	 */
	public UnableToSelectMemberException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnableToSelectMemberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnableToSelectMemberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnableToSelectMemberException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
