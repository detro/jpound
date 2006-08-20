/**
 * File: UserSelectionErrorException.java
 * Created by: detro
 * Created at: Jul 20, 2006 */
package org.jpound.exception;

/**
 * @author detro
 * @project JPound-AgiAddressBook
 * @package org.jpound.fastagi.plugins.exception
 * @startdate Jul 20, 2006
 * @type UserSelectionErrorException
 */
public class UserSelectionErrorException extends Exception {
	
	public static final long serialVersionUID = 100;
	/**
	 * 
	 */
	public UserSelectionErrorException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UserSelectionErrorException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UserSelectionErrorException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserSelectionErrorException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
