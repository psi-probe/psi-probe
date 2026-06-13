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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StringTokenizer}.
 */
class StringTokenizerTest {

  @Test
  void testSimpleString() throws IOException {
    StringTokenizer tokenizer = new StringTokenizer("hello world");
    assertTrue(tokenizer.hasMore());
    Token token = tokenizer.nextToken();
    assertEquals("hello world", token.getText());
    assertEquals(Tokenizer.TT_TOKEN, token.getType());
    assertFalse(tokenizer.hasMore());
  }

  @Test
  void testEmptyString() throws IOException {
    StringTokenizer tokenizer = new StringTokenizer("");
    assertFalse(tokenizer.hasMore());
  }

  @Test
  void testDefaultConstructorThenSetString() throws IOException {
    StringTokenizer tokenizer = new StringTokenizer();
    tokenizer.setString("abc");
    assertTrue(tokenizer.hasMore());
    Token token = tokenizer.nextToken();
    assertEquals("abc", token.getText());
  }

  @Test
  void testWithSymbols() throws IOException {
    StringTokenizer tokenizer = new StringTokenizer("a,b,c");
    tokenizer.addSymbol(",");
    Token t1 = tokenizer.nextToken();
    assertEquals("a", t1.getText());
    assertEquals(Tokenizer.TT_TOKEN, t1.getType());

    Token t2 = tokenizer.nextToken();
    assertEquals(",", t2.getText());
    assertEquals(Tokenizer.TT_SYMBOL, t2.getType());

    Token t3 = tokenizer.nextToken();
    assertEquals("b", t3.getText());

    Token t4 = tokenizer.nextToken();
    assertEquals(",", t4.getText());

    Token t5 = tokenizer.nextToken();
    assertEquals("c", t5.getText());

    assertFalse(tokenizer.hasMore());
  }

  @Test
  void testWithBlock() throws IOException {
    StringTokenizer tokenizer = new StringTokenizer("text[block]end");
    tokenizer.addSymbol("[", "]", false);
    Token t1 = tokenizer.nextToken();
    assertEquals("text", t1.getText());

    Token t2 = tokenizer.nextToken();
    assertEquals(Tokenizer.TT_BLOCK, t2.getType());
    assertEquals("block", t2.getInnerText());
    assertEquals("[block]", t2.getText());

    Token t3 = tokenizer.nextToken();
    assertEquals("end", t3.getText());
  }

  @Test
  void testSetStringOverridesOld() throws IOException {
    StringTokenizer tokenizer = new StringTokenizer("first");
    tokenizer.setString("second");
    Token token = tokenizer.nextToken();
    assertEquals("second", token.getText());
  }
}
