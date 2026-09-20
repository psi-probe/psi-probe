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
package psiprobe.tools.logging.logback13;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;

import com.codebox.bean.JavaBeanTester;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class Logback13AppenderAccessorTest.
 */
class Logback13AppenderAccessorTest {

  private Logback13AppenderAccessor accessor;
  private Logback13LoggerAccessor loggerAccessor;

  @BeforeEach
  void setUp() {
    accessor = new Logback13AppenderAccessor();
    loggerAccessor = mock(Logback13LoggerAccessor.class);
    accessor.setLoggerAccessor(loggerAccessor);
  }

  @Test
  void testLoggerAccessor() {
    assertEquals(loggerAccessor, accessor.getLoggerAccessor());
  }

  @Test
  void testIsContext() {
    when(loggerAccessor.isContext()).thenReturn(true);

    assertEquals(true, accessor.isContext());
  }

  @Test
  void testIsRoot() {
    when(loggerAccessor.isRoot()).thenReturn(true);

    assertEquals(true, accessor.isRoot());
  }

  @Test
  void testGetName() {
    when(loggerAccessor.getName()).thenReturn("my.logger");

    assertEquals("my.logger", accessor.getName());
  }

  @Test
  void testGetLogType() {
    assertEquals("logback13", accessor.getLogType());
  }

  @Test
  void testGetIndex() {
    Object target = mock(Object.class);

    accessor.setTarget(target);

    // getProperty() is inherited from AbstractLogDestination. The target needs to expose
    // a "name" bean property for this test, so use a real Logback appender.
    OutputStreamAppender<?> appender = new OutputStreamAppender<>();
    appender.setName("testAppender");

    accessor.setTarget(appender);

    assertEquals("testAppender", accessor.getIndex());
  }

  @Test
  void testGetIndexWhenNameIsNull() {
    OutputStreamAppender<?> appender = new OutputStreamAppender<>();

    accessor.setTarget(appender);

    assertNull(accessor.getIndex());
  }

  @Test
  void testGetFile() {
    FileAppender<?> appender = new FileAppender<>();

    Path expected = Path.of("logs").resolve("test.log");
    appender.setFile(expected.toString());

    accessor.setTarget(appender);

    assertEquals(expected, accessor.getFile().toPath());
  }

  @Test
  void testGetFileWhenFileIsNotConfigured() {
    String originalCatalinaBase = System.getProperty("catalina.base");

    try {
      System.setProperty("catalina.base",
          Path.of("target", "nonexistent-catalina-base").toString());

      OutputStreamAppender<?> appender = new OutputStreamAppender<>();
      appender.setName("testAppender");

      accessor.setTarget(appender);

      assertEquals(Path.of("stdout").toFile(), accessor.getFile());
    } finally {
      if (originalCatalinaBase == null) {
        System.clearProperty("catalina.base");
      } else {
        System.setProperty("catalina.base", originalCatalinaBase);
      }
    }
  }

  @Test
  void testGetEncoding() {
    OutputStreamAppender appender = mock(OutputStreamAppender.class);
    LayoutWrappingEncoder encoder = mock(LayoutWrappingEncoder.class);

    when(appender.getEncoder()).thenReturn(encoder);
    when(encoder.getCharset()).thenReturn(StandardCharsets.UTF_8);

    accessor.setTarget(appender);

    assertEquals("UTF-8", accessor.getEncoding());
  }

  @Test
  void testGetEncodingWhenCharsetIsNull() {
    OutputStreamAppender appender = mock(OutputStreamAppender.class);
    LayoutWrappingEncoder encoder = mock(LayoutWrappingEncoder.class);

    when(appender.getEncoder()).thenReturn(encoder);
    when(encoder.getCharset()).thenReturn(null);

    assertNull(accessor.getEncoding());
  }

  @Test
  void testGetEncodingWhenEncoderIsNotLayoutWrappingEncoder() {
    OutputStreamAppender appender = mock(OutputStreamAppender.class);
    Encoder encoder = mock(Encoder.class);

    when(appender.getEncoder()).thenReturn(encoder);

    accessor.setTarget(appender);

    assertNull(accessor.getEncoding());
  }

  @Test
  void testGetEncodingWhenTargetIsNotOutputStreamAppender() {
    accessor.setTarget(new Object());

    assertNull(accessor.getEncoding());
  }

  @Test
  void testGetLevel() {
    when(loggerAccessor.getLevel()).thenReturn("INFO");

    assertEquals("INFO", accessor.getLevel());
  }

  @Test
  void testGetValidLevels() {
    assertArrayEquals(new String[] {"OFF", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL"},
        accessor.getValidLevels());
  }

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(Logback13AppenderAccessor.class).skip("level").test();
  }

}
