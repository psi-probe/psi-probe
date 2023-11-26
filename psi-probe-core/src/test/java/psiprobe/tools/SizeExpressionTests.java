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

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class SizeExpressionTests.
 */
class SizeExpressionTests {

  /** The default locale. */
  private Locale defaultLocale;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    this.defaultLocale = Locale.getDefault();
    Locale.setDefault(Locale.US);
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    Locale.setDefault(defaultLocale);
  }

  /**
   * Test format no decimal base2.
   */
  @Test
  void testFormatNoDecimalBase2() {
    Assertions.assertEquals("1 B", SizeExpression.format(1, 0, true));
    Assertions.assertEquals("10 B", SizeExpression.format(10, 0, true));
    Assertions.assertEquals("100 B", SizeExpression.format(100, 0, true));
    Assertions.assertEquals("1,000 B", SizeExpression.format(1000, 0, true));
    Assertions.assertEquals("1,023 B", SizeExpression.format(1023, 0, true));
    Assertions.assertEquals("1 KB", SizeExpression.format(1024, 0, true));
    Assertions.assertEquals("10 KB", SizeExpression.format(10240, 0, true));
    Assertions.assertEquals("10 KB", SizeExpression.format(10250, 0, true));
  }

  /**
   * Test format no decimal base10.
   */
  @Test
  void testFormatNoDecimalBase10() {
    Assertions.assertEquals("1", SizeExpression.format(1, 0, false));
    Assertions.assertEquals("10", SizeExpression.format(10, 0, false));
    Assertions.assertEquals("100", SizeExpression.format(100, 0, false));
    Assertions.assertEquals("1K", SizeExpression.format(1000, 0, false));
    Assertions.assertEquals("1K", SizeExpression.format(1023, 0, false));
    Assertions.assertEquals("1K", SizeExpression.format(1024, 0, false));
    Assertions.assertEquals("10K", SizeExpression.format(10240, 0, false));
    Assertions.assertEquals("10K", SizeExpression.format(10250, 0, false));
  }

  /**
   * Test format one decimal base2.
   */
  @Test
  void testFormatOneDecimalBase2() {
    Assertions.assertEquals("1 B", SizeExpression.format(1, 1, true));
    Assertions.assertEquals("10 B", SizeExpression.format(10, 1, true));
    Assertions.assertEquals("100 B", SizeExpression.format(100, 1, true));
    Assertions.assertEquals("1,000 B", SizeExpression.format(1000, 1, true));
    Assertions.assertEquals("1,023 B", SizeExpression.format(1023, 1, true));
    Assertions.assertEquals("1.0 KB", SizeExpression.format(1024, 1, true));
    Assertions.assertEquals("10.0 KB", SizeExpression.format(10240, 1, true));
    Assertions.assertEquals("10.0 KB", SizeExpression.format(10250, 1, true));
  }

  /**
   * Test format one decimal base10.
   */
  @Test
  void testFormatOneDecimalBase10() {
    Assertions.assertEquals("1", SizeExpression.format(1, 1, false));
    Assertions.assertEquals("10", SizeExpression.format(10, 1, false));
    Assertions.assertEquals("100", SizeExpression.format(100, 1, false));
    Assertions.assertEquals("1.0K", SizeExpression.format(1000, 1, false));
    Assertions.assertEquals("1.0K", SizeExpression.format(1023, 1, false));
    Assertions.assertEquals("1.0K", SizeExpression.format(1024, 1, false));
    Assertions.assertEquals("10.2K", SizeExpression.format(10240, 1, false));
    Assertions.assertEquals("10.3K", SizeExpression.format(10250, 1, false));
  }

  /**
   * Test format all prefixes base2.
   */
  @Test
  void testFormatAllPrefixesBase2() {
    Assertions.assertEquals("1 B", SizeExpression.format(1, 0, true));
    Assertions.assertEquals("1 KB", SizeExpression.format(1024, 0, true));
    Assertions.assertEquals("1 MB", SizeExpression.format(1048576, 0, true));
    Assertions.assertEquals("1 GB", SizeExpression.format(1073741824, 0, true));
    Assertions.assertEquals("1 TB", SizeExpression.format(1099511627776L, 0, true));
    Assertions.assertEquals("1 PB", SizeExpression.format(1125899906842624L, 0, true));
  }

  /**
   * Test format all prefixes base10.
   */
  @Test
  void testFormatAllPrefixesBase10() {
    Assertions.assertEquals("1", SizeExpression.format(1, 0, false));
    Assertions.assertEquals("1K", SizeExpression.format(1000, 0, false));
    Assertions.assertEquals("1M", SizeExpression.format(1000000, 0, false));
    Assertions.assertEquals("1G", SizeExpression.format(1000000000, 0, false));
    Assertions.assertEquals("1T", SizeExpression.format(1000000000000L, 0, false));
    Assertions.assertEquals("1P", SizeExpression.format(1000000000000000L, 0, false));
  }

  /**
   * Test parse with unit.
   */
  @Test
  void testParseWithUnit() {
    Assertions.assertEquals(1, SizeExpression.parse("1B"));
    Assertions.assertEquals(10, SizeExpression.parse("10B"));
    Assertions.assertEquals(100, SizeExpression.parse("100B"));
    Assertions.assertEquals(1000, SizeExpression.parse("1000B"));
    Assertions.assertEquals(1023, SizeExpression.parse("1023B"));
    Assertions.assertEquals(1024, SizeExpression.parse("1024B"));
    Assertions.assertEquals(1024, SizeExpression.parse("1.0KB"));
    Assertions.assertEquals(1024, SizeExpression.parse("1KB"));
    Assertions.assertEquals(1048576, SizeExpression.parse("1MB"));
    Assertions.assertEquals(1073741824, SizeExpression.parse("1GB"));
    Assertions.assertEquals(1099511627776L, SizeExpression.parse("1TB"));
    Assertions.assertEquals(1125899906842624L, SizeExpression.parse("1PB"));
  }

  /**
   * Test parse without unit.
   */
  @Test
  void testParseWithoutUnit() {
    Assertions.assertEquals(1, SizeExpression.parse("1"));
    Assertions.assertEquals(10, SizeExpression.parse("10"));
    Assertions.assertEquals(100, SizeExpression.parse("100"));
    Assertions.assertEquals(1000, SizeExpression.parse("1000"));
    Assertions.assertEquals(1023, SizeExpression.parse("1023"));
    Assertions.assertEquals(1024, SizeExpression.parse("1024"));
    Assertions.assertEquals(1000, SizeExpression.parse("1.0K"));
    Assertions.assertEquals(1000, SizeExpression.parse("1K"));
    Assertions.assertEquals(1000000, SizeExpression.parse("1M"));
    Assertions.assertEquals(1000000000, SizeExpression.parse("1G"));
    Assertions.assertEquals(1000000000000L, SizeExpression.parse("1T"));
    Assertions.assertEquals(1000000000000000L, SizeExpression.parse("1P"));
  }

}
