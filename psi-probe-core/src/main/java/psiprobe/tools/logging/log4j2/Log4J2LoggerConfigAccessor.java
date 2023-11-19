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

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.MethodUtils;

import psiprobe.tools.logging.DefaultAccessor;

/**
 * The Class Log4JLoggerAccessor.
 */
public class Log4J2LoggerConfigAccessor extends DefaultAccessor {

  /** The context. */
  private boolean context;

  /** The LoggerContext. */
  private Log4J2LoggerContextAccessor loggerContext;

  /** The loggers Map of appenders. **/
  private Map<String, Object> appenderMap;

  /**
   * Sets the target.
   *
   * @param target the new target
   */
  @Override
  @SuppressWarnings("unchecked")
  public void setTarget(Object target) {
    super.setTarget(target);

    try {
      this.appenderMap = (Map<String, Object>) invokeMethod(target, "getAppenders", null, null);
    } catch (Exception e) {
      logger.error("{}#getAppenders() failed", target.getClass().getName(), e);
    }
  }

  /**
   * Gets the appenders.
   *
   * @return the appenders
   */
  public List<Log4J2AppenderAccessor> getAppenders() {
    List<Log4J2AppenderAccessor> appenders = new ArrayList<>();
    if (appenderMap != null) {
      for (Object unwrappedAppender : appenderMap.values()) {
        Log4J2AppenderAccessor appender = wrapAppender(unwrappedAppender);
        if (appender != null) {
          appenders.add(appender);
        }
      }
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
  public Log4J2AppenderAccessor getAppender(String name) {
    if (this.appenderMap != null) {
      Object appender = appenderMap.get(name);
      return wrapAppender(appender);
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
   * Sets the logger context.
   *
   * @param loggerContext the new logger context
   */
  public void setLoggerContext(Log4J2LoggerContextAccessor loggerContext) {
    this.loggerContext = loggerContext;
  }

  /**
   * Checks if is root.
   *
   * @return true, if is root
   */
  public boolean isRoot() {
    return Strings.isNullOrEmpty(getName());
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
      loggerContext.updateLoggers();
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
  private Log4J2AppenderAccessor wrapAppender(Object appender) {
    try {
      if (appender == null) {
        throw new IllegalArgumentException("appender is null");
      }
      Log4J2AppenderAccessor appenderAccessor = new Log4J2AppenderAccessor();
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
