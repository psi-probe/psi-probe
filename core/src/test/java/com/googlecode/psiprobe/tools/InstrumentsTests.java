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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author Mark Lewis
 */
public class InstrumentsTests extends TestCase {

    public static final long SIZE_BYTE = 1;
    public static final long SIZE_CHAR = 2;
    public static final long SIZE_SHORT = 2;
    public static final long SIZE_INT = 4;
    public static final long SIZE_LONG = 8;
    public static final long SIZE_FLOAT = 8;
    public static final long SIZE_DOUBLE = 8;
    public static final long SIZE_OBJECT = 8;
    public static final long SIZE_REFERENCE = 4;

    public void testObject() {
        long oSize = Instruments.sizeOf(new Object());
        Assert.assertEquals(SIZE_OBJECT, oSize);
    }

    public void testByte() {
        byte b = 0x00;
        long byteSize = Instruments.sizeOf(new Byte(b)) - SIZE_OBJECT;
        Assert.assertEquals(SIZE_BYTE, byteSize);
    }
    
    public void testChar() {
        char c = '\0';
        long charSize = Instruments.sizeOf(new Character(c)) - SIZE_OBJECT;
        Assert.assertEquals(SIZE_CHAR, charSize);
    }

    public void testShort() {
        short s = 0;
        long shortSize = Instruments.sizeOf(new Short(s)) - SIZE_OBJECT;
        Assert.assertEquals(SIZE_SHORT, shortSize);
    }

    public void testInt() {
        int i = 0;
        long intSize = Instruments.sizeOf(new Integer(i)) - SIZE_OBJECT;
        Assert.assertEquals(SIZE_INT, intSize);
    }

    public void testLong() {
        long l = 0;
        long longSize = Instruments.sizeOf(new Long(l)) - SIZE_OBJECT;
        Assert.assertEquals(SIZE_LONG, longSize);
    }

    public void testFloat() {
        float f = 0.0f;
        long floatSize = Instruments.sizeOf(new Float(f)) - SIZE_OBJECT;
        Assert.assertEquals(SIZE_FLOAT, floatSize);
    }

    public void testDouble() {
        double d = 0.0;
        long doubleSize = Instruments.sizeOf(new Double(d)) - SIZE_OBJECT;
        Assert.assertEquals(SIZE_DOUBLE, doubleSize);
    }
    
    public void testString() {
        String s = "test";
        long stringSize = Instruments.sizeOf(s);
        Assert.assertEquals((s.length() * SIZE_CHAR) + (3 * SIZE_INT) + SIZE_OBJECT, stringSize);
    }

    public void testMap() {
        Map session = new HashMap();
        session.put("test1", "test message");
        List bikes = new ArrayList();
        bikes.add("specialized");
        bikes.add("kona");
        bikes.add("GT");
        session.put("bikes", bikes);

        Map bikeParts = new TreeMap();
        bikeParts.put("bikes", bikes);
        session.put("parts", bikeParts);

        long size = Instruments.sizeOf(session);
        Assert.assertEquals(453, size);
    }

}
