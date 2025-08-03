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
package psiprobe.jfreechart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

/**
 * The Class XYLine3DRendererTest.
 */
class XYLine3DRendererTest {

  /**
   * Test default values.
   */
  @Test
  void testDefaultValues() {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    assertEquals(XYLine3DRenderer.DEFAULT_X_OFFSET, renderer.getXOffset());
    assertEquals(XYLine3DRenderer.DEFAULT_Y_OFFSET, renderer.getYOffset());
    assertEquals(XYLine3DRenderer.DEFAULT_WALL_PAINT, renderer.getWallPaint());
    assertEquals(3, renderer.getPassCount());
  }

  /**
   * Test setters and getters.
   */
  @Test
  void testSettersAndGetters() {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    renderer.setXOffset(20.0);
    renderer.setYOffset(15.0);
    Paint paint = Color.BLUE;
    renderer.setWallPaint(paint);

    assertEquals(20.0, renderer.getXOffset());
    assertEquals(15.0, renderer.getYOffset());
    assertEquals(paint, renderer.getWallPaint());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    XYLine3DRenderer r1 = new XYLine3DRenderer();
    XYLine3DRenderer r2 = new XYLine3DRenderer();
    assertEquals(r1, r2);

    r1.setXOffset(99.0);
    assertNotEquals(r1, r2);
    r2.setXOffset(99.0);
    assertEquals(r1, r2);

    r1.setWallPaint(Color.RED);
    assertNotEquals(r1, r2);
    r2.setWallPaint(Color.RED);
    assertEquals(r1, r2);
  }

  /**
   * Test serialization.
   *
   * @throws Exception the exception
   */
  @Test
  void testSerialization() throws Exception {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    renderer.setXOffset(5.5);
    renderer.setYOffset(7.7);
    renderer.setWallPaint(Color.GREEN);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(renderer);
    }

    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
    XYLine3DRenderer deserialized = (XYLine3DRenderer) ois.readObject();

    assertEquals(renderer, deserialized);
    assertEquals(Color.GREEN, deserialized.getWallPaint());
  }

  /**
   * Test draw first pass shape shadow pass.
   */
  @Test
  void testDrawFirstPassShape_ShadowPass() {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    Graphics2D g2 = mock(Graphics2D.class);
    Shape shape = mock(Shape.class);

    renderer.setWallPaint(Color.BLACK);
    renderer.drawFirstPassShape(g2, 0, 0, 0, shape);

    verify(g2).setPaint(Color.BLACK);
    verify(g2, times(1)).draw(shape);
  }

  /**
   * Test draw first pass shape non shadow pass.
   */
  @Test
  void testDrawFirstPassShape_NonShadowPass() {
    XYLine3DRenderer renderer = spy(new XYLine3DRenderer());
    Graphics2D g2 = mock(Graphics2D.class);
    Shape shape = mock(Shape.class);

    renderer.drawFirstPassShape(g2, 2, 0, 0, shape);
    verify(renderer, times(1)).drawFirstPassShape(g2, 2, 0, 0, shape);
  }

  /**
   * Test hash code consistency.
   */
  @Test
  void testHashCodeConsistency() {
    XYLine3DRenderer r1 = new XYLine3DRenderer();
    XYLine3DRenderer r2 = new XYLine3DRenderer();
    assertEquals(r1.hashCode(), r2.hashCode());

    r1.setXOffset(42.0);
    assertNotEquals(r1.hashCode(), r2.hashCode());
    r2.setXOffset(42.0);
    assertEquals(r1.hashCode(), r2.hashCode());
  }

  /**
   * Test clone.
   *
   * @throws Exception the exception
   */
  @Test
  void testClone() throws Exception {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    renderer.setXOffset(1.1);
    renderer.setYOffset(2.2);
    renderer.setWallPaint(Color.MAGENTA);

    XYLine3DRenderer clone = (XYLine3DRenderer) renderer.clone();
    assertEquals(renderer, clone);
    assertEquals(renderer.getWallPaint(), clone.getWallPaint());
    assertNotEquals(System.identityHashCode(renderer), System.identityHashCode(clone));
  }

  /**
   * Test set wall paint null.
   */
  @Test
  void testSetWallPaintNull() {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    renderer.setWallPaint(null);
    assertEquals(null, renderer.getWallPaint());
  }

  /**
   * Test set negative offsets.
   */
  @Test
  void testSetNegativeOffsets() {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    renderer.setXOffset(-10.0);
    renderer.setYOffset(-5.0);
    assertEquals(-10.0, renderer.getXOffset());
    assertEquals(-5.0, renderer.getYOffset());
  }

  /**
   * Test equals with null and other type.
   */
  @Test
  void testEqualsWithNullAndOtherType() {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    assertNotEquals(renderer, null);
    assertNotEquals(renderer, "not a renderer");
  }

  /**
   * Test equals different Y offset.
   */
  @Test
  void testEqualsDifferentYOffset() {
    XYLine3DRenderer r1 = new XYLine3DRenderer();
    XYLine3DRenderer r2 = new XYLine3DRenderer();
    r1.setYOffset(10.0);
    assertNotEquals(r1, r2);
    r2.setYOffset(10.0);
    assertEquals(r1, r2);
  }

  /**
   * Test equals different wall paint.
   */
  @Test
  void testEqualsDifferentWallPaint() {
    XYLine3DRenderer r1 = new XYLine3DRenderer();
    XYLine3DRenderer r2 = new XYLine3DRenderer();
    r1.setWallPaint(Color.BLUE);
    r2.setWallPaint(Color.RED);
    assertNotEquals(r1, r2);
  }

  /**
   * Test serialization with null wall paint.
   *
   * @throws Exception the exception
   */
  @Test
  void testSerializationWithNullWallPaint() throws Exception {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    renderer.setWallPaint(null);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(renderer);
    }

    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
    XYLine3DRenderer deserialized = (XYLine3DRenderer) ois.readObject();

    assertEquals(renderer, deserialized);
    assertEquals(null, deserialized.getWallPaint());
  }

  /**
   * Test default constructor fields.
   */
  @Test
  void testDefaultConstructorFields() {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    assertEquals(XYLine3DRenderer.DEFAULT_X_OFFSET, renderer.getXOffset());
    assertEquals(XYLine3DRenderer.DEFAULT_Y_OFFSET, renderer.getYOffset());
    assertEquals(XYLine3DRenderer.DEFAULT_WALL_PAINT, renderer.getWallPaint());
  }

  /**
   * Test is item pass.
   */
  @Test
  void testIsItemPass() {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    // Pass 0 is shadow, pass 1 and 2 are normal
    assertFalse(renderer.isItemPass(1));
    assertTrue(renderer.isItemPass(2));
    assertFalse(renderer.isItemPass(0));
  }

  /**
   * Test is line pass.
   *
   * @throws Exception the exception
   */
  @Test
  void testIsLinePass() throws Exception {
    XYLine3DRenderer renderer = new XYLine3DRenderer();
    var method = XYLine3DRenderer.class.getDeclaredMethod("isLinePass", int.class);
    method.setAccessible(true);

    // pass 0 and 1 should return true, others false
    assertTrue((Boolean) method.invoke(renderer, 0));
    assertTrue((Boolean) method.invoke(renderer, 1));
    assertFalse((Boolean) method.invoke(renderer, 2));
    assertFalse((Boolean) method.invoke(renderer, -1));
    assertFalse((Boolean) method.invoke(renderer, 3));
  }

}
