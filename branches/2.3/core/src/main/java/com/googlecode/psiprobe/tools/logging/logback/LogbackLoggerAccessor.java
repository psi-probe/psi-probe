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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.MethodUtils;

import com.googlecode.psiprobe.tools.logging.DefaultAccessor;

/**
 * A wrapper for a Logback logger.
 * 
 * @author Harald Wellmann
 */
public class LogbackLoggerAccessor extends DefaultAccessor {

    /**
     * Returns all appenders of this logger.
     * 
     * @return a list of {@link LogbackAppenderAccessor}s
     */
    public List getAppenders() {
        List appenders = new ArrayList();
        try {
            Iterator it =  (Iterator) MethodUtils.invokeMethod(getTarget(), "iteratorForAppenders", null);
            while (it.hasNext()) {
                LogbackAppenderAccessor aa = wrapAppender(it.next());
                if (aa != null) {
                    appenders.add(aa);
                }
            }
        } catch (Exception e) {
            log.error(getTarget() + ".iteratorForAppenders() failed", e);
        }
        return appenders;
    }

    /**
     * Returns the appender of this logger with the given name.
     * 
     * @param name the name of the appender to return
     * @return the appender with the given name, or null if no such
     *         appender exists for this logger
     */
    public LogbackAppenderAccessor getAppender(String name) {
        try {
            Object appender = MethodUtils.invokeMethod(getTarget(), "getAppender", name);
            return wrapAppender(appender);
        } catch (Exception e) {
            log.error(getTarget() + ".getAppender() failed", e);
        }
        return null;
    }

    public boolean isContext() {
        return false;
    }

    public boolean isRoot() {
        return "ROOT".equals(getName());
    }

    public String getName() {
        return (String) getProperty(getTarget(), "name", null);
    }

    /**
     * Gets the log level of this logger.
     * 
     * @return the level of this logger
     */
    public String getLevel() {
        try {
            Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
            return (String) MethodUtils.invokeMethod(level, "toString", null);
        } catch (Exception e) {
            log.error(getTarget() + ".getLevel() failed", e);
        }
        return null;
    }

    /**
     * Sets the log level of this logger.
     * 
     * @param newLevelStr the name of the new level
     */
    public void setLevel(String newLevelStr) {
        try {
            Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
            Object newLevel = MethodUtils.invokeMethod(level, "toLevel", newLevelStr);
            MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
        } catch (Exception e) {
            log.error(getTarget() + ".setLevel(\"" + newLevelStr + "\") failed", e);
        }
    }

    private LogbackAppenderAccessor wrapAppender(Object appender) {
        try {
            if (appender == null) {
                throw new IllegalArgumentException("appender is null");
            }
            LogbackAppenderAccessor appenderAccessor = new LogbackAppenderAccessor();
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
