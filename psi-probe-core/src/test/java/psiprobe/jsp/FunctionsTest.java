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
package psiprobe.jsp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Functions}.
 */
class FunctionsTest {

  @Test
  void testPrivateConstructor() {
    JavaBeanTester.builder(Functions.class).testPrivateConstructor();
  }

  @Test
  void testSafeCookieNameNoQuotes() {
    assertEquals("simpleName", Functions.safeCookieName("simpleName"));
  }

  @Test
  void testSafeCookieNameWithDoubleQuotes() {
    assertEquals("name", Functions.safeCookieName("\"name\""));
  }

  @Test
  void testSafeCookieNameMultipleQuotes() {
    assertEquals("name value", Functions.safeCookieName("\"name\" \"value\""));
  }

  @Test
  void testSafeCookieNameEmpty() {
    assertEquals("", Functions.safeCookieName(""));
  }

  @Test
  void testSafeCookieNameOnlyQuotes() {
    assertEquals("", Functions.safeCookieName("\"\""));
  }
}
