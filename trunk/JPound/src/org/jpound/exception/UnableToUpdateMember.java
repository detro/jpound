/**
 * File: UnableToUpdateMember.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jul 23, 2006
 * @type UnableToUpdateMember
 */
public class UnableToUpdateMember extends Exception {

	public static final long serialVersionUID = 9879821; 
	
	/**
	 * 
	 */
	public UnableToUpdateMember() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnableToUpdateMember(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnableToUpdateMember(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnableToUpdateMember(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
