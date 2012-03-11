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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.beanutils.MethodUtils;

public class Log4JLoggerAccessor extends DefaultAccessor {

    private boolean context = false;

    public List getAppenders() {
        List appenders = new ArrayList();
        try {
            Enumeration e = (Enumeration) MethodUtils.invokeMethod(getTarget(), "getAllAppenders", null);
            while(e.hasMoreElements()) {
                Log4JAppenderAccessor appender = wrapAppender(e.nextElement());
                if (appender != null) {
                    appenders.add(appender);
                }
            }
        } catch (Exception e) {
            log.error(getTarget()+".getAllAppenders() failed", e);
        }
        return appenders;
    }

    public Log4JAppenderAccessor getAppender(String name) {
        try {
            Object appender = MethodUtils.invokeMethod(getTarget(), "getAppender", name);
            return wrapAppender(appender);
        } catch (Exception e) {
            log.error(getTarget() + ".getAppender() failed", e);
        }
        return null;
    }

    public boolean isContext() {
        return context;
    }

    public void setContext(boolean context) {
        this.context = context;
    }

    public boolean isRoot() {
        return "root".equals(getName())
                && "org.apache.log4j.spi.RootLogger".equals(getTargetClass());
    }

    public String getName() {
        return (String) getProperty(getTarget(), "name", null);
    }

    public String getLevel() {
        try {
            Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
            return (String) MethodUtils.invokeMethod(level, "toString", null);
        } catch (Exception e) {
            log.error(getTarget() + ".getLevel() failed", e);
        }
        return null;
    }

    public void setLevel(String newLevelStr) {
        try {
            Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
            Object newLevel = MethodUtils.invokeMethod(level, "toLevel", newLevelStr);
            MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
        } catch (Exception e) {
            log.error(getTarget() + ".setLevel(\"" + newLevelStr + "\") failed", e);
        }
    }

    private Log4JAppenderAccessor wrapAppender(Object appender) {
        try {
            if (appender == null) {
                throw new IllegalArgumentException("appender is null");
            }
            Log4JAppenderAccessor appenderAccessor = new Log4JAppenderAccessor();
            appenderAccessor.setTarget(appender);
            appenderAccessor.setLoggerAccessor(this);
            appenderAccessor.setApplication(getApplication());
            return appenderAccessor;
        } catch (Exception e) {
            log.error("Could not wrap appender: " + appender, e);
        }
        return null;
    }

}
