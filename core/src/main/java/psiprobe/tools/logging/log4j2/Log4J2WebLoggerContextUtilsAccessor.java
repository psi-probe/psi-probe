/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.tools.logging.log4j2;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.reflect.MethodUtils;

import psiprobe.tools.logging.DefaultAccessor;

/**
 * The Class Log4J2WebLoggerContextUtilsAccessor.
 */
public class Log4J2WebLoggerContextUtilsAccessor extends DefaultAccessor {

  /**
   * Instantiates a new log4 j 2 web logger context utils accessor.
   *
   * @param cl the cl
   * @throws ClassNotFoundException the class not found exception
   */
  public Log4J2WebLoggerContextUtilsAccessor(ClassLoader cl) throws ClassNotFoundException {
    Class<?> clazz = cl.loadClass("org.apache.logging.log4j.web.WebLoggerContextUtils");
    setTarget(clazz);
  }

  /**
   * Gets the logger context configured for the given ServletContext.
   *
   * @return the root logger
   */
  public Log4J2LoggerContextAccessor getWebLoggerContext(ServletContext ctx) {
    try {
      Class<?> clazz = (Class<?>) getTarget();
      Method getWebLoggerContext = MethodUtils.getAccessibleMethod(clazz, "getWebLoggerContext",
          new Class[] {ServletContext.class});

      Object loggerContext = getWebLoggerContext.invoke(null, ctx);
      if (loggerContext == null) {
        throw new NullPointerException(getTarget().getClass().getName()
            + "#getWebLoggerContext(Ljavax/servlet/ServletContext) returned null");
      }
      Log4J2LoggerContextAccessor accessor = new Log4J2LoggerContextAccessor();
      accessor.setTarget(loggerContext);
      accessor.setApplication(getApplication());
      return accessor;
    } catch (Exception e) {
      logger.error("{}#getWebLoggerContext(Ljavax/servlet/ServletContext) failed",
          getTarget().getClass().getName(), e);
    }
    return null;
  }
}
