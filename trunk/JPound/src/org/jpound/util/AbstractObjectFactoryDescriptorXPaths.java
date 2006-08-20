/**
 * File: AbstractObjectFactoryDescriptorXPaths.java
 * Created by: detro
 * Created at: Jul 3, 2006 */
package org.jpound.util;

/**
 * Questa Interfaccia raccoglie gli XPath verso le chiavi del file di 
 * configurazione XML based che usa JPound.
 * 
 * Serve per individuare gli elementi XML nel file Descriptor, utili al
 * caricamento della Classe.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound
 * @startdate Jul 3, 2006
 * @type AbstractObjectFactoryDescriptorXPaths */
public interface AbstractObjectFactoryDescriptorXPaths {
	public final static String className = "class[@classname]";
	public final static String classDescription = "class.description";
	public final static String classFilename = "class.filename";
}
