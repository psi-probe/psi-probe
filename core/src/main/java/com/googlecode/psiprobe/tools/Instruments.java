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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Instruments {

    public static final long SIZE_VOID = 0;
    public static final long SIZE_BOOLEAN = 1;
    public static final long SIZE_BYTE = 1;
    public static final long SIZE_CHAR = 2;
    public static final long SIZE_SHORT = 2;
    public static final long SIZE_INT = 4;
    public static final long SIZE_LONG = 8;
    public static final long SIZE_FLOAT = 4;
    public static final long SIZE_DOUBLE = 8;
    public static final long SIZE_OBJECT = 8;
    public static final long SIZE_REFERENCE;

    private static final Accessor ACCESSOR = AccessorFactory.getInstance();
    private static final boolean IGNORE_NIO;
    static {
        String ignoreNIOProp = System.getProperty("com.googlecode.psiprobe.intruments.ignoreNIO");
        String os64bitProp = System.getProperty("sun.arch.data.model");
        IGNORE_NIO = (ignoreNIOProp == null || "true".equalsIgnoreCase(ignoreNIOProp));
        SIZE_REFERENCE = ("64".equals(os64bitProp) ? 8 : 4);
    }

    private Set processedObjects = new HashSet(2048);
    private List thisQueue = new LinkedList();
    private List nextQueue = new LinkedList();
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

    private long internalSizeOf(Object obj) {
        long size = 0;
        thisQueue.add(obj);
        while (!thisQueue.isEmpty()) {
            Iterator it = thisQueue.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (isInitialized()
                        && o != null
                        && (classLoader == null || classLoader == o.getClass().getClassLoader())
                        && (!IGNORE_NIO || !o.getClass().getName().startsWith("java.nio.")) ) {
                    ObjectWrapper ow = new ObjectWrapper(o);
                    if (!processedObjects.contains(ow)) {
                        if (o.getClass().isArray()) {
                            size += sizeOfArray(o);
                        } else if (o.getClass().isPrimitive()) {
                            size += sizeOfPrimitive(o.getClass());
                        } else {
                            processedObjects.add(ow);
                            size += sizeOfObject(o);
                        }
                    }
                }
                it.remove();
            }
            //avoids ConcurrentModificationException
            if (!nextQueue.isEmpty()) {
                thisQueue.addAll(nextQueue);
                nextQueue.clear();
            }
        }
        return size;
    }

    private long sizeOfObject(Object o) {
        long size = SIZE_OBJECT;
        Class clazz = o.getClass();
        while (clazz != null) {
            Field fields[] = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                if (!Modifier.isStatic(f.getModifiers())) {
                    if (f.getType().isPrimitive()) {
                        size += sizeOfPrimitive(f.getType());
                    } else {
                        Object val = ACCESSOR.get(o, f);
                        if (f.getType().isArray()) {
                            size += sizeOfArray(val);
                        } else {
                            size += SIZE_REFERENCE;
                            nextQueue.add(val);
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return size;
    }

    private long sizeOfArray(Object o) {
        if (o != null) {
            Class ct = o.getClass().getComponentType();
            if (ct.isPrimitive()) {
                return Array.getLength(o) * sizeOfPrimitive(ct);
            } else {
                for (int i = 0; i < Array.getLength(o); i++) {
                    nextQueue.add(Array.get(o, i));
                }
            }
        }
        return 0;
    }

    private static long sizeOfPrimitive(Class t) {
        if (t == Boolean.TYPE) {
            return SIZE_BOOLEAN;
        } else if (t == Byte.TYPE) {
            return SIZE_BYTE;
        } else if (t == Character.TYPE) {
            return SIZE_CHAR;
        } else if (t == Short.TYPE) {
            return SIZE_SHORT;
        } else if (t == Integer.TYPE) {
            return SIZE_INT;
        } else if (t == Long.TYPE) {
            return SIZE_LONG;
        } else if (t == Float.TYPE) {
            return SIZE_FLOAT;
        } else if (t == Double.TYPE) {
            return SIZE_DOUBLE;
        } else if (t == Void.TYPE) {
            return SIZE_VOID;
        } else {
            return SIZE_REFERENCE;
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
