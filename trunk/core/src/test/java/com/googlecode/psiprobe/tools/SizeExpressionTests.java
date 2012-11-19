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
    
    public void testRoundedExpressionNoDecimal() {
        Assert.assertEquals(SizeExpression.roundedExpression(1, 0), "1 B");
        Assert.assertEquals(SizeExpression.roundedExpression(10, 0), "10 B");
        Assert.assertEquals(SizeExpression.roundedExpression(100, 0), "100 B");
        Assert.assertEquals(SizeExpression.roundedExpression(1000, 0), "1,000 B");
        Assert.assertEquals(SizeExpression.roundedExpression(1023, 0), "1,023 B");
        Assert.assertEquals(SizeExpression.roundedExpression(1024, 0), "1 KB");
        Assert.assertEquals(SizeExpression.roundedExpression(10240, 0), "10 KB");
        Assert.assertEquals(SizeExpression.roundedExpression(10250, 0), "10 KB");
    }
    
    public void testRoundedExpressionOneDecimal() {
        Assert.assertEquals(SizeExpression.roundedExpression(1, 1), "1 B");
        Assert.assertEquals(SizeExpression.roundedExpression(10, 1), "10 B");
        Assert.assertEquals(SizeExpression.roundedExpression(100, 1), "100 B");
        Assert.assertEquals(SizeExpression.roundedExpression(1000, 1), "1,000 B");
        Assert.assertEquals(SizeExpression.roundedExpression(1023, 1), "1,023 B");
        Assert.assertEquals(SizeExpression.roundedExpression(1024, 1), "1.0 KB");
        Assert.assertEquals(SizeExpression.roundedExpression(10240, 1), "10.0 KB");
        Assert.assertEquals(SizeExpression.roundedExpression(10250, 1), "10.0 KB");
    }
    
    public void testRoundedExpressionAllPrefixes() {
        Assert.assertEquals(SizeExpression.roundedExpression(1, 0), "1 " + SizeExpression.UNIT_BASE);
        Assert.assertEquals(SizeExpression.roundedExpression(SizeExpression.MULTIPLIER_KILO, 0), "1 " + SizeExpression.PREFIX_KILO + SizeExpression.UNIT_BASE);
        Assert.assertEquals(SizeExpression.roundedExpression(SizeExpression.MULTIPLIER_MEGA, 0), "1 " + SizeExpression.PREFIX_MEGA + SizeExpression.UNIT_BASE);
        Assert.assertEquals(SizeExpression.roundedExpression(SizeExpression.MULTIPLIER_GIGA, 0), "1 " + SizeExpression.PREFIX_GIGA + SizeExpression.UNIT_BASE);
        Assert.assertEquals(SizeExpression.roundedExpression(SizeExpression.MULTIPLIER_TERA, 0), "1 " + SizeExpression.PREFIX_TERA + SizeExpression.UNIT_BASE);
        Assert.assertEquals(SizeExpression.roundedExpression(SizeExpression.MULTIPLIER_PETA, 0), "1 " + SizeExpression.PREFIX_PETA + SizeExpression.UNIT_BASE);
    }
    
    public void testInBytesWithUnit() {
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.UNIT_BASE), 1);
        Assert.assertEquals(SizeExpression.inBytes("10" + SizeExpression.UNIT_BASE), 10);
        Assert.assertEquals(SizeExpression.inBytes("100" + SizeExpression.UNIT_BASE), 100);
        Assert.assertEquals(SizeExpression.inBytes("1000" + SizeExpression.UNIT_BASE), 1000);
        Assert.assertEquals(SizeExpression.inBytes("1023" + SizeExpression.UNIT_BASE), 1023);
        Assert.assertEquals(SizeExpression.inBytes("1024" + SizeExpression.UNIT_BASE), 1024);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_KILO + SizeExpression.UNIT_BASE), SizeExpression.MULTIPLIER_KILO);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_MEGA + SizeExpression.UNIT_BASE), SizeExpression.MULTIPLIER_MEGA);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_GIGA + SizeExpression.UNIT_BASE), SizeExpression.MULTIPLIER_GIGA);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_TERA + SizeExpression.UNIT_BASE), SizeExpression.MULTIPLIER_TERA);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_PETA + SizeExpression.UNIT_BASE), SizeExpression.MULTIPLIER_PETA);
    }
    
    public void testInBytesWithoutUnit() {
        Assert.assertEquals(SizeExpression.inBytes("1"), 1);
        Assert.assertEquals(SizeExpression.inBytes("10"), 10);
        Assert.assertEquals(SizeExpression.inBytes("100"), 100);
        Assert.assertEquals(SizeExpression.inBytes("1000"), 1000);
        Assert.assertEquals(SizeExpression.inBytes("1023"), 1023);
        Assert.assertEquals(SizeExpression.inBytes("1024"), 1024);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_KILO), SizeExpression.MULTIPLIER_KILO);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_MEGA), SizeExpression.MULTIPLIER_MEGA);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_GIGA), SizeExpression.MULTIPLIER_GIGA);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_TERA), SizeExpression.MULTIPLIER_TERA);
        Assert.assertEquals(SizeExpression.inBytes("1" + SizeExpression.PREFIX_PETA), SizeExpression.MULTIPLIER_PETA);
    }
    
}
