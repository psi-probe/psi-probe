/*
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import psiprobe.tools.logging.DefaultAccessor;

/**
 * The Class Log4J2WebLoggerContextUtilsAccessor.
 */
public class Log4J2WebLoggerContextUtilsAccessor extends DefaultAccessor {

  /** The Constant logger. */
  private static final Logger logger =
      LoggerFactory.getLogger(Log4J2WebLoggerContextUtilsAccessor.class);

  /**
   * Instantiates a new log4 j 2 web logger context utils accessor.
   *
   * @param cl the cl
   *
   * @throws ClassNotFoundException the class not found exception
   */
  public Log4J2WebLoggerContextUtilsAccessor(ClassLoader cl) throws ClassNotFoundException {
    logger.debug("Log4J2WebLoggerContextUtilsAccessor(): IN: cl={}", cl);
    Class<?> clazz = cl.loadClass("org.apache.logging.log4j.web.WebLoggerContextUtils");
    setTarget(clazz);
    logger.debug("Log4J2WebLoggerContextUtilsAccessor(): OUT: this={}", this);
  }

  /**
   * Gets the logger context configured for the given ServletContext.
   *
   * @param ctx the servlet context
   *
   * @return the root logger
   */
  public Log4J2LoggerContextAccessor getWebLoggerContext(ServletContext ctx) {
    logger.debug("getWebLoggerContext(): IN: ctx={}", ctx);
    Log4J2LoggerContextAccessor result = null;

    Class<?> clazz = (Class<?>) getTarget();
    Method getWebLoggerContext;
    try {
      getWebLoggerContext =
          MethodUtils.getAccessibleMethod(clazz, "getWebLoggerContext", ServletContext.class);
    } catch (Exception e) {
      logger.error("exception getting accessible method getWebLoggerContext", e);
      return result;
    }

    Object loggerContext;
    try {
      loggerContext = getWebLoggerContext.invoke(null, ctx);
    } catch (Exception e) {
      logger.error("exception getting logger context in getWebLoggerContext", e);
      return result;
    }

    result = new Log4J2LoggerContextAccessor();
    result.setTarget(loggerContext);
    result.setApplication(getApplication());

    logger.debug("getWebLoggerContext(): OUT: result={}", result);
    return result;
  }

}
