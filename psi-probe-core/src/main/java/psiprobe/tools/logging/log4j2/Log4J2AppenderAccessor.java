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

import java.io.File;
import java.nio.file.Path;

import psiprobe.tools.logging.AbstractLogDestination;

/**
 * The Class Log4J2AppenderAccessor.
 */
public class Log4J2AppenderAccessor extends AbstractLogDestination {

  /** The logger accessor. */
  private Log4J2LoggerConfigAccessor loggerAccessor;

  /**
   * Gets the logger accessor.
   *
   * @return the logger accessor
   */
  public Log4J2LoggerConfigAccessor getLoggerAccessor() {
    return loggerAccessor;
  }

  /**
   * Sets the logger accessor.
   *
   * @param loggerAccessor the new logger accessor
   */
  public void setLoggerAccessor(Log4J2LoggerConfigAccessor loggerAccessor) {
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
    return "log4j2";
  }

  @Override
  public String getIndex() {
    return (String) invokeMethod(getTarget(), "getName", null, null);
  }

  @Override
  public String getConversionPattern() {
    Object layout = invokeMethod(getTarget(), "getLayout", null, null);
    if (layout != null && "org.apache.logging.log4j.core.layout.PatternLayout"
        .equals(layout.getClass().getName())) {
      return (String) invokeMethod(layout, "getConversionPattern", null, null);
    }
    return null;
  }

  @Override
  public File getFile() {
    String fileName = (String) getProperty(getTarget(), "fileName", null);
    if (fileName != null) {
      return Path.of(fileName).toFile();
    }
    // Check for SMTPAppender information
    File result = null;
    if ("org.apache.logging.log4j.core.appender.SmtpAppender"
        .equals(getTarget().getClass().getName())) {
      Object smtpManager = getProperty(getTarget(), "manager", null, true);
      Object factoryData = getProperty(smtpManager, "data", null, true);
      Object cc = getProperty(factoryData, "cc", null, true);
      Object bcc = getProperty(factoryData, "bcc", null, true);
      Object from = getProperty(factoryData, "from", null, true);
      Object subjectSerializer = getProperty(factoryData, "subject", null, true);
      String subject = null;
      if (subjectSerializer != null) {
        Object[] subjectFormatters =
            (Object[]) getProperty(subjectSerializer, "formatters", null, true);
        if (subjectFormatters != null) {
          Object subjectFormatterConverter =
              getProperty(subjectFormatters[0], "converter", null, true);
          if (subjectFormatterConverter != null) {
            subject = (String) getProperty(subjectFormatterConverter, "literal", null, true);
          }
        }
      }
      result = Path
          .of("mailto:" + getProperty(factoryData, "to", "", true)
              + (from != null ? "&from=" + from : "") + (cc != null ? "&cc=" + cc : "")
              + (bcc != null ? "&bcc=" + bcc : "") + (subject != null ? "&subject=" + subject : ""))
          .toFile();
    } else {
      result = getStdoutFile();
    }
    return result;
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
