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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class BackwardsLineReaderTest.
 */
class BackwardsLineReaderTest {

  /**
   * Read lines backwards test.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  void readLinesBackwardsTest() throws IOException {
    File file = Files.createTempFile("backwards-reader-", ".log").toFile();
    Files.writeString(file.toPath(), "line1\nline2\nline3", StandardCharsets.UTF_8);

    try (BackwardsFileStream stream = new BackwardsFileStream(file)) {
      BackwardsLineReader reader = new BackwardsLineReader(stream);
      Assertions.assertEquals("line3", reader.readLine());
      Assertions.assertEquals("line2", reader.readLine());
      Assertions.assertEquals("line1", reader.readLine());
      Assertions.assertNull(reader.readLine());
      reader.close();
    } finally {
      Files.deleteIfExists(file.toPath());
    }
  }

  /**
   * Read lines backwards with CRLF test.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  void readLinesBackwardsCrLfTest() throws IOException {
    File file = Files.createTempFile("backwards-reader-crlf-", ".log").toFile();
    Files.writeString(file.toPath(), "left\r\nright", StandardCharsets.UTF_8);

    try (BackwardsFileStream stream = new BackwardsFileStream(file)) {
      BackwardsLineReader reader = new BackwardsLineReader(stream, StandardCharsets.UTF_8.name());
      Assertions.assertEquals("right", reader.readLine());
      Assertions.assertEquals("left", reader.readLine());
      Assertions.assertNull(reader.readLine());
      reader.close();
    } finally {
      Files.deleteIfExists(file.toPath());
    }
  }

  /**
   * Backwards file stream from position test.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  void backwardsFileStreamFromPositionTest() throws IOException {
    File file = Files.createTempFile("backwards-stream-pos-", ".txt").toFile();
    Files.writeString(file.toPath(), "abcde", StandardCharsets.UTF_8);

    try (BackwardsFileStream stream = new BackwardsFileStream(file, 3)) {
      Assertions.assertEquals('c', stream.read());
      Assertions.assertEquals('b', stream.read());
      Assertions.assertEquals('a', stream.read());
      Assertions.assertEquals(-1, stream.read());
    } finally {
      Files.deleteIfExists(file.toPath());
    }
  }
}
