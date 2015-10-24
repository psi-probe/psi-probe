/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package com.googlecode.psiprobe.tools;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class InstrumentsTests.
 *
 * @author Mark Lewis
 */
public class InstrumentsTests {

  /** The sun arch data model property. */
  private String sunArchDataModelProperty;

  /**
   * Forces the tests to run in 32-bit mode.
   */
  @Before
  public void setUp() {
    this.sunArchDataModelProperty = System.getProperty("sun.arch.data.model");
    System.setProperty("sun.arch.data.model", "32");
  }

  /**
   * Undoes the changes made in {@link #setUp()}.
   */
  @After
  public void tearDown() {
    System.setProperty("sun.arch.data.model", this.sunArchDataModelProperty);
  }

  /**
   * Test object.
   */
  @Test
  public void testObject() {
    Object o = new Object();
    long objectSize = Instruments.sizeOf(o);
    Assert.assertEquals(Instruments.SIZE_OBJECT, objectSize);
  }

  /**
   * Test boolean.
   */
  @Test
  public void testBoolean() {
    boolean b = false;
    long booleanSize = Instruments.sizeOf(new Boolean(b)) - Instruments.SIZE_OBJECT;
    Assert.assertEquals(Instruments.SIZE_BOOLEAN, booleanSize);
  }

  /**
   * Test byte.
   */
  @Test
  public void testByte() {
    byte b = 0x00;
    long byteSize = Instruments.sizeOf(new Byte(b)) - Instruments.SIZE_OBJECT;
    Assert.assertEquals(Instruments.SIZE_BYTE, byteSize);
  }

  /**
   * Test char.
   */
  @Test
  public void testChar() {
    char c = '\0';
    long charSize = Instruments.sizeOf(new Character(c)) - Instruments.SIZE_OBJECT;
    Assert.assertEquals(Instruments.SIZE_CHAR, charSize);
  }

  /**
   * Test short.
   */
  @Test
  public void testShort() {
    short s = 0;
    long shortSize = Instruments.sizeOf(new Short(s)) - Instruments.SIZE_OBJECT;
    Assert.assertEquals(Instruments.SIZE_SHORT, shortSize);
  }

  /**
   * Test int.
   */
  @Test
  public void testInt() {
    int i = 0;
    long intSize = Instruments.sizeOf(new Integer(i)) - Instruments.SIZE_OBJECT;
    Assert.assertEquals(Instruments.SIZE_INT, intSize);
  }

  /**
   * Test long.
   */
  @Test
  public void testLong() {
    long l = 0;
    long longSize = Instruments.sizeOf(new Long(l)) - Instruments.SIZE_OBJECT;
    Assert.assertEquals(Instruments.SIZE_LONG, longSize);
  }

  /**
   * Test float.
   */
  @Test
  public void testFloat() {
    float f = 0.0f;
    long floatSize = Instruments.sizeOf(new Float(f)) - Instruments.SIZE_OBJECT;
    Assert.assertEquals(Instruments.SIZE_FLOAT, floatSize);
  }

  /**
   * Test double.
   */
  @Test
  public void testDouble() {
    double d = 0.0;
    long doubleSize = Instruments.sizeOf(new Double(d)) - Instruments.SIZE_OBJECT;
    Assert.assertEquals(Instruments.SIZE_DOUBLE, doubleSize);
  }

}
