/**
 * File: FilenameSuffixFilter.java
 * Created by: detro
 * Created at: Jul 6, 2006 */
package org.jpound.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Classe che implementa {@link FilenameFilter} necessaria per 
 * filtrare i {@link File} in base al Suffisso e 
 * alla Directory in cui si trovano.
 * 
 * @author detro
 * @project JPound
 * @package org.jpound.util
 * @startdate Jul 6, 2006
 * @type FilenameSuffixFilter */
public class FilenameSuffixFilter implements FilenameFilter {
	
	/** Directory in cui cercare i File */
	private final File searchDirectory;
	
	/** Suffisso che deve avere un File per essere selezionato */
	private final String fileSuffix;
	
	/**
	 * Constructor
	 * @param searchDirectory Stringa 
	 * 		contenente il nome della Directory in cui Cercare
	 * 
	 * @param fileSuffix Stringa contenente 
	 * 		Suffisso che devono avere i File */
	public FilenameSuffixFilter(
			String searchDirectory,
			String fileSuffix ) {
		this(new File(searchDirectory), fileSuffix);
	}
	
	/**
	 * Constructor
	 * 
	 * @param searchDirectory Directory in cui Cercare
	 * @param fileSuffix Stringa contenente il Suffisso 
	 * 		che devono avere i File */
	public FilenameSuffixFilter(
			File searchDirectory,
			String fileSuffix
			) {
		this.searchDirectory = searchDirectory;
		this.fileSuffix = fileSuffix;
	}
	
	/**
	 * I File con suffisso uguale a quello selezionato 
	 * all'atto dell'istanziazione del filtro, sono validi.
	 * 
	 * @see FilenameFilter#accept(java.io.File, java.lang.String) */
	public boolean accept(File searchDir, String fileName) {
		if ( this.searchDirectory.equals(searchDir) ) {
			if ( fileName.endsWith(this.fileSuffix) ) {
				return true;
			}
			return false;
		}
		return false;
	}
}
