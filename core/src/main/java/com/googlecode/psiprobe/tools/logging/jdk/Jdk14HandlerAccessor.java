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

package com.googlecode.psiprobe.tools.logging.jdk;

import com.googlecode.psiprobe.tools.logging.AbstractLogDestination;

import org.apache.commons.beanutils.MethodUtils;

/**
 * The Class Jdk14HandlerAccessor.
 *
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class Jdk14HandlerAccessor extends AbstractLogDestination {

  /** The logger accessor. */
  private Jdk14LoggerAccessor loggerAccessor;
  
  /** The index. */
  private String index;

  /**
   * Gets the logger accessor.
   *
   * @return the logger accessor
   */
  public Jdk14LoggerAccessor getLoggerAccessor() {
    return loggerAccessor;
  }

  /**
   * Sets the logger accessor.
   *
   * @param loggerAccessor the new logger accessor
   */
  public void setLoggerAccessor(Jdk14LoggerAccessor loggerAccessor) {
    this.loggerAccessor = loggerAccessor;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.AbstractLogDestination#isContext()
   */
  @Override
  public boolean isContext() {
    return getLoggerAccessor().isContext();
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.AbstractLogDestination#isRoot()
   */
  @Override
  public boolean isRoot() {
    return getLoggerAccessor().isRoot();
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getName()
   */
  public String getName() {
    return getLoggerAccessor().getName();
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.AbstractLogDestination#getIndex()
   */
  @Override
  public String getIndex() {
    return index;
  }

  /**
   * Sets the index.
   *
   * @param index the new index
   */
  public void setIndex(String index) {
    this.index = index;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getLogType()
   */
  public String getLogType() {
    return "jdk";
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.AbstractLogDestination#getLevel()
   */
  @Override
  public String getLevel() {
    return getLoggerAccessor().getLevel();
  }

  /**
   * Sets the level.
   *
   * @param newLevelStr the new level
   */
  public void setLevel(String newLevelStr) {
    try {
      Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
      Object newLevel = MethodUtils.invokeMethod(level, "parse", newLevelStr);
      MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
    } catch (Exception e) {
      log.error(getTarget().getClass().getName() + "#setLevel(\"" + newLevelStr + "\") failed", e);
    }
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.AbstractLogDestination#getValidLevels()
   */
  @Override
  public String[] getValidLevels() {
    return new String[] {"OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST",
        "ALL"};
  }

}
