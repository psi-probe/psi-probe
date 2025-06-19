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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenizerTest {

  private Tokenizer tokenizer;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    tokenizer = new Tokenizer();
  }

  @Test
  void testTokenizeSimpleString() throws IOException {
    String input = "hello world";
    tokenizer.setReader(new StringReader(input));
    Token token1 = tokenizer.nextToken();
    assertEquals("hello world", token1.getText());
    assertEquals(Tokenizer.TT_TOKEN, token1.getType());
    assertFalse(tokenizer.hasMore());
  }

  @Test
  void testAddAndDetectSymbol() throws IOException {
    tokenizer.addSymbol(";");
    tokenizer.setReader(new StringReader("abc;def"));
    Token token1 = tokenizer.nextToken();
    assertEquals("abc", token1.getText());
    assertEquals(Tokenizer.TT_TOKEN, token1.getType());

    Token token2 = tokenizer.nextToken();
    assertEquals(";", token2.getText());
    assertEquals(Tokenizer.TT_SYMBOL, token2.getType());
    assertNull(token2.getName());

    Token token3 = tokenizer.nextToken();
    assertEquals("def", token3.getText());
    assertEquals(Tokenizer.TT_TOKEN, token3.getType());
  }

  @Test
  void testBlockSymbol() throws IOException {
    tokenizer.addSymbol("/*", "*/", false);
    tokenizer.setReader(new StringReader("abc/*block*/def"));
    Token token1 = tokenizer.nextToken();
    assertEquals("abc", token1.getText());
    assertEquals(Tokenizer.TT_TOKEN, token1.getType());

    Token token2 = tokenizer.nextToken();
    assertEquals("/*block*/", token2.getText());
    assertEquals("block", token2.getInnerText());
    assertEquals(Tokenizer.TT_BLOCK, token2.getType());

    Token token3 = tokenizer.nextToken();
    assertEquals("def", token3.getText());
    assertEquals(Tokenizer.TT_TOKEN, token3.getType());
  }

  @Test
  void testGetNextStringAndLong() throws IOException {
    tokenizer.setReader(new StringReader("12345"));
    assertEquals("12345", tokenizer.getNextString("default"));
    tokenizer.setReader(new StringReader("notanumber"));
    assertEquals(42L, tokenizer.getNextLong(42L));
  }

  @Test
  void testGetNextBoolean() throws IOException {
    tokenizer.setReader(new StringReader("yes"));
    assertTrue(tokenizer.getNextBoolean("yes", false));
    tokenizer.setReader(new StringReader("no"));
    assertFalse(tokenizer.getNextBoolean("yes", false));
  }

  @Test
  void testPushBack() throws IOException {
    tokenizer.setReader(new StringReader("foo bar"));
    Token token1 = tokenizer.nextToken();
    assertEquals("foo bar", token1.getText());
    tokenizer.pushBack();
    Token token2 = tokenizer.nextToken();
    assertEquals("foo bar", token2.getText());
  }
}

