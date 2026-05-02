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
package psiprobe.model.jsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CompilerException}.
 */
class CompilerExceptionTest {

  @Test
  void testDefaultConstructor() {
    CompilerException ex = new CompilerException();
    assertNull(ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
  void testMessageConstructor() {
    CompilerException ex = new CompilerException("compile error");
    assertEquals("compile error", ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
  void testCauseConstructor() {
    Throwable cause = new RuntimeException("root cause");
    CompilerException ex = new CompilerException(cause);
    assertNotNull(ex.getCause());
    assertEquals(cause, ex.getCause());
  }

  @Test
  void testMessageAndCauseConstructor() {
    Throwable cause = new RuntimeException("root");
    CompilerException ex = new CompilerException("compile failed", cause);
    assertEquals("compile failed", ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}
