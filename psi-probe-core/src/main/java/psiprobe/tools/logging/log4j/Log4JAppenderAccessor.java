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

import java.io.File;
import java.nio.file.Path;

import psiprobe.tools.logging.AbstractLogDestination;

/**
 * The Class Log4JAppenderAccessor.
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

  @Override
  public String getName() {
    return getLoggerAccessor().getName();
  }

  @Override
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
    }
    return null;
  }

  @Override
  public File getFile() {
    String fileName = (String) getProperty(getTarget(), "file", null);
    return fileName != null ? Path.of(fileName).toFile() : getStdoutFile();
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
