/**
 * File: UnableToSelectMember.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jul 23, 2006
 * @type UnableToSelectMember
 */
public class UnableToSelectMember extends Exception {

	public static final long serialVersionUID = 76522; 
	
	/**
	 * 
	 */
	public UnableToSelectMember() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnableToSelectMember(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnableToSelectMember(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnableToSelectMember(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
