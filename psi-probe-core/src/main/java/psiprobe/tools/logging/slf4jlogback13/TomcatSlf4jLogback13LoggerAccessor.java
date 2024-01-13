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
package psiprobe.tools.logging.slf4jlogback13;

import com.google.common.collect.Iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;

import psiprobe.tools.logging.DefaultAccessor;

/**
 * A wrapper for a TomcatSlf4jLogback logger.
 */
public class TomcatSlf4jLogback13LoggerAccessor extends DefaultAccessor {

  /**
   * Returns all appenders of this logger.
   *
   * @return a list of {@link TomcatSlf4jLogback13AppenderAccessor}s
   */
  @SuppressWarnings("unchecked")
  public List<TomcatSlf4jLogback13AppenderAccessor> getAppenders() {
    List<TomcatSlf4jLogback13AppenderAccessor> appenders = new ArrayList<>();

    try {
      for (Object appender : Collections.list(Iterators.asEnumeration(
          (Iterator<Object>) MethodUtils.invokeMethod(getTarget(), "iteratorForAppenders")))) {
        List<Object> siftedAppenders = getSiftedAppenders(appender);
        if (!siftedAppenders.isEmpty()) {
          for (Object siftedAppender : siftedAppenders) {
            wrapAndAddAppender(siftedAppender, appenders);
          }
        } else {
          wrapAndAddAppender(appender, appenders);
        }
      }
    } catch (NoClassDefFoundError e) {
      logger.error("{}#getAppenders() failed", getTarget().getClass().getName(), e);
      logger.error("To see this logger, upgrade slf4j to 1.7.21+");
    } catch (Exception e) {
      logger.error("{}#getAppenders() failed", getTarget().getClass().getName(), e);
    }
    return appenders;
  }

  /**
   * Returns the appender of this logger with the given name.
   *
   * @param name the name of the appender to return
   *
   * @return the appender with the given name, or null if no such appender exists for this logger
   */
  public TomcatSlf4jLogback13AppenderAccessor getAppender(String name) {
    try {
      Object appender = MethodUtils.invokeMethod(getTarget(), "getAppender", name);
      if (appender == null) {
        List<TomcatSlf4jLogback13AppenderAccessor> appenders = getAppenders();
        for (TomcatSlf4jLogback13AppenderAccessor wrappedAppender : appenders) {
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
      Object level = MethodUtils.invokeMethod(getTarget(), "getLevel");
      return (String) MethodUtils.invokeMethod(level, "toString");
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
      Object level = MethodUtils.invokeMethod(getTarget(), "getLevel");
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
   *
   * @return the sifted appenders
   *
   * @throws Exception the exception
   */
  @SuppressWarnings("unchecked")
  private List<Object> getSiftedAppenders(Object appender) throws Exception {
    if ("org.apache.juli.logging.ch.qos.logback.classic.sift.SiftingAppender"
        .equals(appender.getClass().getName())) {

      Object tracker = MethodUtils.invokeMethod(appender, "getAppenderTracker");
      if (tracker != null) {
        return (List<Object>) MethodUtils.invokeMethod(tracker, "allComponents");
      }
    }
    return Collections.emptyList();
  }

  /**
   * Wrap and add appender.
   *
   * @param appender the appender
   * @param appenders the appenders
   */
  private void wrapAndAddAppender(Object appender,
      Collection<TomcatSlf4jLogback13AppenderAccessor> appenders) {

    TomcatSlf4jLogback13AppenderAccessor appenderAccessor = wrapAppender(appender);
    if (appenderAccessor != null) {
      appenders.add(appenderAccessor);
    }
  }

  /**
   * Wrap appender.
   *
   * @param appender the appender
   *
   * @return the tomcat slf4j logback appender accessor
   */
  private TomcatSlf4jLogback13AppenderAccessor wrapAppender(Object appender) {
    try {
      if (appender == null) {
        throw new IllegalArgumentException("appender is null");
      }
      TomcatSlf4jLogback13AppenderAccessor appenderAccessor =
          new TomcatSlf4jLogback13AppenderAccessor();

      appenderAccessor.setTarget(appender);
      appenderAccessor.setLoggerAccessor(this);
      appenderAccessor.setApplication(getApplication());
      return appenderAccessor;
    } catch (IllegalArgumentException e) {
      logger.error("Could not wrap appender: '{}'", appender, e);
    }
    return null;
  }

}
