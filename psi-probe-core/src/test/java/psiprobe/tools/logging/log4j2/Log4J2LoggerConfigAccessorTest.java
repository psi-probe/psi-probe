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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class Log4J2LoggerConfigAccessorTest.
 */
class Log4J2LoggerConfigAccessorTest {

  /** The accessor. */
  private Log4J2LoggerConfigAccessor accessor;

  /** The mock target. */
  private Object mockTarget;

  /** The mock logger context. */
  private Log4J2LoggerContextAccessor mockLoggerContext;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    accessor = new Log4J2LoggerConfigAccessor();
    mockTarget = mock(Object.class);
    mockLoggerContext = mock(Log4J2LoggerContextAccessor.class);
    accessor.setLoggerContext(mockLoggerContext);
  }

  /**
   * Test set and get context.
   */
  @Test
  void testSetAndGetContext() {
    accessor.setContext(true);
    assertTrue(accessor.isContext());
  }

  /**
   * Test set target and get appenders.
   */
  @Test
  void testSetTargetAndGetAppenders() {
    Map<String, Object> appenderMap = new HashMap<>();
    Object appender = mock(Object.class);
    appenderMap.put("A1", appender);

    Log4J2AppenderAccessor wrapped = mock(Log4J2AppenderAccessor.class);

    Log4J2LoggerConfigAccessor spyAccessor = spy(accessor);
    doReturn(appenderMap).when(spyAccessor).invokeMethod(any(), eq("getAppenders"), any(), any());
    doReturn(wrapped).when(spyAccessor).wrapAppender(any());

    spyAccessor.setTarget(mockTarget);
    assertEquals(1, spyAccessor.getAppenders().size());
  }

  /**
   * Test get appender by name.
   */
  @Test
  void testGetAppenderByName() {
    Map<String, Object> appenderMap = new HashMap<>();
    Object appender = mock(Object.class);
    appenderMap.put("A1", appender);

    Log4J2LoggerConfigAccessor spyAccessor = spy(accessor);
    doReturn(appenderMap).when(spyAccessor).invokeMethod(any(), eq("getAppenders"), any(), any());
    doReturn(new Log4J2AppenderAccessor()).when(spyAccessor).wrapAppender(any());

    spyAccessor.setTarget(mockTarget);
    assertNotNull(spyAccessor.getAppender("A1"));
  }

  /**
   * Test is root.
   */
  @Test
  void testIsRoot() {
    Log4J2LoggerConfigAccessor spyAccessor = spy(accessor);
    doReturn("").when(spyAccessor).getName();
    assertTrue(spyAccessor.isRoot());
  }

}
