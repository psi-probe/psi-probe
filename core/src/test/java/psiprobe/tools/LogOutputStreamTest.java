/**
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
import org.slf4j.Logger;
import mockit.Mocked;

/**
 * The Class LogOutputStreamTest.
 */
public class LogOutputStreamTest {

  /** The stream. */
  PrintStream stream;

  /** The log. */
  @Mocked
  Logger log;

  /**
   * Logger test.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  public void loggerTest() throws IOException {
    stream = LogOutputStream.createPrintStream(log, 5);
    stream.write('\u0001');
  }

}
