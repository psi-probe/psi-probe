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
import junit.framework.TestCase;

/**
 *
 * @author Mark Lewis
 */
public class InstrumentsTests extends TestCase {

    private String sunArchDataModelProperty;

    /**
     * Forces the tests to run in 32-bit mode.
     */
    protected void setUp() {
        this.sunArchDataModelProperty = System.getProperty("sun.arch.data.model");
        System.setProperty("sun.arch.data.model", "32");
    }

    /**
     * Undoes the changes made in {@link #setUp()}.
     */
    protected void tearDown() {
        System.setProperty("sun.arch.data.model", this.sunArchDataModelProperty);
    }

    public void testObject() {
        Object o = new Object();
        long objectSize = Instruments.sizeOf(o);
        Assert.assertEquals(Instruments.SIZE_OBJECT, objectSize);
    }

    public void testBoolean() {
        boolean b = false;
        long booleanSize = Instruments.sizeOf(new Boolean(b)) - Instruments.SIZE_OBJECT;
        Assert.assertEquals(Instruments.SIZE_BOOLEAN, booleanSize);
    }

    public void testByte() {
        byte b = 0x00;
        long byteSize = Instruments.sizeOf(new Byte(b)) - Instruments.SIZE_OBJECT;
        Assert.assertEquals(Instruments.SIZE_BYTE, byteSize);
    }
    
    public void testChar() {
        char c = '\0';
        long charSize = Instruments.sizeOf(new Character(c)) - Instruments.SIZE_OBJECT;
        Assert.assertEquals(Instruments.SIZE_CHAR, charSize);
    }

    public void testShort() {
        short s = 0;
        long shortSize = Instruments.sizeOf(new Short(s)) - Instruments.SIZE_OBJECT;
        Assert.assertEquals(Instruments.SIZE_SHORT, shortSize);
    }

    public void testInt() {
        int i = 0;
        long intSize = Instruments.sizeOf(new Integer(i)) - Instruments.SIZE_OBJECT;
        Assert.assertEquals(Instruments.SIZE_INT, intSize);
    }

    public void testLong() {
        long l = 0;
        long longSize = Instruments.sizeOf(new Long(l)) - Instruments.SIZE_OBJECT;
        Assert.assertEquals(Instruments.SIZE_LONG, longSize);
    }

    public void testFloat() {
        float f = 0.0f;
        long floatSize = Instruments.sizeOf(new Float(f)) - Instruments.SIZE_OBJECT;
        Assert.assertEquals(Instruments.SIZE_FLOAT, floatSize);
    }

    public void testDouble() {
        double d = 0.0;
        long doubleSize = Instruments.sizeOf(new Double(d)) - Instruments.SIZE_OBJECT;
        Assert.assertEquals(Instruments.SIZE_DOUBLE, doubleSize);
    }
    
}
