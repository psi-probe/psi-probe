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

package com.googlecode.psiprobe.tools.logging.log4j;

import com.googlecode.psiprobe.tools.logging.AbstractLogDestination;

import java.io.File;

/**
 * The Class Log4JAppenderAccessor.
 *
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class Log4JAppenderAccessor extends AbstractLogDestination {
  
  /** The logger accessor. */
  private Log4JLoggerAccessor loggerAccessor;

  /**
   * Gets the logger accessor.
   *
   * @return the logger accessor
   */
  public Log4JLoggerAccessor getLoggerAccessor() {
    return loggerAccessor;
  }

  /**
   * Sets the logger accessor.
   *
   * @param loggerAccessor the new logger accessor
   */
  public void setLoggerAccessor(Log4JLoggerAccessor loggerAccessor) {
    this.loggerAccessor = loggerAccessor;
  }

  @Override
  public boolean isContext() {
    return getLoggerAccessor().isContext();
  }

  @Override
  public boolean isRoot() {
    return getLoggerAccessor().isRoot();
  }

  public String getName() {
    return getLoggerAccessor().getName();
  }

  public String getLogType() {
    return "log4j";
  }

  @Override
  public String getIndex() {
    return (String) getProperty(getTarget(), "name", null);
  }

  @Override
  public String getConversionPattern() {
    Object layout = getProperty(getTarget(), "layout", null);
    if (layout != null && "org.apache.log4j.PatternLayout".equals(layout.getClass().getName())) {
      return (String) getProperty(layout, "conversionPattern", null);
    } else {
      return null;
    }
  }

  @Override
  public File getFile() {
    String fileName = (String) getProperty(getTarget(), "file", null);
    return fileName != null ? new File(fileName) : getStdoutFile();
  }

  @Override
  public String getLevel() {
    return getLoggerAccessor().getLevel();
  }

  @Override
  public String[] getValidLevels() {
    return new String[] {"OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL"};
  }

}
