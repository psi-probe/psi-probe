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
import java.io.PrintStream;

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
    stream = LogOutputStream.createPrintStream(log, 5);
    stream.write('\u0001');
  }

}
