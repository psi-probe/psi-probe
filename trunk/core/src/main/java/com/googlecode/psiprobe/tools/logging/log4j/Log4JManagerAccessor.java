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
package com.googlecode.psiprobe.tools.logging.log4j;

import com.googlecode.psiprobe.tools.logging.DefaultAccessor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.beanutils.MethodUtils;

public class Log4JManagerAccessor extends DefaultAccessor {

    private Log4JManagerAccessor() {
        
    }

    public static Log4JManagerAccessor create(ClassLoader cl) {
        try {
            Class clazz = cl.loadClass("org.apache.log4j.LogManager");
            Log4JManagerAccessor accessor = new Log4JManagerAccessor();
            accessor.setTarget(clazz);
            return accessor;
        } catch (Exception e) {
            return null;
        }
    }

    public Log4JLoggerAccessor getRootLogger() {
        try {
            Class clazz = (Class) getTarget();
            Method m = MethodUtils.getAccessibleMethod(clazz, "getRootLogger", new Class[]{});

            Log4JLoggerAccessor accessor = new Log4JLoggerAccessor();
            accessor.setTarget(m.invoke(null, null));
            accessor.setApplication(getApplication());
            return accessor;

        } catch (Exception e) {
            log.error(getTarget() + ".getRootLogger() failed", e);
            return null;
        }
    }

    public List getAppenders() {
        List appenders = new ArrayList();
        try {
            appenders.addAll(getRootLogger().getAppenders());

            Class clazz = (Class) getTarget();
            Method m = MethodUtils.getAccessibleMethod(clazz, "getCurrentLoggers", new Class[]{});
            Enumeration e = (Enumeration) m.invoke(null, null);
            while (e.hasMoreElements()) {
                Log4JLoggerAccessor accessor = new Log4JLoggerAccessor();
                accessor.setTarget(e.nextElement());
                accessor.setApplication(getApplication());

                appenders.addAll(accessor.getAppenders());
            }
        } catch (Exception e) {
            log.error(getTarget() + ".getCurrentLoggers() failed", e);
        }
        return appenders;
    }

}
