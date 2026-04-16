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
package psiprobe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class UtilsTest.
 */
class UtilsTest {

  /**
   * To int test.
   */
  @Test
  void toIntTest() {
    Assertions.assertEquals(5, Utils.toInt("garbage", 5));
    Assertions.assertEquals(3, Utils.toInt("3", 5));
    Assertions.assertEquals(5, Utils.toInt("3 3", 5));
    Assertions.assertEquals(5, Utils.toInt((String) null, 5));
  }

  /**
   * To int hex test.
   */
  @Test
  void toIntHexTest() {
    Assertions.assertEquals(5, Utils.toIntHex("garbage", 5));
    Assertions.assertEquals(3, Utils.toIntHex("3", 5));
    Assertions.assertEquals(3, Utils.toIntHex("#3", 5));
    Assertions.assertEquals(5, Utils.toIntHex("3 3", 5));
    Assertions.assertEquals(5, Utils.toIntHex((String) null, 5));
  }

  /**
   * To long test.
   */
  @Test
  void toLongTest() {
    Assertions.assertEquals(5L, Utils.toLong("garbage", 5L));
    Assertions.assertEquals(3L, Utils.toLong("3", 5L));
    Assertions.assertEquals(5L, Utils.toLong("3 3", 5L));
    Assertions.assertEquals(5L, Utils.toLong((String) null, 5L));
  }

  /**
   * To long int test.
   */
  @Test
  void toLongIntTest() {
    Assertions.assertEquals(5, Utils.toLong((Long) null, 5));
    Assertions.assertEquals(1, Utils.toLong(Long.valueOf(1), 5));
  }

  /**
   * To float test.
   */
  @Test
  void toFloatTest() {
    Assertions.assertEquals(5.0f, Utils.toFloat("garbage", 5.0f), 0.0);
    Assertions.assertEquals(3.0f, Utils.toFloat("3", 5.0f), 0.0);
    Assertions.assertEquals(5.0f, Utils.toFloat("3 3", 5.0f), 0.0);
    Assertions.assertEquals(5.0f, Utils.toFloat((String) null, 5.0f), 0.0);
  }

  /**
   * Left pad test.
   */
  @Test
  void leftPadTest() {
    Assertions.assertEquals("0005", Utils.leftPad("5", 4, "0"));
    Assertions.assertEquals("5", Utils.leftPad("5", 1, "0"));
    Assertions.assertEquals("", Utils.leftPad(null, 4, "0"));
  }

  /**
   * Read stream and file test.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  void readStreamAndFileTest() throws IOException {
    String payload = "alpha\nbeta";
    byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
    Assertions.assertEquals("alpha\nbeta\n",
        Utils.readStream(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8.name()));
    // Unsupported charset names fall back to the platform default charset.
    Assertions.assertEquals("alpha\nbeta\n",
        Utils.readStream(new ByteArrayInputStream(bytes), "unsupported-charset"));

    File file = Files.createTempFile("utils-test-", ".txt").toFile();
    Files.writeString(file.toPath(), payload, StandardCharsets.UTF_8);
    try {
      Assertions.assertEquals("alpha\nbeta\n", Utils.readFile(file, StandardCharsets.UTF_8.name()));
    } finally {
      Utils.delete(file);
    }
  }

  /**
   * JSP encoding parser test.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  void getJspEncodingTest() throws IOException {
    String pageEncodingJsp = "<%@ page pageEncoding=\"ISO-8859-1\" %>";
    Assertions.assertEquals("ISO-8859-1", Utils.getJspEncoding(
        new ByteArrayInputStream(pageEncodingJsp.getBytes(StandardCharsets.UTF_8))));

    String contentTypeJsp = "<%@ page contentType=\"text/html; charset=UTF-16\" %>";
    Assertions.assertEquals("UTF-16", Utils
        .getJspEncoding(new ByteArrayInputStream(contentTypeJsp.getBytes(StandardCharsets.UTF_8))));

    String defaultJsp = "<html><body>no directives</body></html>";
    Assertions.assertEquals(StandardCharsets.UTF_8.name(), Utils
        .getJspEncoding(new ByteArrayInputStream(defaultJsp.getBytes(StandardCharsets.UTF_8))));
  }

  /**
   * Locale name expansion test.
   */
  @Test
  void getNamesForLocaleTest() {
    List<String> names = Utils.getNamesForLocale("messages", new Locale("en", "US", "TX"));
    Assertions.assertEquals(List.of("messages_en_US_TX", "messages_en_US", "messages_en"), names);
    Assertions.assertTrue(Utils.getNamesForLocale("messages", Locale.ROOT).isEmpty());
  }

  /**
   * Thread lookup and threading support test.
   *
   * @throws InterruptedException Signals that an interrupted exception has occurred.
   */
  @Test
  void threadAndThreadingSupportTest() throws InterruptedException {
    Assertions.assertNull(Utils.getThreadByName(null));

    String threadName = "utils-test-" + UUID.randomUUID();
    CountDownLatch started = new CountDownLatch(1);
    CountDownLatch stop = new CountDownLatch(1);
    Thread thread = new Thread(() -> {
      started.countDown();
      try {
        stop.await(5, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }, threadName);
    thread.start();
    try {
      Assertions.assertTrue(started.await(2, TimeUnit.SECONDS));
      Assertions.assertNotNull(Utils.getThreadByName(threadName));
    } finally {
      stop.countDown();
      thread.join(2000);
    }

    Assertions.assertTrue(Utils.isThreadingEnabled());
  }

  /**
   * Recursive delete test.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  void deleteRecursivelyTest() throws IOException {
    Path dirPath = Files.createTempDirectory("utils-delete-test-");
    Path nestedPath = dirPath.resolve("nested");
    Path childPath = nestedPath.resolve("child.txt");
    Files.createDirectories(nestedPath);
    Files.writeString(childPath, "child", StandardCharsets.UTF_8);
    File dir = dirPath.toFile();

    Utils.delete(dir);
    Assertions.assertFalse(dir.exists());

    Assertions.assertDoesNotThrow(() -> Utils.delete(dir));
  }

}
