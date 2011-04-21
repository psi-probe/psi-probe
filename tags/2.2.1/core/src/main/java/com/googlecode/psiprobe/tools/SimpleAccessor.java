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

/**
 *
 * @author Mark Lewis
 */
public class SimpleAccessor implements Accessor {

    public Object get(Object o, Field f) {
        boolean accessible = pre(f);
        try {
            return get0(o, f);
        } catch (Exception e) {
            return null;
        } finally {
            post(f, accessible);
        }
    }

    private Object get0(Object o, Field f) throws IllegalArgumentException, IllegalAccessException {
        if (f.isAccessible()) {
            return f.get(o);
        } else {
            return null;
        }
    }

    private boolean pre(Field f) {
        boolean accessible = f.isAccessible();
        if (!accessible) {
            try {
                f.setAccessible(true);
            } catch (SecurityException ex) {
                //ignore
            }
        }
        return accessible;
    }

    private void post(Field f, boolean value) {
        if (!value) {
            try {
                f.setAccessible(false);
            } catch (SecurityException ex) {
                //ignore
            }
        }
    }

}
