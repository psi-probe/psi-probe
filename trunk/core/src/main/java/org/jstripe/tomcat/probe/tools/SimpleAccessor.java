package org.jstripe.tomcat.probe.tools;

import java.lang.reflect.Field;

/**
 *
 * @author Mark Lewis
 */
public class SimpleAccessor implements Accessor {

    public Object get(Object o, Field f) {
        boolean accessible = pre(f);
        try {
            if (accessible) {
                return f.get(o);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        } finally {
            post(f, accessible);
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
