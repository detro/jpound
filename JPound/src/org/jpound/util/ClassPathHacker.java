/**
 * File: ClassPathHacker.java
 * Created by: detro
 * Created at: Jul 6, 2006 */
package org.jpound.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Questa Classe permette di aggiungere File al ClassPath a RunTime.
 * <br>
 * I File possono essere sia delle Directory, sia dei File "<code>.jar</code>".
 * Molto utile per il caricamento di Classi Plug-IN.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.util
 * @startdate Jul 6, 2006
 * @type ClassPathHacker
 * @class ClassPathHacker */
public class ClassPathHacker {
	/** Parametri per il Metodo "addURL" della Classe "URLClassLoader" */
    private static final Class[] addUrlParameters = new Class[] {URL.class};

    /** Aggiunge un File al ClassPath
     * 
     * @param szFilePath FilePath del File da aggiungere al ClassPath
     * @throws IOException */
    public static void addFile(String filePath) throws IOException {
    		ClassPathHacker.addFile( new File(filePath) );
    }

    /** Aggiunge un File al ClassPath
     * 
     * @param oFile File da aggiungere al ClassPath
     * @throws IOException */
    public static void addFile(File file) throws IOException {
    		ClassPathHacker.addURL( file.toURL() );
    }

    /** Aggiunge un File al ClassPath
     * 
     * @param oFileURL URL del File da aggiungere al ClassPath
     * @throws IOException */
    public static void addURL(URL fileURL) throws IOException {
    		// Recupera il ClassLoader di Default (URLClassLoader)
        URLClassLoader sysLoader = 
        		(URLClassLoader)ClassLoader.getSystemClassLoader();
        
        // Oggetto-Classe di tipo URLClassLoader
        Class sysClass = URLClassLoader.class;
        
        try {
        		// Recupera il metodo "addURL" dall'istanza di Classe URLClassLoader
			Method method = sysClass.getDeclaredMethod(
					"addURL", 
					ClassPathHacker.addUrlParameters);
			// Setta l'accessibilita' (visibilita') del Metodo
			method.setAccessible(true);
			// Invoca il Metodo
			method.invoke( sysLoader, new Object[] {fileURL} );
        } catch (Throwable t) {
            throw new IOException(
            		"Error, could not add URL "+ 
            		fileURL.toString() +
            		" to system classloader: "+
            		t.getMessage() );
        }
    }
}