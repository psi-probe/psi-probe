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
package com.googlecode.psiprobe.tools;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.logging.Log;

/**
 * An {@code OutputStream} which writes to a commons-logging {@code Log} at a
 * particular level.
 *
 * @author Mark Lewis
 */
public class LogOutputStream extends OutputStream {

    public static final int LEVEL_OFF   = 0;
    public static final int LEVEL_TRACE = 1;
    public static final int LEVEL_DEBUG = 2;
    public static final int LEVEL_INFO  = 3;
    public static final int LEVEL_WARN  = 4;
    public static final int LEVEL_ERROR = 5;
    public static final int LEVEL_FATAL = 6;

    private Log log;
    private int level;
    private StringBuffer buf = new StringBuffer();

    /**
     * Creates a {@code PrintStream} with autoFlush enabled which will write to
     * the given {@code Log} at the given level.
     * 
     * @param log
     *        the {@code Log} to which to write
     * @param level
     *        the level at which to write
     * @return a {@code PrintStream} that writes to the given log
     */
    public static PrintStream createPrintStream(Log log, int level) {
        LogOutputStream logStream = new LogOutputStream(log, level);
        return new PrintStream(logStream, true);
    }

    /**
     * Creates a new instance of {@code LogOutputStream} which will write to
     * a given {@code Log} at the given level.
     * 
     * @param log
     *        the {@code Log} to which to write
     * @param level
     *        the level at which to write
     * @throws IllegalArgumentException
     *         if {@code log} is null
     */
    private LogOutputStream(Log log, int level) {
        if (log == null) {
            throw new IllegalArgumentException("Log cannot be null");
        } else {
            this.log = log;
        }
        this.level = level;
    }

    /**
     * Flushes the contents of this stream to its {@link Log}.
     */
    public void flush() {
        if (shouldWrite()) {
            String message = buf.toString();
            log(message);
        }
        buf.setLength(0);
    }

    /**
     * Writes the specified {@code byte} to this stream.
     *
     * @param b
     *        the {@code byte} to write
     */
    public void write(int b) {
        if (shouldWrite()) {
            char c = (char) b;
            buf.append(c);
        }
    }

    /**
     * Returns the {@code Log} to which this stream writes.
     *
     * @return the {@code Log} to which this stream writes
     */
    public Log getLog() {
        return log;
    }

    /**
     * Returns the level at which this stream writes to the {@code Log}.
     *
     * @return the level at which this stream writes to the {@code Log}
     */
    public int getLevel() {
        return level;
    }

    /**
     * Determines if the {@code Log} is configured to accept messages at this
     * stream's level.
     *
     * @return {@code true} if the level of the underlying {@code Log} is equal
     *         to or greater than the level assigned to this stream
     */
    private boolean shouldWrite() {
        switch (level) {
            case LEVEL_TRACE:
                return log.isTraceEnabled();
            case LEVEL_DEBUG:
                return log.isDebugEnabled();
            case LEVEL_INFO:
                return log.isInfoEnabled();
            case LEVEL_WARN:
                return log.isWarnEnabled();
            case LEVEL_ERROR:
                return log.isErrorEnabled();
            case LEVEL_FATAL:
                return log.isFatalEnabled();
            default:
                return false;
        }
    }

    /**
     * Writes the given message to this stream's {@code Log} at this stream's
     * level.
     * 
     * @param message
     *        the message to be written
     */
    private void log(String message) {
        if (message == null || "".equals(message)) {
            return;
        }
        switch (level) {
            case LEVEL_TRACE:
                log.trace(message);
            case LEVEL_DEBUG:
                log.debug(message);
            case LEVEL_INFO:
                log.info(message);
            case LEVEL_WARN:
                log.warn(message);
            case LEVEL_ERROR:
                log.error(message);
            case LEVEL_FATAL:
                log.fatal(message);
        }
    }

}