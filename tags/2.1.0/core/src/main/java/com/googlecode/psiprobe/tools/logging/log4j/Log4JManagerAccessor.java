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

import java.lang.reflect.Method;
import org.apache.commons.beanutils.MethodUtils;

public class Log4JManagerAccessor {

    public static Log4JLoggerAccessor getRootLogger(ClassLoader cl) {
        try {
            Class clazz = cl.loadClass("org.apache.log4j.LogManager");
            Method m = MethodUtils.getAccessibleMethod(clazz, "getRootLogger", new Class[]{});

            Log4JLoggerAccessor accessor = new Log4JLoggerAccessor();
            accessor.setTarget(m.invoke(null, null));
            return accessor;

        } catch (Exception e) {
            return null;
        }
    }
}
