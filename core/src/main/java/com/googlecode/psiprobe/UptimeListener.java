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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Simple listener that registers its startup time in the ServletContext object.
 * 
 * @author Vlad Ilyushchenko
 * @author Adriano Machado
 * @author Mark Lewis
 */
public class UptimeListener implements ServletContextListener {

  /** The Constant START_TIME_KEY. */
  public static final String START_TIME_KEY = "UPTIME_START";

  /* (non-Javadoc)
   * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
   */
  public void contextInitialized(ServletContextEvent sce) {
    sce.getServletContext().setAttribute(START_TIME_KEY, System.currentTimeMillis());
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
   */
  public void contextDestroyed(ServletContextEvent sce) {
    sce.getServletContext().removeAttribute(START_TIME_KEY);
  }
}
