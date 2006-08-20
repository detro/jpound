/**
 * File: Digits.java
 * Created by: detro
 * Created at: Jul 31, 2006 */
package org.jpound.util;

import java.util.HashSet;

/**
 * Classe di Utility che fornisce delle operazioni
 * di base sui Digits, i numeri/caratteri digitabili sulla tastiera
 * di un telefono.
 * <br>
 * I Digit sono:
 * <ul>
 *  <li>'0'</li>
 *  <li>'1'</li>
 *  <li>'2'</li>
 *  <li>'3'</li>
 *  <li>'4'</li>
 *  <li>'5'</li>
 *  <li>'6'</li>
 *  <li>'7'</li>
 *  <li>'8'</li>
 *  <li>'9'</li>
 *  <li>'*'</li>
 *  <li>'#'</li>
 * </ul>
 * <br>
 * I Numerical Digit sono:
 * <ul>
 *  <li>'0'</li>
 *  <li>'1'</li>
 *  <li>'2'</li>
 *  <li>'3'</li>
 *  <li>'4'</li>
 *  <li>'5'</li>
 *  <li>'6'</li>
 *  <li>'7'</li>
 *  <li>'8'</li>
 *  <li>'9'</li>
 * </ul>
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.util
 * @startdate Jul 31, 2006
 * @type Digits
 * @class Digits */
public class Digits {
	
	private static HashSet<Character> digits = 
		new HashSet<Character>(12);
	
	private static HashSet<Character> numericalDigits = 
		new HashSet<Character>(10);
	
	private static boolean initialized = false;
	
	synchronized private static void init() {
		if ( !isInitiated() ) {
			digits.add('0');
			digits.add('1');
			digits.add('2');
			digits.add('3');
			digits.add('4');
			digits.add('5');
			digits.add('6');
			digits.add('7');
			digits.add('8');
			digits.add('9');
			digits.add('*');
			digits.add('#');
			
			numericalDigits.add('0');
			numericalDigits.add('1');
			numericalDigits.add('2');
			numericalDigits.add('3');
			numericalDigits.add('4');
			numericalDigits.add('5');
			numericalDigits.add('6');
			numericalDigits.add('7');
			numericalDigits.add('8');
			numericalDigits.add('9');
			
			initialized = true;
		}
	}

	synchronized private static boolean isInitiated() {
		return initialized;
	}
	
	/**
	 * Controlla se il Carattere fornito e' un Digit.
	 *
	 * @param digit
	 * @return "true" se e' un digit, "false" altrimenti. */
	public static boolean isDigit(char digit) {
		init();
		return digits.contains(digit);
	}
	
	/**
	 * Controlla se il Carattere fornito e' un Numerical Digit.
	 *
	 * @param digit
	 * @return "true" se e' un numerical digit, "false" altrimenti. */
	public static boolean isNumericalDigit(char digit) {
		init();
		return numericalDigits.contains(digit);
	}
}
