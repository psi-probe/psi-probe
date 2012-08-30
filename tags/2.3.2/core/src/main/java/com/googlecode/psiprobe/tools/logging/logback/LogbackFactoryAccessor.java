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
package com.googlecode.psiprobe.tools.logging.logback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.MethodUtils;

import com.googlecode.psiprobe.tools.logging.DefaultAccessor;

/**
 * Wraps a Logback logger factory from a given web application class loader.
 * 
 * <p>
 * All Logback classes are loaded via the given class loader and not via psi-probe's own
 * class loader. For this reasons, all methods on Logback objects are invoked via reflection.
 * </p>
 * <p>
 * This way, we can even handle different versions of Logback embedded in different WARs.
 * </p>
 * 
 * @author Harald Wellmann
 */
public class LogbackFactoryAccessor extends DefaultAccessor {

    /**
     * Attempts to initialize a Logback logger factory via the given class loader.
     *  
     * @param cl the ClassLoader to use when fetching the factory
     */
    public LogbackFactoryAccessor(ClassLoader cl) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Get the singleton SLF4J binding, which may or may not be Logback, depending on the            
        // binding.
        Class clazz = cl.loadClass("org.slf4j.impl.StaticLoggerBinder");
        Method m1 = MethodUtils.getAccessibleMethod(clazz, "getSingleton", new Class[]{});
        Object singleton = m1.invoke(null, null);
        Method m = MethodUtils.getAccessibleMethod(clazz, "getLoggerFactory", new Class[]{});
        Object loggerFactory = m.invoke(singleton, null);

        // Check if the binding is indeed Logback
        Class loggerFactoryClass = cl.loadClass("ch.qos.logback.classic.LoggerContext");            
        if (!loggerFactoryClass.isInstance(loggerFactory)) {
            throw new RuntimeException("The singleton SLF4J binding was not Logback");
        }
        setTarget(loggerFactory);
    }

    /**
     * Returns the Logback root logger.
     * 
     * @return the root logger
     */
    public LogbackLoggerAccessor getRootLogger() {
        // Logback has no dedicated getRootLogger() method, so we simply access the root logger
        // by its well-defined name.
        return getLogger("ROOT");
    }

    /**
     * Returns the Logback logger with a given name.
     * 
     * @return the Logger with the given name
     */
    public LogbackLoggerAccessor getLogger(String name) {
        try {
            Class clazz = getTarget().getClass();
            Method m = MethodUtils.getAccessibleMethod(clazz, "getLogger", new Class[] {String.class});
            Object logger = m.invoke(getTarget(), new Object[] {name});
            if (logger == null) {
                throw new NullPointerException(getTarget() + ".getLogger(\"" + name + "\") returned null");
            }
            LogbackLoggerAccessor accessor = new LogbackLoggerAccessor();
            accessor.setTarget(logger);
            accessor.setApplication(getApplication());
            return accessor;

        } catch (Exception e) {
            log.error(getTarget() + ".getLogger(\"" + name + "\") failed", e);
        }
        return null;
    }

    /**
     * Returns a list of wrappers for all Logback appenders that have an
     * associated logger.
     * 
     * @return a list of {@link LogbackAppenderAccessor}s representing all
     *         appenders that are in use
     */
    public List getAppenders() {
        List appenders = new ArrayList();
        try {
            Class clazz = getTarget().getClass();
            Method m = MethodUtils.getAccessibleMethod(clazz, "getLoggerList", new Class[]{});
            List loggers = (List) m.invoke(getTarget(), null);
            Iterator it = loggers.iterator();
            while (it.hasNext()) {
                LogbackLoggerAccessor accessor = new LogbackLoggerAccessor();
                accessor.setTarget(it.next());
                accessor.setApplication(getApplication());

                appenders.addAll(accessor.getAppenders());
            }
        } catch (Exception e) {
            log.error(getTarget() + ".getLoggerList() failed", e);
        }
        return appenders;
    }

}
