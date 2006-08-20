/**
 * File: UnableToInsertMember.java
 * Created by: detro
 * Created at: Jul 23, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.exception
 * @startdate Jul 23, 2006
 * @type UnableToInsertMember
 */
public class UnableToInsertMember extends Exception {

	public static final long serialVersionUID = 827168; 
	
	/**
	 * 
	 */
	public UnableToInsertMember() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UnableToInsertMember(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UnableToInsertMember(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnableToInsertMember(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
