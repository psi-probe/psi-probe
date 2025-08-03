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
package psiprobe.tools.logging.logback;

import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;

import java.io.File;
import java.nio.file.Path;

import psiprobe.tools.logging.AbstractLogDestination;

/**
 * A wrapper for a Logback appender for a specific logger.
 */
public class LogbackAppenderAccessor extends AbstractLogDestination {

  /** The logger accessor. */
  private LogbackLoggerAccessor loggerAccessor;

  /**
   * Gets the logger accessor.
   *
   * @return the logger accessor
   */
  public LogbackLoggerAccessor getLoggerAccessor() {
    return loggerAccessor;
  }

  /**
   * Sets the logger accessor.
   *
   * @param loggerAccessor the new logger accessor
   */
  public void setLoggerAccessor(LogbackLoggerAccessor loggerAccessor) {
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

  /**
   * Returns the log type, to distinguish logback appenders from other types like log4j appenders or
   * jdk handlers.
   *
   * @return the log type
   */
  @Override
  public String getLogType() {
    return "logback";
  }

  /**
   * Returns the name of this appender.
   *
   * @return the name of this appender.
   */
  @Override
  public String getIndex() {
    return (String) getProperty(getTarget(), "name", null);
  }

  /**
   * Returns the file that this appender writes to by accessing the {@code file} bean property of
   * the appender.
   *
   * <p>
   * If no such property exists, we assume the appender to write to stdout or stderr so the output
   * will be contained in catalina.out.
   *
   * @return the file this appender writes to
   */
  @Override
  public File getFile() {
    String fileName = (String) getProperty(getTarget(), "file", null);
    return fileName != null ? Path.of(fileName).toFile() : getStdoutFile();
  }

  @Override
  public String getEncoding() {
    if (getTarget() instanceof OutputStreamAppender) {
      OutputStreamAppender<?> appender = (OutputStreamAppender<?>) getTarget();
      Encoder<?> encoder = appender.getEncoder();
      if (encoder instanceof LayoutWrappingEncoder) {
        LayoutWrappingEncoder<?> base = (LayoutWrappingEncoder<?>) encoder;
        if (base.getCharset() != null) {
          return base.getCharset().name();
        }
      }
    }
    return null;
  }

  /**
   * Gets the level of the associated logger.
   *
   * @return the logger's level
   */
  @Override
  public String getLevel() {
    return getLoggerAccessor().getLevel();
  }

  /**
   * Returns the valid log level names.
   *
   * <p>
   * Note that Logback has no FATAL level.
   *
   * @return the valid log level names
   */
  @Override
  public String[] getValidLevels() {
    return new String[] {"OFF", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL"};
  }

}
