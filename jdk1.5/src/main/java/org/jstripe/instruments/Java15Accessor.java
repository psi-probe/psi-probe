/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.instruments;

import java.lang.reflect.Field;
import java.security.AccessController;
import org.jstripe.tomcat.probe.tools.Accessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

public class Java15Accessor implements Accessor {

    private static ReflectionFactory reflectionFactory;

    static {
        reflectionFactory = (ReflectionFactory)
                AccessController.doPrivileged(new sun.reflect.ReflectionFactory.GetReflectionFactoryAction());
    }

    private FieldAccessor getFieldAccessor(Field f) {
        return reflectionFactory.newFieldAccessor(f, true);
    }

    public Object get(Object o, Field f) {
        return getFieldAccessor(f).get(o);
    }
}
