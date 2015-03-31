/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.tools;

import java.util.Locale;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author Mark Lewis
 */
public class SizeExpressionTests extends TestCase {
    
    private Locale defaultLocale;

    protected void setUp() throws Exception {
        this.defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    protected void tearDown() throws Exception {
        Locale.setDefault(defaultLocale);
    }
    
    public void testFormatNoDecimalBase2() {
        Assert.assertEquals("1 B", SizeExpression.format(1, 0, true));
        Assert.assertEquals("10 B", SizeExpression.format(10, 0, true));
        Assert.assertEquals("100 B", SizeExpression.format(100, 0, true));
        Assert.assertEquals("1,000 B", SizeExpression.format(1000, 0, true));
        Assert.assertEquals("1,023 B", SizeExpression.format(1023, 0, true));
        Assert.assertEquals("1 KB", SizeExpression.format(1024, 0, true));
        Assert.assertEquals("10 KB", SizeExpression.format(10240, 0, true));
        Assert.assertEquals("10 KB", SizeExpression.format(10250, 0, true));
    }
    
    public void testFormatNoDecimalBase10() {
        Assert.assertEquals("1", SizeExpression.format(1, 0, false));
        Assert.assertEquals("10", SizeExpression.format(10, 0, false));
        Assert.assertEquals("100", SizeExpression.format(100, 0, false));
        Assert.assertEquals("1K", SizeExpression.format(1000, 0, false));
        Assert.assertEquals("1K", SizeExpression.format(1023, 0, false));
        Assert.assertEquals("1K", SizeExpression.format(1024, 0, false));
        Assert.assertEquals("10K", SizeExpression.format(10240, 0, false));
        Assert.assertEquals("10K", SizeExpression.format(10250, 0, false));
    }
    
    public void testFormatOneDecimalBase2() {
        Assert.assertEquals("1 B", SizeExpression.format(1, 1, true));
        Assert.assertEquals("10 B", SizeExpression.format(10, 1, true));
        Assert.assertEquals("100 B", SizeExpression.format(100, 1, true));
        Assert.assertEquals("1,000 B", SizeExpression.format(1000, 1, true));
        Assert.assertEquals("1,023 B", SizeExpression.format(1023, 1, true));
        Assert.assertEquals("1.0 KB", SizeExpression.format(1024, 1, true));
        Assert.assertEquals("10.0 KB", SizeExpression.format(10240, 1, true));
        Assert.assertEquals("10.0 KB", SizeExpression.format(10250, 1, true));
    }
    
    public void testFormatOneDecimalBase10() {
        Assert.assertEquals("1", SizeExpression.format(1, 1, false));
        Assert.assertEquals("10", SizeExpression.format(10, 1, false));
        Assert.assertEquals("100", SizeExpression.format(100, 1, false));
        Assert.assertEquals("1.0K", SizeExpression.format(1000, 1, false));
        Assert.assertEquals("1.0K", SizeExpression.format(1023, 1, false));
        Assert.assertEquals("1.0K", SizeExpression.format(1024, 1, false));
        Assert.assertEquals("10.2K", SizeExpression.format(10240, 1, false));
        Assert.assertEquals("10.3K", SizeExpression.format(10250, 1, false));
    }
    
    public void testFormatAllPrefixesBase2() {
        Assert.assertEquals("1 B", SizeExpression.format(1, 0, true));
        Assert.assertEquals("1 KB", SizeExpression.format(1024, 0, true));
        Assert.assertEquals("1 MB", SizeExpression.format(1048576, 0, true));
        Assert.assertEquals("1 GB", SizeExpression.format(1073741824, 0, true));
        Assert.assertEquals("1 TB", SizeExpression.format(1099511627776L, 0, true));
        Assert.assertEquals("1 PB", SizeExpression.format(1125899906842624L, 0, true));
    }
    
    public void testFormatAllPrefixesBase10() {
        Assert.assertEquals("1", SizeExpression.format(1, 0, false));
        Assert.assertEquals("1K", SizeExpression.format(1000, 0, false));
        Assert.assertEquals("1M", SizeExpression.format(1000000, 0, false));
        Assert.assertEquals("1G", SizeExpression.format(1000000000, 0, false));
        Assert.assertEquals("1T", SizeExpression.format(1000000000000L, 0, false));
        Assert.assertEquals("1P", SizeExpression.format(1000000000000000L, 0, false));
    }
    
    public void testParseWithUnit() {
        Assert.assertEquals(1, SizeExpression.parse("1B"));
        Assert.assertEquals(10, SizeExpression.parse("10B"));
        Assert.assertEquals(100, SizeExpression.parse("100B"));
        Assert.assertEquals(1000, SizeExpression.parse("1000B"));
        Assert.assertEquals(1023, SizeExpression.parse("1023B"));
        Assert.assertEquals(1024, SizeExpression.parse("1024B"));
        Assert.assertEquals(1024, SizeExpression.parse("1.0KB"));
        Assert.assertEquals(1024, SizeExpression.parse("1KB"));
        Assert.assertEquals(1048576, SizeExpression.parse("1MB"));
        Assert.assertEquals(1073741824, SizeExpression.parse("1GB"));
        Assert.assertEquals(1099511627776L, SizeExpression.parse("1TB"));
        Assert.assertEquals(1125899906842624L, SizeExpression.parse("1PB"));
    }
    
    public void testParseWithoutUnit() {
        Assert.assertEquals(1, SizeExpression.parse("1"));
        Assert.assertEquals(10, SizeExpression.parse("10"));
        Assert.assertEquals(100, SizeExpression.parse("100"));
        Assert.assertEquals(1000, SizeExpression.parse("1000"));
        Assert.assertEquals(1023, SizeExpression.parse("1023"));
        Assert.assertEquals(1024, SizeExpression.parse("1024"));
        Assert.assertEquals(1000, SizeExpression.parse("1.0K"));
        Assert.assertEquals(1000, SizeExpression.parse("1K"));
        Assert.assertEquals(1000000, SizeExpression.parse("1M"));
        Assert.assertEquals(1000000000, SizeExpression.parse("1G"));
        Assert.assertEquals(1000000000000L, SizeExpression.parse("1T"));
        Assert.assertEquals(1000000000000000L, SizeExpression.parse("1P"));
    }
    
}
