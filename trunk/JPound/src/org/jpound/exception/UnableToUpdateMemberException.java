/**
 * File: UnableToUpdateMemberException.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jul 23, 2006
 * @type UnableToUpdateMemberException
 */
public class UnableToUpdateMemberException extends Exception {

	public static final long serialVersionUID = 9879821; 
	
	/**
	 * 
	 */
	public UnableToUpdateMemberException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnableToUpdateMemberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnableToUpdateMemberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnableToUpdateMemberException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
