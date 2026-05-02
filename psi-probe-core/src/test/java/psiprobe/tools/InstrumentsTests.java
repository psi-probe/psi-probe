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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class InstrumentsTests.
 */
class InstrumentsTests {

  /**
   * Test object.
   */
  @Test
  void objectTest() {
    Object o = new Object();
    long objectSize = Instruments.sizeOf(o);
    Assertions.assertEquals(Instruments.SIZE_OBJECT, objectSize);
  }

  /**
   * Test boolean.
   */
  @Test
  void booleanTest() {
    boolean b = false;
    long booleanSize = Instruments.sizeOf(Boolean.valueOf(b)) - Instruments.SIZE_OBJECT;
    Assertions.assertEquals(Instruments.SIZE_BOOLEAN, booleanSize);
  }

  /**
   * Test byte.
   */
  @Test
  void byteTest() {
    byte b = 0x00;
    long byteSize = Instruments.sizeOf(Byte.valueOf(b)) - Instruments.SIZE_OBJECT;
    Assertions.assertEquals(Instruments.SIZE_BYTE, byteSize);
  }

  /**
   * Test char.
   */
  @Test
  void charTest() {
    char c = '\0';
    long charSize = Instruments.sizeOf(Character.valueOf(c)) - Instruments.SIZE_OBJECT;
    Assertions.assertEquals(Instruments.SIZE_CHAR, charSize);
  }

  /**
   * Test short.
   */
  @Test
  void shortTest() {
    short s = 0;
    long shortSize = Instruments.sizeOf(Short.valueOf(s)) - Instruments.SIZE_OBJECT;
    Assertions.assertEquals(Instruments.SIZE_SHORT, shortSize);
  }

  /**
   * Test int.
   */
  @Test
  void intTest() {
    int i = 0;
    long intSize = Instruments.sizeOf(Integer.valueOf(i)) - Instruments.SIZE_OBJECT;
    Assertions.assertEquals(Instruments.SIZE_INT, intSize);
  }

  /**
   * Test long.
   */
  @Test
  void longTest() {
    long l = 0;
    long longSize = Instruments.sizeOf(Long.valueOf(l)) - Instruments.SIZE_OBJECT;
    Assertions.assertEquals(Instruments.SIZE_LONG, longSize);
  }

  /**
   * Test float.
   */
  @Test
  void floatTest() {
    float f = 0.0F;
    long floatSize = Instruments.sizeOf(Float.valueOf(f)) - Instruments.SIZE_OBJECT;
    Assertions.assertEquals(Instruments.SIZE_FLOAT, floatSize);
  }

  /**
   * Test double.
   */
  @Test
  void doubleTest() {
    double d = 0.0;
    long doubleSize = Instruments.sizeOf(Double.valueOf(d)) - Instruments.SIZE_OBJECT;
    Assertions.assertEquals(Instruments.SIZE_DOUBLE, doubleSize);
  }

  @Test
  void testSizeOfNull() {
    // sizeOf(null) should return 0 (null is skipped)
    long size = Instruments.sizeOf(null);
    assertEquals(0L, size);
  }

  @Test
  void testSizeOfWithClassLoader() {
    Object o = new Object();
    long size = Instruments.sizeOf(o, o.getClass().getClassLoader());
    assertTrue(size >= 0);
  }

  @Test
  void testSizeOfWithPreexistingSet() {
    Object o = new Object();
    long size = Instruments.sizeOf(o, new HashSet<>());
    assertTrue(size >= 0);
  }

  @Test
  void testIsInitialized() {
    assertTrue(Instruments.isInitialized());
  }

  @Test
  void testGetFieldExisting() {
    // Use our own class with accessible field
    class Container {
      String value = "hello";
    }
    Container c = new Container();
    Object val = Instruments.getField(c, "value");
    assertEquals("hello", val);
  }

  @Test
  void testGetFieldNonExistent() {
    Object o = new Object();
    Object val = Instruments.getField(o, "nonExistentField");
    assertNull(val);
  }

  @Test
  void testFindFieldInSuperclass() {
    // Create an object where the field is in the superclass
    class Parent {
      String parentField = "parent";
    }
    class Child extends Parent {
      String childField = "child";
    }
    Child child = new Child();
    Object val = Instruments.getField(child, "parentField");
    assertEquals("parent", val);
  }

  @Test
  void testSizeOfPrimitiveIntArray() {
    int[] arr = {1, 2, 3, 4, 5};
    long size = Instruments.sizeOf(arr);
    // primitive int array: 5 * SIZE_INT
    assertEquals(5 * Instruments.SIZE_INT, size);
  }

  @Test
  void testSizeOfBooleanArray() {
    boolean[] arr = {true, false, true};
    long size = Instruments.sizeOf(arr);
    assertEquals(3 * Instruments.SIZE_BOOLEAN, size);
  }

  @Test
  void testSizeReferenceIsPositive() {
    assertTrue(Instruments.SIZE_REFERENCE > 0);
  }

  @Test
  void testSizeOfComplexObject() {
    // A simple object with primitive fields
    class SimpleData {
      int x = 42;
      long y = 100L;
    }
    SimpleData data = new SimpleData();
    long size = Instruments.sizeOf(data);
    // At minimum should include SIZE_OBJECT + primitives
    assertTrue(size >= Instruments.SIZE_OBJECT + Instruments.SIZE_INT + Instruments.SIZE_LONG);
  }
}
