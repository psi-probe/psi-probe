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
package com.googlecode.psiprobe.tools.logging.jdk;

import com.googlecode.psiprobe.tools.logging.DefaultAccessor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.beanutils.MethodUtils;

public class Jdk14ManagerAccessor extends DefaultAccessor {

    public Jdk14ManagerAccessor(ClassLoader cl) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class clazz = cl.loadClass("java.util.logging.LogManager");
        Method getManager = MethodUtils.getAccessibleMethod(clazz, "getLogManager", new Class[]{});
        Object manager = getManager.invoke(null, null);
        if (manager == null) {
            throw new NullPointerException(clazz.getName() + ".getLogManager() returned null");
        }
        setTarget(manager);
    }

    public Jdk14LoggerAccessor getRootLogger() {
        return getLogger("");
    }

    public Jdk14LoggerAccessor getLogger(String name) {
        try {
            Object logger = MethodUtils.invokeMethod(getTarget(), "getLogger", name);
            if (logger == null) {
                throw new NullPointerException(getTarget() + ".getLogger(\"" + name + "\") returned null");
            }
            Jdk14LoggerAccessor accessor = new Jdk14LoggerAccessor();
            accessor.setTarget(logger);
            accessor.setApplication(getApplication());
            return accessor;
        } catch (Exception e) {
            log.error(getTarget() + ".getLogger(\"" + name + "\") failed", e);
        }
        return null;
    }

    public List getHandlers() {
        List allHandlers = new ArrayList();
        try {
            Enumeration e = (Enumeration) MethodUtils.invokeMethod(getTarget(), "getLoggerNames", null);
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                Jdk14LoggerAccessor accessor = getLogger(name);
                if (accessor != null) {
                    allHandlers.addAll(accessor.getHandlers());
                }
            }
        } catch (Exception e) {
            log.error(getTarget() + ".getLoggerNames() failed", e);
        }
        return allHandlers;
    }

}
