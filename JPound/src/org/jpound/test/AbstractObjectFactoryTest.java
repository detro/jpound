/**
 * File: AbstractObjectFactoryTest.java
 * Created by: detro
 * Created at: Jul 11, 2006 */
package org.jpound.test;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jpound.exception.NotADirectoryException;
import org.jpound.util.AbstractObjectFactory;

/**
 * @author detro
 * @project JPound
 * @package org.jpound.util
 * @startdate Jul 11, 2006
 * @type AbstractObjectFactoryTest */
public class AbstractObjectFactoryTest extends AbstractObjectFactory {

	private static Logger logger =
		Logger.getLogger(AbstractObjectFactoryTest.class);
	
	/**
	 * Constructor
	 * @param classesDirectory
	 * @throws NotADirectoryException
	 * @throws IOException */
	public AbstractObjectFactoryTest(File classesDirectory)
			throws NotADirectoryException, IOException {
		super(classesDirectory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor
	 * @param classesDirectory
	 * @throws NotADirectoryException
	 * @throws IOException */
	public AbstractObjectFactoryTest(String classesDirectory)
			throws NotADirectoryException, IOException {
		super(classesDirectory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args */
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		
		AbstractObjectFactoryTest test = null;
		try {
			test = new AbstractObjectFactoryTest("plugins/agiscripts/");
		} catch (NotADirectoryException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
		logger.debug(test.getClassNames()[0]);
	}

}
