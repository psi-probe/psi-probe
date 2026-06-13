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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SimpleAccessor}.
 */
class SimpleAccessorTest {

  /** Test target class with a public and a private field. */
  static class Target {
    public String publicField = "public";
    private String privateField = "private";
  }

  @Test
  void testGetPublicField() throws NoSuchFieldException {
    SimpleAccessor accessor = new SimpleAccessor();
    Target target = new Target();
    Field field = Target.class.getDeclaredField("publicField");
    Object value = accessor.get(target, field);
    assertEquals("public", value);
  }

  @Test
  void testGetPrivateField() throws NoSuchFieldException {
    SimpleAccessor accessor = new SimpleAccessor();
    Target target = new Target();
    Field field = Target.class.getDeclaredField("privateField");
    // private field can be accessed after setAccessible(true)
    Object value = accessor.get(target, field);
    assertEquals("private", value);
  }

  @Test
  void testGetStaticField() throws NoSuchFieldException {
    SimpleAccessor accessor = new SimpleAccessor();
    // Test with a static field from Integer (which can be accessed with null object)
    Field field = Integer.class.getDeclaredField("MAX_VALUE");
    Object value = accessor.get(null, field);
    assertEquals(Integer.MAX_VALUE, value);
  }
}
