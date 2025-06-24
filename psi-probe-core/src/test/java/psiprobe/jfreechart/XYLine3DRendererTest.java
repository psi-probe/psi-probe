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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(renderer);
    oos.close();

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

}
