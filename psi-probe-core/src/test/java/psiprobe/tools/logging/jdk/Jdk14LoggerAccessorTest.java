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
package psiprobe.tools.logging.jdk;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class Jdk14LoggerAccessorTest.
 */
class Jdk14LoggerAccessorTest {

  /** The accessor. */
  private Jdk14LoggerAccessor accessor;

  /** The mock logger. */
  private Object mockLogger;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    accessor = new Jdk14LoggerAccessor();
    mockLogger = mock(Object.class);
    accessor.setTarget(mockLogger);
  }

  /**
   * Test set and is context.
   */
  @Test
  void testSetAndIsContext() {
    accessor.setContext(true);
    assertTrue(accessor.isContext());
    accessor.setContext(false);
    assertFalse(accessor.isContext());
  }

  /**
   * Test is root when name is null.
   */
  @Test
  void testIsRootWhenNameIsNull() {
    Jdk14LoggerAccessor spyAccessor = spy(accessor);
    doReturn(null).when(spyAccessor).getName();
    assertTrue(spyAccessor.isRoot());
  }

  /**
   * Test is root when juli root.
   */
  @Test
  void testIsRootWhenJuliRoot() {
    Jdk14LoggerAccessor spyAccessor = spy(accessor);
    doReturn("org.apache.juli.ClassLoaderLogManager$RootLogger").when(spyAccessor).getTargetClass();
    assertTrue(spyAccessor.isRoot());
  }

  /**
   * Test get handler with invalid index.
   */
  @Test
  void testGetHandlerWithInvalidIndex() {
    Jdk14LoggerAccessor spyAccessor = spy(accessor);
    Jdk14HandlerAccessor handler = spyAccessor.getHandler("notAnInt");
    assertNull(handler);
  }

  /**
   * Test set level handles exception.
   */
  @Test
  void testSetLevelHandlesException() {
    accessor.setLevel("INVALID_LEVEL");
    // No exception should be thrown
  }
}
