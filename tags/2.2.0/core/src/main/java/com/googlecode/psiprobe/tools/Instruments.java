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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class Instruments {

    private static final int SZ_REF = 4;
    private static final Accessor ACCESSOR = AccessorFactory.getInstance();
    private static final boolean IGNORE_NIO;
    static {
        String ignoreNIOProp = System.getProperty("com.googlecode.psiprobe.intruments.ignoreNIO");
        IGNORE_NIO = (ignoreNIOProp == null || "true".equalsIgnoreCase(ignoreNIOProp));
    }

    private Set processedObjects = new HashSet(2048);
    private ClassLoader classLoader = null;

    public static long sizeOf(Object o) {
        return new Instruments().internalSizeOf(o);
    }

    public static long sizeOf(Object o, ClassLoader cl) {
        Instruments instruments = new Instruments();
        instruments.classLoader = cl;
        return instruments.internalSizeOf(o);
    }

    public static long sizeOf(Object o, Set objects) {
        Instruments instruments = new Instruments();
        instruments.processedObjects = objects;
        return instruments.internalSizeOf(o);
    }

    private long internalSizeOf(Object o) {
        long size = 0;
        if (isInitialized()
                && o != null
                && (classLoader == null || classLoader == o.getClass().getClassLoader())
                && (!IGNORE_NIO || !o.getClass().getName().startsWith("java.nio.")) ) {
            ObjectWrapper ow = new ObjectWrapper(o);
            if (! processedObjects.contains(ow)) {
                size += 8;
                if (o.getClass().isArray()) {
                    size = sizeOfArray(o);
                } else if (o.getClass().isPrimitive()) {
                    size = sizeOfPrimitive(o.getClass());
                } else {
                    processedObjects.add(ow);
                    Class clazz = o.getClass();
                    while (clazz != null) {
                        Field fields[] = clazz.getDeclaredFields();
                        for (int i = 0; i < fields.length; i++) {
                            Field f = fields[i];
                            if ((f.getModifiers() & Modifier.STATIC) == 0) {
                                if (f.getType().isPrimitive()) {
                                    size += sizeOfPrimitive(f.getType());
                                } else {
                                    Object val = ACCESSOR.get(o, f);
                                    if (f.getType().isArray()) {
                                        size += sizeOfArray(val);
                                    } else {
                                        size += internalSizeOf(val);
                                        size += SZ_REF;
                                    }
                                }
                            }
                        }
                        clazz = clazz.getSuperclass();
                    }
                }
            }
        }
        return size;
    }

    private long sizeOfArray(Object o) {
        if (o != null) {
            Class ct = o.getClass().getComponentType();
            if (ct.isPrimitive()) {
                return Array.getLength(o) * sizeOfPrimitive(ct);
            } else {
                long size = 0;
                for (int i = 0; i < Array.getLength(o); i++) {
                    size += internalSizeOf(Array.get(o, i));
                }
                return size;
            }
        } else {
            return 0;
        }
    }

    private static long sizeOfPrimitive(Class t) {
        if (t == Boolean.TYPE) {
            return 1;
        } else if (t == Byte.TYPE) {
            return 1;
        } else if (t == Character.TYPE) {
            return 2;
        } else if (t == Short.TYPE) {
            return 2;
        } else if (t == Integer.TYPE) {
            return 4;
        } else if (t == Long.TYPE) {
            return 8;
        } else if (t == Float.TYPE) {
            return 8;
        } else if (t == Double.TYPE) {
            return 8;
        } else if (t == Void.TYPE) {
            return 0;
        } else {
            return SZ_REF;
        }
    }

    public static boolean isInitialized() {
        return ACCESSOR != null;
    }

    public static Object getField(Object o, String name) {
        if (isInitialized()) {
            Field f = findField(o.getClass(), name);
            if (f != null) {
                return ACCESSOR.get(o, f);
            }
        }
        return null;
    }

    public static Field findField(Class clazz, String name) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (name.equals(fields[i].getName())) {
                return fields[i];
            }
        }
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            return findField(superClass, name);
        } else {
            return null;
        }
    }

}
