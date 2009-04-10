package org.jstripe.instruments;

import org.jstripe.tomcat.probe.tools.AccessorFactory;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Field;
import java.security.AccessController;

public class Java14AccessorFactory implements AccessorFactory {

    private static ReflectionFactory reflectionFactory;

    static {
        reflectionFactory = (ReflectionFactory)
                AccessController.doPrivileged(new sun.reflect.ReflectionFactory.GetReflectionFactoryAction());
    }

    public FieldAccessor getFieldAccessor(Field f) {
        return reflectionFactory.newFieldAccessor(f);
    }
}
