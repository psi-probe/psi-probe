/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.tools.logging.logback;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.IteratorUtils;

import psiprobe.tools.logging.DefaultAccessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A wrapper for a Logback logger.
 * 
 * @author Harald Wellmann
 * @author Mark Lewis
 */
public class LogbackLoggerAccessor extends DefaultAccessor {

  /**
   * Returns all appenders of this logger.
   * 
   * @return a list of {@link LogbackAppenderAccessor}s
   */
  public List<LogbackAppenderAccessor> getAppenders() {
    List<LogbackAppenderAccessor> appenders = new ArrayList<>();
    try {
      for (Object appender : Collections.list(IteratorUtils.asEnumeration((Iterator<Object>) MethodUtils
              .invokeMethod(getTarget(), "iteratorForAppenders", null)))) {
        List<Object> siftedAppenders = getSiftedAppenders(appender);
        if (siftedAppenders != null) {
          for (Object siftedAppender : siftedAppenders) {
            wrapAndAddAppender(siftedAppender, appenders);
          }
        } else {
          wrapAndAddAppender(appender, appenders);
        }
      }
    } catch (Exception e) {
      logger.error("{}#getAppenders() failed", getTarget().getClass().getName(), e);
    }
    return appenders;
  }

  /**
   * Returns the appender of this logger with the given name.
   * 
   * @param name the name of the appender to return
   * @return the appender with the given name, or null if no such appender exists for this logger
   */
  public LogbackAppenderAccessor getAppender(String name) {
    try {
      Object appender = MethodUtils.invokeMethod(getTarget(), "getAppender", name);
      if (appender == null) {
        List<LogbackAppenderAccessor> appenders = getAppenders();
        for (LogbackAppenderAccessor wrappedAppender : appenders) {
          if (wrappedAppender.getIndex().equals(name)) {
            return wrappedAppender;
          }
        }
      }
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
    return false;
  }

  /**
   * Checks if is root.
   *
   * @return true, if is root
   */
  public boolean isRoot() {
    return "ROOT".equals(getName());
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
   * Gets the log level of this logger.
   * 
   * @return the level of this logger
   */
  public String getLevel() {
    try {
      Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
      return (String) MethodUtils.invokeMethod(level, "toString", null);
    } catch (Exception e) {
      logger.error("{}#getLevel() failed", getTarget().getClass().getName(), e);
    }
    return null;
  }

  /**
   * Sets the log level of this logger.
   * 
   * @param newLevelStr the name of the new level
   */
  public void setLevel(String newLevelStr) {
    try {
      Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
      Object newLevel = MethodUtils.invokeMethod(level, "toLevel", newLevelStr);
      MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
    } catch (Exception e) {
      logger.error("{}#setLevel('{}') failed", getTarget().getClass().getName(), newLevelStr, e);
    }
  }

  /**
   * Gets the sifted appenders.
   *
   * @param appender the appender
   * @return the sifted appenders
   * @throws Exception the exception
   */
  private List<Object> getSiftedAppenders(Object appender) throws Exception {
    if ("ch.qos.logback.classic.sift.SiftingAppender".equals(appender.getClass().getName())) {
      Object tracker = MethodUtils.invokeMethod(appender, "getAppenderTracker", null);
      if (tracker != null) {
        try {
          return (List<Object>) MethodUtils.invokeMethod(tracker, "allComponents", null);
        } catch (final NoSuchMethodException e) {
          // XXX Legacy 1.0.x and lower support for logback
          logger.trace("", e);
          return (List<Object>) MethodUtils.invokeMethod(tracker, "valueList", null);
        }
      }
      return new ArrayList<>();
    }
    return null;
  }

  /**
   * Wrap and add appender.
   *
   * @param appender the appender
   * @param appenders the appenders
   */
  private void wrapAndAddAppender(Object appender, List<LogbackAppenderAccessor> appenders) {
    LogbackAppenderAccessor appenderAccessor = wrapAppender(appender);
    if (appenderAccessor != null) {
      appenders.add(appenderAccessor);
    }
  }

  /**
   * Wrap appender.
   *
   * @param appender the appender
   * @return the logback appender accessor
   */
  private LogbackAppenderAccessor wrapAppender(Object appender) {
    try {
      if (appender == null) {
        throw new IllegalArgumentException("appender is null");
      }
      LogbackAppenderAccessor appenderAccessor = new LogbackAppenderAccessor();
      appenderAccessor.setTarget(appender);
      appenderAccessor.setLoggerAccessor(this);
      appenderAccessor.setApplication(getApplication());
      return appenderAccessor;
    } catch (Exception e) {
      logger.error("Could not wrap appender: '{}'", appender, e);
    }
    return null;
  }

}
