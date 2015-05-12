package com.googlecode.psiprobe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Prevents a classloader leak as suggested by <a href="https://cdivilly.wordpress.com/2012/04/23/permgen-memory-leak/">Colm Divilly</a>
 * 
 * @author diogosantana
 *
 */
public class AwtAppContextClassloaderListener implements ServletContextListener {

	
	public void contextInitialized(ServletContextEvent sce) {
		try {
			final ClassLoader active = Thread.currentThread().getContextClassLoader();
			try {
				// Find the root classloader
				ClassLoader root = active;
				while (root.getParent() != null) {
					root = root.getParent();
				}
				// Temporarily make the root class loader the active class loader
				Thread.currentThread().setContextClassLoader(root);
				/*
				 *  Forces the sun.awt.AppContext singleton to be created and initialized.
				 *  Call ImageIO.getCacheDirectory() to avoid direct call to Oracle JVM internal
				 *  class sun.awt.AppContext.
				 *  Same solution as in org.apache.catalina.core.JreMemoryLeakPreventionListener
				 *  which is optional on Tomcat 1.7.0_02 or greater.
				 */
				ImageIO.getCacheDirectory();
			} finally {
				// restore the class loader
				Thread.currentThread().setContextClassLoader(active);
			}
		} catch (Throwable t) {
			Log logger = LogFactory.getLog(AwtAppContextClassloaderListener.class.getName());
			logger.error("Failed to address PermGen leak.", t);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
