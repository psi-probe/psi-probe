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
package psiprobe.tokenizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TokenizerSymbol}.
 */
class TokenizerSymbolTest {

  @Test
  void testEqualsAndHashCode() {
    TokenizerSymbol s1 = new TokenizerSymbol("name", "start", "tail", false, false, true, false);
    TokenizerSymbol s2 = new TokenizerSymbol("name", "start", "tail", false, false, true, false);
    TokenizerSymbol s3 = new TokenizerSymbol("other", "start", "tail", false, false, true, false);

    assertEquals(s1, s2);
    assertEquals(s1.hashCode(), s2.hashCode());
    assertNotEquals(s1, s3);
  }

  @Test
  void testEqualsNull() {
    TokenizerSymbol s = new TokenizerSymbol("name", "start", "tail", false, false, true, false);
    assertNotEquals(s, null);
  }

  @Test
  void testEqualsWrongType() {
    TokenizerSymbol s = new TokenizerSymbol("name", "start", "tail", false, false, true, false);
    assertNotEquals(s, "string");
  }

  @Test
  void testCompareToCharacter() {
    TokenizerSymbol s = new TokenizerSymbol("name", "a", "tail", false, false, true, false);
    // compareTo(char) = chr - startText.charAt(0) = 'b' - 'a' = 1
    assertEquals(1, s.compareTo(Character.valueOf('b')));
    assertEquals(0, s.compareTo(Character.valueOf('a')));
    assertEquals(-1, s.compareTo(Character.valueOf((char) ('a' - 1))));
  }

  @Test
  void testCompareToSymbol() {
    // compareTo(symbol) = symbol.startText.compareTo(startText)
    TokenizerSymbol s1 = new TokenizerSymbol("n1", "abc", "tail", false, false, true, false);
    TokenizerSymbol s2 = new TokenizerSymbol("n2", "xyz", "tail", false, false, true, false);
    // s1.compareTo(s2) = "xyz".compareTo("abc") > 0
    assertTrue(s1.compareTo(s2) > 0);
    // s2.compareTo(s1) = "abc".compareTo("xyz") < 0
    assertTrue(s2.compareTo(s1) < 0);
    // s1.compareTo(s1) = 0
    assertEquals(0, s1.compareTo(s1));
  }

  @Test
  void testCompareToDispatch() {
    TokenizerSymbol s = new TokenizerSymbol("name", "a", "tail", false, false, true, false);
    Object charObj = Character.valueOf('b');
    assertEquals(1, s.compareTo(charObj));
  }

  @Test
  void testEqualsDifferentHidden() {
    TokenizerSymbol s1 = new TokenizerSymbol("name", "start", "tail", false, false, true, false);
    TokenizerSymbol s2 = new TokenizerSymbol("name", "start", "tail", true, false, true, false);
    assertNotEquals(s1, s2);
  }

  @Test
  void testEqualsDifferentDecodePaired() {
    TokenizerSymbol s1 = new TokenizerSymbol("name", "start", "tail", false, false, true, false);
    TokenizerSymbol s2 = new TokenizerSymbol("name", "start", "tail", false, true, true, false);
    assertNotEquals(s1, s2);
  }

  @Test
  void testEqualsDifferentEnabled() {
    TokenizerSymbol s1 = new TokenizerSymbol("name", "start", "tail", false, false, true, false);
    TokenizerSymbol s2 = new TokenizerSymbol("name", "start", "tail", false, false, false, false);
    assertNotEquals(s1, s2);
  }

  @Test
  void testEqualsDifferentCanBeNested() {
    TokenizerSymbol s1 = new TokenizerSymbol("name", "start", "tail", false, false, true, false);
    TokenizerSymbol s2 = new TokenizerSymbol("name", "start", "tail", false, false, true, true);
    assertNotEquals(s1, s2);
  }

  @Test
  void testEqualsDifferentTail() {
    TokenizerSymbol s1 = new TokenizerSymbol("name", "start", "tail1", false, false, true, false);
    TokenizerSymbol s2 = new TokenizerSymbol("name", "start", "tail2", false, false, true, false);
    assertNotEquals(s1, s2);
  }
}
