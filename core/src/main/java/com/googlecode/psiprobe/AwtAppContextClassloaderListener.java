/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Prevents a classloader leak as suggested by
 * <a href="https://cdivilly.wordpress.com/2012/04/23/permgen-memory-leak/">Colm Divilly</a>
 * 
 * @author diogosantana
 *
 */
public class AwtAppContextClassloaderListener implements ServletContextListener {

  /**
   * Forces the {@code sun.awt.AppContext} singleton to be created and initialized when the context
   * is initialized.
   * 
   * @param sce the event containing the context being initialized
   */
  @Override
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
         * Forces the sun.awt.AppContext singleton to be created and initialized. Call
         * ImageIO.getCacheDirectory() to avoid direct call to Oracle JVM internal class
         * sun.awt.AppContext. Same solution as in
         * org.apache.catalina.core.JreMemoryLeakPreventionListener which is optional on Tomcat
         * 1.7.0_02 or greater.
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

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}

}
