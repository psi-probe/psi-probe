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
package psiprobe.tools.logging.catalina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import psiprobe.tools.Instruments;

/**
 * The Class CatalinaLoggerAccessorTest.
 */
class CatalinaLoggerAccessorTest {

  /** The accessor. */
  private CatalinaLoggerAccessor accessor;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    accessor = spy(new CatalinaLoggerAccessor());
  }

  /**
   * Test is context.
   */
  @Test
  void testIsContext() {
    assertTrue(accessor.isContext());
  }

  /**
   * Test get name.
   */
  @Test
  void testGetName() {
    assertNull(accessor.getName());
  }

  /**
   * Test get log type.
   */
  @Test
  void testGetLogType() {
    assertEquals("catalina", accessor.getLogType());
  }

  /**
   * Test get file with all fields.
   */
  @Test
  void testGetFileWithAllFields() {
    Object target = mock(Object.class);
    doReturn(target).when(accessor).getTarget();
    doReturn("/tmp").when(accessor).invokeMethod(target, "getDirectory", null, null);
    doReturn("catalina.").when(accessor).invokeMethod(target, "getPrefix", null, null);
    doReturn(".log").when(accessor).invokeMethod(target, "getSuffix", null, null);
    // Simulate timestamp field present
    try (MockedStatic<Instruments> mocked = mockStatic(Instruments.class)) {
      mocked.when(() -> Instruments.getField(target, "timestamp")).thenReturn(new Object());

      System.setProperty("catalina.base", "");

      File file = accessor.getFile();
      assertNotNull(file);
      assertTrue(file.getName().startsWith("catalina."));
      assertTrue(file.getName().endsWith(".log"));
    }
  }

  /**
   * Test get file with missing fields.
   */
  @Test
  void testGetFileWithMissingFields() {
    Object target = mock(Object.class);
    doReturn(target).when(accessor).getTarget();
    doReturn(null).when(accessor).invokeMethod(target, "getDirectory", null, null);
    doReturn("catalina.").when(accessor).invokeMethod(target, "getPrefix", null, null);
    doReturn(".log").when(accessor).invokeMethod(target, "getSuffix", null, null);
    try (MockedStatic<Instruments> mocked = mockStatic(Instruments.class)) {
      mocked.when(() -> Instruments.getField(target, "timestamp")).thenReturn(null);

      File file = accessor.getFile();
      assertNull(file);
    }
  }

}
