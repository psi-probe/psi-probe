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

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

/**
 * The Class LogOutputStreamTest.
 */
@ExtendWith(MockitoExtension.class)
class LogOutputStreamTest {

  /** The stream. */
  PrintStream stream;

  /** The log. */
  @Mock
  Logger log;

  /**
   * Logger test.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  void loggerTest() throws IOException {
    stream = LogOutputStream.createPrintStream(log, LogOutputStream.LEVEL_ERROR);
    stream.write('\u0001');
  }

  @Test
  void testLevelTrace() {
    when(log.isTraceEnabled()).thenReturn(true);
    stream = LogOutputStream.createPrintStream(log, LogOutputStream.LEVEL_TRACE);
    Assertions.assertNotNull(stream);
    stream.print("trace message");
    stream.flush();
  }

  @Test
  void testLevelDebug() {
    when(log.isDebugEnabled()).thenReturn(true);
    stream = LogOutputStream.createPrintStream(log, LogOutputStream.LEVEL_DEBUG);
    Assertions.assertNotNull(stream);
    stream.print("debug message");
    stream.flush();
  }

  @Test
  void testLevelInfo() {
    when(log.isInfoEnabled()).thenReturn(true);
    stream = LogOutputStream.createPrintStream(log, LogOutputStream.LEVEL_INFO);
    Assertions.assertNotNull(stream);
    stream.print("info message");
    stream.flush();
  }

  @Test
  void testLevelWarn() {
    when(log.isWarnEnabled()).thenReturn(true);
    stream = LogOutputStream.createPrintStream(log, LogOutputStream.LEVEL_WARN);
    Assertions.assertNotNull(stream);
    stream.print("warn message");
    stream.flush();
  }

  @Test
  void testLevelOff() {
    stream = LogOutputStream.createPrintStream(log, LogOutputStream.LEVEL_OFF);
    Assertions.assertNotNull(stream);
    stream.print("off message - should not be logged");
    stream.flush();
  }

  @Test
  void testLevelFatal() {
    // LEVEL_FATAL (6) hits default case in shouldWrite(), returns false so no logging
    stream = LogOutputStream.createPrintStream(log, LogOutputStream.LEVEL_FATAL);
    Assertions.assertNotNull(stream);
    stream.print("fatal message");
    stream.flush();
  }

  @Test
  void testFlushWithEmptyBuffer() {
    when(log.isInfoEnabled()).thenReturn(true);
    stream = LogOutputStream.createPrintStream(log, LogOutputStream.LEVEL_INFO);
    // flush with empty buffer should not log
    stream.flush();
  }

  @Test
  void testNullLogThrows() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> LogOutputStream.createPrintStream(null, LogOutputStream.LEVEL_INFO));
  }
}
