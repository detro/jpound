/**
 * File: UnableToInsertMemberException.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jul 23, 2006
 * @type UnableToInsertMemberException
 */
public class UnableToInsertMemberException extends Exception {

	public static final long serialVersionUID = 827168; 
	
	/**
	 * 
	 */
	public UnableToInsertMemberException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnableToInsertMemberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnableToInsertMemberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnableToInsertMemberException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
