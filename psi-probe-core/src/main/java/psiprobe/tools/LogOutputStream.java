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
package psiprobe.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@code OutputStream} which writes to a commons-logging {@code Log} at a particular level.
 */
public final class LogOutputStream extends OutputStream {

  /** The Constant INTERNAL_LOGGER. */
  private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger(LogOutputStream.class);

  /** The Constant LEVEL_OFF. */
  public static final int LEVEL_OFF = 0;

  /** The Constant LEVEL_TRACE. */
  public static final int LEVEL_TRACE = 1;

  /** The Constant LEVEL_DEBUG. */
  public static final int LEVEL_DEBUG = 2;

  /** The Constant LEVEL_INFO. */
  public static final int LEVEL_INFO = 3;

  /** The Constant LEVEL_WARN. */
  public static final int LEVEL_WARN = 4;

  /** The Constant LEVEL_ERROR. */
  public static final int LEVEL_ERROR = 5;

  /** The Constant LEVEL_FATAL. */
  public static final int LEVEL_FATAL = 6;

  /** The logger. */
  private final Logger logger;

  /** The level. */
  private final int level;

  /** The buf. */
  private final StringBuilder buf = new StringBuilder();

  /**
   * Creates a new instance of {@code LogOutputStream} which will write to a given {@code Log} at
   * the given level.
   *
   * @param log the {@code Log} to which to write
   * @param level the level at which to write
   */
  private LogOutputStream(Logger log, int level) {
    if (log == null) {
      throw new IllegalArgumentException("Log cannot be null");
    }
    this.logger = log;
    this.level = level;
  }

  /**
   * Creates a {@code PrintStream} with autoFlush enabled which will write to the given {@code Log}
   * at the given level.
   *
   * @param log the {@code Log} to which to write
   * @param level the level at which to write
   *
   * @return a {@code PrintStream} that writes to the given log
   */
  public static PrintStream createPrintStream(Logger log, int level) {
    try (LogOutputStream logStream = new LogOutputStream(log, level)) {
      return new PrintStream(logStream, true, StandardCharsets.UTF_8.name());
    } catch (IOException e) {
      INTERNAL_LOGGER.error("", e);
    }
    return null;
  }

  /**
   * Flushes the contents of this stream to its {@code Log}.
   */
  @Override
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
   * @param out the {@code byte} to write
   */
  @Override
  public void write(int out) {
    if (shouldWrite()) {
      char chr = (char) out;
      buf.append(chr);
    }
  }

  /**
   * Returns the {@code Log} to which this stream writes.
   *
   * @return the {@code Log} to which this stream writes
   */
  public Logger getLog() {
    return logger;
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
   * Determines if the {@code Log} is configured to accept messages at this stream's level.
   *
   * @return {@code true} if the level of the underlying {@code Log} is equal to or greater than the
   *         level assigned to this stream
   */
  private boolean shouldWrite() {
    switch (level) {
      case LEVEL_TRACE:
        return logger.isTraceEnabled();
      case LEVEL_DEBUG:
        return logger.isDebugEnabled();
      case LEVEL_INFO:
        return logger.isInfoEnabled();
      case LEVEL_WARN:
        return logger.isWarnEnabled();
      case LEVEL_ERROR:
        return logger.isErrorEnabled();
      default:
        return false;
    }
  }

  /**
   * Writes the given message to this stream's {@code Log} at this stream's level.
   *
   * @param message the message to be written
   */
  private void log(String message) {
    if (message == null || "".equals(message)) {
      return;
    }
    switch (level) {
      case LEVEL_TRACE:
        logger.trace(message);
        break;
      case LEVEL_DEBUG:
        logger.debug(message);
        break;
      case LEVEL_INFO:
        logger.info(message);
        break;
      case LEVEL_WARN:
        logger.warn(message);
        break;
      case LEVEL_ERROR:
        logger.error(message);
        break;
      default:
        // Don't log anything
        break;
    }
  }

}
