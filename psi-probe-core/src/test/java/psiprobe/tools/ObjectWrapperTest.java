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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObjectWrapper}.
 */
class ObjectWrapperTest {

  @Test
  void testEqualsWithSameObject() {
    Object obj = new Object();
    ObjectWrapper w1 = new ObjectWrapper(obj);
    ObjectWrapper w2 = new ObjectWrapper(obj);
    assertEquals(w1, w2);
  }

  @Test
  void testEqualsWithDifferentObject() {
    ObjectWrapper w1 = new ObjectWrapper(new Object());
    ObjectWrapper w2 = new ObjectWrapper(new Object());
    assertNotEquals(w1, w2);
  }

  @Test
  void testEqualsWithNullWrapped() {
    ObjectWrapper w1 = new ObjectWrapper(null);
    // When wrapped object is null, equals(o1) returns o1 == null (not the wrapper)
    // So two ObjectWrapper(null) instances are NOT equal to each other
    assertNotEquals(w1, new ObjectWrapper(null));
    // But null itself does equal the wrapper wrapping null
    assertEquals(w1, null); // equals(null) when wrappedObject==null returns true
  }

  @Test
  void testEqualsNullWrappedVsNonNull() {
    ObjectWrapper w1 = new ObjectWrapper(null);
    ObjectWrapper w2 = new ObjectWrapper(new Object());
    // null vs non-null: o1 != null, so returns false
    assertNotEquals(w1, w2);
  }

  @Test
  void testEqualsDifferentClass() {
    ObjectWrapper w = new ObjectWrapper(new Object());
    assertFalse(w.equals("a string"));
  }

  @Test
  void testHashCodeWithSameInstance() {
    Object obj = new Object();
    ObjectWrapper w1 = new ObjectWrapper(obj);
    ObjectWrapper w2 = new ObjectWrapper(obj);
    assertEquals(w1.hashCode(), w2.hashCode());
  }

  @Test
  void testHashCodeWithNull() {
    ObjectWrapper w = new ObjectWrapper(null);
    assertEquals(System.identityHashCode(null), w.hashCode());
  }

  @Test
  void testHashCodeDifferentInstances() {
    Object obj1 = new Object();
    Object obj2 = new Object();
    ObjectWrapper w1 = new ObjectWrapper(obj1);
    ObjectWrapper w2 = new ObjectWrapper(obj2);
    // Different object instances may have different hashCodes
    // (not guaranteed but likely)
    assertNotEquals(0, w1.hashCode()); // just verify it returns something
    assertNotEquals(0, w2.hashCode());
  }
}
