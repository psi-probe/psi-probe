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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 *
 * @author Mark Lewis
 */
public class ReflectiveAccessor implements Accessor {

    private static Object reflectionFactory;
    private static Method newFieldAccessor;
    private static Method get;

    ReflectiveAccessor() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        init();
    }

    public Object get(Object o, Field f) {
        try {
            Object fieldAccessor = getFieldAccessor(f);
            if (fieldAccessor != null) {
                return get.invoke(fieldAccessor, new Object[] {o});
            }
        } catch (Exception ex) {
            //ignore
        }
        return null;
    }

    private static Object getFieldAccessor(Field f) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (newFieldAccessor.getParameterTypes().length == 1) {
            return newFieldAccessor.invoke(reflectionFactory, new Object[] {f});
        } else {
            return newFieldAccessor.invoke(reflectionFactory, new Object[] {f, Boolean.TRUE});
        }
    }

    private static final void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String vmVendor = System.getProperty("java.vm.vendor");
        if (vmVendor != null && (
                vmVendor.indexOf("Sun Microsystems") != -1
                || vmVendor.indexOf("Apple Computer") != -1
                || vmVendor.indexOf("Apple Inc.") != -1
                || vmVendor.indexOf("IBM Corporation") != -1)) {

            reflectionFactory = getReflectionFactory();
            String vmVer = System.getProperty("java.runtime.version");
            Class[] paramTypes;
            if (vmVer.startsWith("1.4")) {
                paramTypes = new Class[] {Field.class};
            } else {
                paramTypes = new Class[] {Field.class, Boolean.TYPE};
            }
            newFieldAccessor = reflectionFactory.getClass().getMethod("newFieldAccessor", paramTypes);
            get = newFieldAccessor.getReturnType().getMethod("get", new Class[] {Object.class});
        }
    }

    private static Object getReflectionFactory() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class getReflectionFactoryActionClass = Class.forName("sun.reflect.ReflectionFactory$GetReflectionFactoryAction");
        PrivilegedAction getReflectionFactoryAction = (PrivilegedAction) getReflectionFactoryActionClass.newInstance();
        return AccessController.doPrivileged(getReflectionFactoryAction);
    }

}
