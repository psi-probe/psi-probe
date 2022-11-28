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
package psiprobe.tools.logging.log4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;

import psiprobe.tools.logging.DefaultAccessor;

/**
 * The Class Log4JLoggerAccessor.
 */
public class Log4JLoggerAccessor extends DefaultAccessor {

  /** The context. */
  private boolean context;

  /**
   * Gets the appenders.
   *
   * @return the appenders
   */
  @SuppressWarnings("unchecked")
  public List<Log4JAppenderAccessor> getAppenders() {
    List<Log4JAppenderAccessor> appenders = new ArrayList<>();
    try {
      for (Object unwrappedAppender : Collections
          .list((Enumeration<Object>) MethodUtils.invokeMethod(getTarget(), "getAllAppenders"))) {
        Log4JAppenderAccessor appender = wrapAppender(unwrappedAppender);
        if (appender != null) {
          appenders.add(appender);
        }
      }
    } catch (Exception e) {
      logger.error("{}#getAllAppenders() failed", getTarget().getClass().getName(), e);
    }
    return appenders;
  }

  /**
   * Gets the appender.
   *
   * @param name the name
   *
   * @return the appender
   */
  public Log4JAppenderAccessor getAppender(String name) {
    try {
      Object appender = MethodUtils.invokeMethod(getTarget(), "getAppender", name);
      return wrapAppender(appender);
    } catch (Exception e) {
      logger.error("{}#getAppender() failed", getTarget().getClass().getName(), e);
    }
    return null;
  }

  /**
   * Checks if is context.
   *
   * @return true, if is context
   */
  public boolean isContext() {
    return context;
  }

  /**
   * Sets the context.
   *
   * @param context the new context
   */
  public void setContext(boolean context) {
    this.context = context;
  }

  /**
   * Checks if is root.
   *
   * @return true, if is root
   */
  public boolean isRoot() {
    return "root".equals(getName()) && "org.apache.log4j.spi.RootLogger".equals(getTargetClass());
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return (String) getProperty(getTarget(), "name", null);
  }

  /**
   * Gets the level.
   *
   * @return the level
   */
  public String getLevel() {
    try {
      Object level = MethodUtils.invokeMethod(getTarget(), "getLevel");
      return (String) MethodUtils.invokeMethod(level, "toString");
    } catch (Exception e) {
      logger.error("{}#getLevel() failed", getTarget().getClass().getName(), e);
    }
    return null;
  }

  /**
   * Sets the level.
   *
   * @param newLevelStr the new level
   */
  public void setLevel(String newLevelStr) {
    try {
      Object level = MethodUtils.invokeMethod(getTarget(), "getLevel");
      Object newLevel = MethodUtils.invokeMethod(level, "toLevel", newLevelStr);
      MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
    } catch (Exception e) {
      logger.error("{}#setLevel('{}') failed", getTarget().getClass().getName(), newLevelStr, e);
    }
  }

  /**
   * Wrap appender.
   *
   * @param appender the appender
   *
   * @return the log4 j appender accessor
   */
  private Log4JAppenderAccessor wrapAppender(Object appender) {
    try {
      if (appender == null) {
        throw new IllegalArgumentException("appender is null");
      }
      Log4JAppenderAccessor appenderAccessor = new Log4JAppenderAccessor();
      appenderAccessor.setTarget(appender);
      appenderAccessor.setLoggerAccessor(this);
      appenderAccessor.setApplication(getApplication());
      return appenderAccessor;
    } catch (Exception e) {
      logger.error("Could not wrap appender: {}", appender, e);
    }
    return null;
  }

}
