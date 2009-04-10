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

import org.jstripe.tomcat.probe.tools.AccessorFactory;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Field;
import java.security.AccessController;

public class Java15AccessorFactory implements AccessorFactory {

    private static ReflectionFactory reflectionFactory;

    static {
        reflectionFactory = (ReflectionFactory)
                AccessController.doPrivileged(new sun.reflect.ReflectionFactory.GetReflectionFactoryAction());
    }

    public FieldAccessor getFieldAccessor(Field f) {
        return reflectionFactory.newFieldAccessor(f, true);
    }
}
