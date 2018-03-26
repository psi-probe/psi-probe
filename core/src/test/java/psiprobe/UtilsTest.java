/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class UtilsTest.
 */
public class UtilsTest {

  /**
   * To int test.
   */
  @Test
  public void toIntTest() {
    Assert.assertEquals(5, Utils.toInt("garbage", 5));
    Assert.assertEquals(3, Utils.toInt("3", 5));
    Assert.assertEquals(5, Utils.toInt("3 3", 5));
    Assert.assertEquals(5, Utils.toInt((String) null, 5));
  }

  /**
   * To int hex test.
   */
  @Test
  public void toIntHexTest() {
    Assert.assertEquals(5, Utils.toIntHex("garbage", 5));
    Assert.assertEquals(3, Utils.toIntHex("3", 5));
    Assert.assertEquals(3, Utils.toIntHex("#3", 5));
    Assert.assertEquals(5, Utils.toIntHex("3 3", 5));
    Assert.assertEquals(5, Utils.toIntHex((String) null, 5));
  }

  /**
   * To long test.
   */
  @Test
  public void toLongTest() {
    Assert.assertEquals(5L, Utils.toLong("garbage", 5L));
    Assert.assertEquals(3L, Utils.toLong("3", 5L));
    Assert.assertEquals(5L, Utils.toLong("3 3", 5L));
    Assert.assertEquals(5L, Utils.toLong((String) null, 5L));
  }

  /**
   * To long int test.
   */
  @Test
  public void toLongIntTest() {
    Assert.assertEquals(5, Utils.toLong((Long) null, 5));
    Assert.assertEquals(1, Utils.toLong(Long.valueOf(1), 5));
  }

  /**
   * To float test.
   */
  @Test
  public void toFloatTest() {
    Assert.assertEquals(5.0f, Utils.toFloat("garbage", 5.0f), 0.0);
    Assert.assertEquals(3.0f, Utils.toFloat("3", 5.0f), 0.0);
    Assert.assertEquals(5.0f, Utils.toFloat("3 3", 5.0f), 0.0);
    Assert.assertEquals(5.0f, Utils.toFloat((String) null, 5.0f), 0.0);
  }

  /**
   * Left pad test.
   */
  @Test
  public void leftPadTest() {
    Assert.assertEquals("0005", Utils.leftPad("5", 4, "0"));
    Assert.assertEquals("5", Utils.leftPad("5", 1, "0"));
    Assert.assertEquals("", Utils.leftPad(null, 4, "0"));
  }

}
