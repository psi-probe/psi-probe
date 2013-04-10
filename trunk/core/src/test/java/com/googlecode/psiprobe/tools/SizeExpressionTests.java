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

import junit.framework.Assert;

/**
 *
 * @author Mark Lewis
 */
public class SizeExpressionTests {
    
    public void testFormatNoDecimal() {
        Assert.assertEquals(SizeExpression.format(1, 0), "1 B");
        Assert.assertEquals(SizeExpression.format(10, 0), "10 B");
        Assert.assertEquals(SizeExpression.format(100, 0), "100 B");
        Assert.assertEquals(SizeExpression.format(1000, 0), "1,000 B");
        Assert.assertEquals(SizeExpression.format(1023, 0), "1,023 B");
        Assert.assertEquals(SizeExpression.format(1024, 0), "1 KB");
        Assert.assertEquals(SizeExpression.format(10240, 0), "10 KB");
        Assert.assertEquals(SizeExpression.format(10250, 0), "10 KB");
    }
    
    public void testFormatOneDecimal() {
        Assert.assertEquals(SizeExpression.format(1, 1), "1 B");
        Assert.assertEquals(SizeExpression.format(10, 1), "10 B");
        Assert.assertEquals(SizeExpression.format(100, 1), "100 B");
        Assert.assertEquals(SizeExpression.format(1000, 1), "1,000 B");
        Assert.assertEquals(SizeExpression.format(1023, 1), "1,023 B");
        Assert.assertEquals(SizeExpression.format(1024, 1), "1.0 KB");
        Assert.assertEquals(SizeExpression.format(10240, 1), "10.0 KB");
        Assert.assertEquals(SizeExpression.format(10250, 1), "10.0 KB");
    }
    
    public void testFormatOneDecimalBase10() {
        Assert.assertEquals(SizeExpression.format(1, 1, false), "1");
        Assert.assertEquals(SizeExpression.format(10, 1, false), "10");
        Assert.assertEquals(SizeExpression.format(100, 1, false), "100");
        Assert.assertEquals(SizeExpression.format(1000, 1, false), "1.0K");
        Assert.assertEquals(SizeExpression.format(1023, 1, false), "1.0K");
        Assert.assertEquals(SizeExpression.format(1024, 1, false), "1.0K");
        Assert.assertEquals(SizeExpression.format(10240, 1, false), "10.0K");
        Assert.assertEquals(SizeExpression.format(10250, 1, false), "10.0K");
    }
    
    public void testFormatAllPrefixes() {
        Assert.assertEquals(SizeExpression.format(1, 0), "1 B");
        Assert.assertEquals(SizeExpression.format(1024, 0), "1 KB");
        Assert.assertEquals(SizeExpression.format(1048576, 0), "1 MB");
        Assert.assertEquals(SizeExpression.format(1073741824, 0), "1 GB");
        Assert.assertEquals(SizeExpression.format(1099511627776L, 0), "1 TB");
        Assert.assertEquals(SizeExpression.format(1125899906842624L, 0), "1 PB");
    }
    
    public void testFormatAllPrefixesBase10() {
        Assert.assertEquals(SizeExpression.format(1, 0, false), "1");
        Assert.assertEquals(SizeExpression.format(1000, 0, false), "1K");
        Assert.assertEquals(SizeExpression.format(1000000, 0, false), "1M");
        Assert.assertEquals(SizeExpression.format(1000000000, 0, false), "1G");
        Assert.assertEquals(SizeExpression.format(1000000000000L, 0, false), "1T");
        Assert.assertEquals(SizeExpression.format(1000000000000000L, 0, false), "1P");
    }
    
    public void testParseWithUnit() {
        Assert.assertEquals(SizeExpression.parse("1B"), 1);
        Assert.assertEquals(SizeExpression.parse("10B"), 10);
        Assert.assertEquals(SizeExpression.parse("100B"), 100);
        Assert.assertEquals(SizeExpression.parse("1000B"), 1000);
        Assert.assertEquals(SizeExpression.parse("1023B"), 1023);
        Assert.assertEquals(SizeExpression.parse("1024B"), 1024);
        Assert.assertEquals(SizeExpression.parse("1.0KB"), 1024);
        Assert.assertEquals(SizeExpression.parse("1KB"), 1024);
        Assert.assertEquals(SizeExpression.parse("1MB"), 1048576);
        Assert.assertEquals(SizeExpression.parse("1GB"), 1073741824);
        Assert.assertEquals(SizeExpression.parse("1TB"), 1099511627776L);
        Assert.assertEquals(SizeExpression.parse("1PB"), 1125899906842624L);
    }
    
    public void testParseWithoutUnit() {
        Assert.assertEquals(SizeExpression.parse("1"), 1);
        Assert.assertEquals(SizeExpression.parse("10"), 10);
        Assert.assertEquals(SizeExpression.parse("100"), 100);
        Assert.assertEquals(SizeExpression.parse("1000"), 1000);
        Assert.assertEquals(SizeExpression.parse("1023"), 1023);
        Assert.assertEquals(SizeExpression.parse("1024"), 1024);
        Assert.assertEquals(SizeExpression.parse("1.0K"), 1000);
        Assert.assertEquals(SizeExpression.parse("1K"), 1000);
        Assert.assertEquals(SizeExpression.parse("1M"), 1000000);
        Assert.assertEquals(SizeExpression.parse("1G"), 1000000000);
        Assert.assertEquals(SizeExpression.parse("1T"), 1000000000000L);
        Assert.assertEquals(SizeExpression.parse("1P"), 1000000000000000L);
    }
    
}
