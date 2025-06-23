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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import psiprobe.tools.logging.LogDestination;

/**
 * The Class Jdk14ManagerAccessorTest.
 */
class Jdk14ManagerAccessorTest {

  /** The class loader. */
  private ClassLoader classLoader;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    classLoader = getClass().getClassLoader();
  }

  /**
   * Test constructor and get root logger.
   *
   * @throws Exception the exception
   */
  @Test
  void testConstructorAndGetRootLogger() throws Exception {
    Jdk14ManagerAccessor accessor = new Jdk14ManagerAccessor(classLoader);
    assertNotNull(accessor.getRootLogger());
  }

  /**
   * Test get logger.
   *
   * @throws Exception the exception
   */
  @Test
  void testGetLogger() throws Exception {
    Jdk14ManagerAccessor accessor = new Jdk14ManagerAccessor(classLoader);
    assertNotNull(accessor.getLogger(""));
    assertNotNull(accessor.getLogger("global"));
  }

  /**
   * Test get handlers.
   *
   * @throws Exception the exception
   */
  @Test
  void testGetHandlers() throws Exception {
    Jdk14ManagerAccessor accessor = new Jdk14ManagerAccessor(classLoader);
    List<LogDestination> handlers = accessor.getHandlers();
    assertNotNull(handlers);
  }

}
