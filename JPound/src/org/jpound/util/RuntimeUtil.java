/**
 * File: RuntimeUtil.java
 * Created by: detro
 * Created at: Jul 6, 2006 */
package org.jpound.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Questa classe di Utiliti permette di recuperare alcune informazioni
 * dall'Ambiente Runtime del processo Java in esecuzione.
 * 
 * <b>N.B.</b> Il metodo {@link RuntimeUtil#getPID(String)} si basa
 * sul presupposto che sia disponibile una Shell (consigliata Bash).
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.util
 * @startdate Jul 6, 2006
 * @type RuntimeUtil */
public class RuntimeUtil {
	
	/**
	 * Restituisce il PID del Processo corrente.
	 * Sia assume che la Shell sia nella posizione "<code>/bin/bash</code>",
	 * poiche' abbastanza comune nei sistemi Unix.
	 * 
	 * @see RuntimeUtil#getPID(String) */
	public static long getPID() throws IOException {
		return RuntimeUtil.getPID( "/bin/bash" );
	}
	
	/**
	 * Restituisce il PID del Processo corrente.
	 * 
	 * Si basa sul presupposto che esista una Shell nel sistema, usabile
	 * per poter eseguire i seguenti comandi:
	 * <pre>
	 * # /bin/bash -c
	 * # echo $PPID
	 * </pre>
	 * Leggendo quindi l'output del comando "<code>echo</code>", e' possibile
	 * conoscere il PID del processo.
	 * 
	 * @param pathToShell Path alla Shell di Sistema
	 * @return "-1" in caso di errore, il PID altrimenti. 
	 * @throws IOException */
	public static long getPID ( String pathToShell ) throws IOException {
		String[] cmd = { pathToShell, "-c", "echo $PPID" };
		long PID = -1;
		
		Process process = Runtime.getRuntime().exec(cmd);
		InputStreamReader inputStreamReader = 
			new InputStreamReader(process.getInputStream());
		BufferedReader bufferedReader = 
			new BufferedReader(inputStreamReader);

		PID = Long.parseLong( bufferedReader.readLine() );
		
		return PID;
	}
	
	/**
	 * Restituisce la Current Work Directory del Processo Corrente.
	 * @return Restituisce la Current Work Directory del Processo Corrente. */
	public static String getEnvCurrentWorkDirectory() {
		return System.getProperty("user.dir");
	}
	
	/**
	 * Restituisce il carattere utilizzato nel sistema corrente come
	 * separatore tra le Directory nei FilePath (es. "/" nei sistemi Unix).
	 * @return Restituisce il carattere utilizzato nel sistema corrente come
	 * 		separatore tra le Directory nei 
	 * 		FilePath (es. "/" nei sistemi Unix). */
	public static String getEnvFilepathDirectorySeparator() {
		return System.getProperty("file.separator");
	}
	
	/**
	 * Restituisce la variabile ClassPath all'atto dell'esecuzione 
	 * del Processo Corrente.
	 * @return Restituisce la variabile ClassPath all'atto dell'esecuzione 
	 * 		del Processo Corrente. */
	public static String getEnvClassPath() {
		return System.getProperty("java.class.path");
	}
}
