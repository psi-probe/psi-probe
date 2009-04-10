/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.tools;

import sun.reflect.FieldAccessor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Instruments {

    public static final int SZ_REF = 4;
    private static AccessorFactory accessorFactory = null;
    private List processedObjects = new ArrayList();
    private ClassLoader classLoader = null;
    private static boolean ignoreNIO;

    static {
        try {
            String vmVer = System.getProperty("java.runtime.version");
            String vmVendor = System.getProperty("java.vm.vendor");

            if (vmVendor != null &&
                    (vmVendor.indexOf("Sun Microsystems") != -1
                            || vmVendor.indexOf("Apple Computer") != -1
                            || vmVendor.indexOf("IBM Corporation") != -1)) {
                if (vmVer.startsWith("1.4")) {
                    accessorFactory = (AccessorFactory) Class.forName("org.jstripe.instruments.Java14AccessorFactory").newInstance();
                } else {
                    accessorFactory = (AccessorFactory) Class.forName("org.jstripe.instruments.Java15AccessorFactory").newInstance();
                }
            }

                String ignoreNIOProp = System.getProperty("org.jstripe.intruments.ignoreNIO");
            ignoreNIO = ignoreNIOProp == null || "true".equalsIgnoreCase(ignoreNIOProp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long sizeOf(Object o) {
        return new Instruments().internalSizeOf(o);
    }

    public static long sizeOf(Object o, ClassLoader cl) {
        Instruments instruments = new Instruments();
        instruments.classLoader = cl;
        return instruments.internalSizeOf(o);
    }

    public static long sizeOf(Object o, List objects) {
        Instruments instruments = new Instruments();
        instruments.processedObjects = objects;
        return instruments.internalSizeOf(o);
    }

    private long internalSizeOf(Object o) {
        long size = 0;
        if (isInitilized()
                && o != null
                && (classLoader == null || classLoader == o.getClass().getClassLoader())
                && (!ignoreNIO || !o.getClass().getName().startsWith("java.nio.")) ) {
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
                                FieldAccessor fa = getFieldAccessor(f);
                                if (f.getType().isPrimitive()) {
                                    size += sizeOfPrimitive(f.getType());
                                } else if (f.getType().isArray()) {
                                    size += sizeOfArray(fa.get(o));
                                } else {
                                    size += internalSizeOf(fa.get(o));
                                    size += SZ_REF;
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
                for (int i = 0; i < Array.getLength(o); i ++) {
                    size += internalSizeOf(Array.get(o, i));
                }
                return size;
            }
        } else {
            return 0;
        }
    }

    private static long sizeOfPrimitive(Class t) {
        if (t == Boolean.TYPE)
            return 1;
        else if (t == Byte.TYPE)
            return 1;
        else if (t == Character.TYPE)
            return 2;
        else if (t == Short.TYPE)
            return 2;
        else if (t == Integer.TYPE)
            return 4;
        else if (t == Long.TYPE)
            return 8;
        else if (t == Float.TYPE)
            return 8;
        else if (t == Double.TYPE)
            return 8;
        else if (t == Void.TYPE)
            return 0;
        else
            return SZ_REF;
    }

    public static FieldAccessor getFieldAccessor(Field f) {
        return isInitilized() ? accessorFactory.getFieldAccessor(f) : null;
    }

    public static boolean isInitilized() {
        return accessorFactory != null;
    }

    public static Object getField(Object o, String name) {
        return getField(o, name, null);
    }

    public static Object getField(Object o, String name, Object defaultValue) {
        Object value = null;
        Field f = findField(o.getClass(), name);
        FieldAccessor fa;

        if (f != null && ((fa = Instruments.getFieldAccessor(f)) != null)) {
            value = fa.get(o);
        }
        return value != null ? value : defaultValue;
    }

    public static Field findField(Class clazz, String name) {
        Field result = null;

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (name.equals(fields[i].getName())) {
                result = fields[i];
                break;
            }
        }
        if (result == null && clazz.getSuperclass() != null) {
            result = findField(clazz.getSuperclass(), name);
        }

        return result;
    }


}
