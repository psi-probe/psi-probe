package org.jstripe.instruments;

import java.lang.reflect.Field;
import java.security.AccessController;
import org.jstripe.tomcat.probe.tools.Accessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

public class Java14Accessor implements Accessor {

    private static ReflectionFactory reflectionFactory;

    static {
        reflectionFactory = (ReflectionFactory)
                AccessController.doPrivileged(new sun.reflect.ReflectionFactory.GetReflectionFactoryAction());
    }

    private FieldAccessor getFieldAccessor(Field f) {
        return reflectionFactory.newFieldAccessor(f);
    }

    public Object get(Object o, Field f) {
        return getFieldAccessor(f).get(o);
    }

}
