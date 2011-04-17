/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.tools.logging.logback;

import com.googlecode.psiprobe.tools.logging.AbstractLogDestination;
import java.io.File;

/**
 * A wrapper for a Logback appender for a specific logger.
 * 
 * @author Harald Wellmann
 */
public class LogbackAppenderAccessor extends AbstractLogDestination {

    private LogbackLoggerAccessor loggerAccessor;

    public LogbackLoggerAccessor getLoggerAccessor() {
        return loggerAccessor;
    }

    public void setLoggerAccessor(LogbackLoggerAccessor loggerAccessor) {
        this.loggerAccessor = loggerAccessor;
    }

    public boolean isContext() {
        return getLoggerAccessor().isContext();
    }

    public boolean isRoot() {
        return getLoggerAccessor().isRoot();
    }

    public String getName() {
        return getLoggerAccessor().getName();
    }

    /**
     * Returns the log type, to distinguish logback appenders from other types
     * like log4j appenders or jdk handlers.
     * 
     * @return the log type
     */
    public String getLogType() {
        return "logback";
    }

    /**
     * Returns the name of this appender.
     * 
     * @return the name of this appender.
     */
    public String getIndex() {
        return (String) getProperty(getTarget(), "name", null);
    }

    /**
     * Returns the file that this appender writes to by accessing the
     * {@code file} bean property of the appender.
     * 
     * <p>
     * If no such property exists, we assume the appender to write to stdout or
     * stderr so the output will be contained in catalina.out.
     * <p>
     * 
     * @return the file this appender writes to
     */
    public File getFile() {
        String fileName = (String) getProperty(getTarget(), "file", null);
        return fileName != null ? new File(fileName) : getStdoutFile();
    }

    /**
     * Gets the level of the associated logger.
     * 
     * @return the logger's level
     */
    public String getLevel() {
        return getLoggerAccessor().getLevel();
    }

    /**
     * Returns the valid log level names.
     * 
     * <p>
     * Note that Logback has no FATAL level.
     * </p>
     * 
     * @return the valid log level names
     */
    public String[] getValidLevels() {
        return new String[] {"OFF", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL"};
    }

}
