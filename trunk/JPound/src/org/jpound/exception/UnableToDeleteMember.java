/**
 * File: UnableToDeleteMember.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jul 23, 2006
 * @type UnableToDeleteMember
 */
public class UnableToDeleteMember extends Exception {

	public static final long serialVersionUID = 76521; 
	
	/**
	 * 
	 */
	public UnableToDeleteMember() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnableToDeleteMember(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnableToDeleteMember(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnableToDeleteMember(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
