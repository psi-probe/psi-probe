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

import com.googlecode.psiprobe.tools.logging.AbstractLogDestination;
import org.apache.commons.beanutils.MethodUtils;

public class Jdk14HandlerAccessor extends AbstractLogDestination {

    private Jdk14LoggerAccessor loggerAccessor;
    private String index;

    public Jdk14LoggerAccessor getLoggerAccessor() {
        return loggerAccessor;
    }

    public void setLoggerAccessor(Jdk14LoggerAccessor loggerAccessor) {
        this.loggerAccessor = loggerAccessor;
    }

    public boolean isContext() {
        return getLoggerAccessor().isContext();
    }

    public boolean isRoot() {
        return getLoggerAccessor().isRoot();
    }

    public String getName() {
        return getLoggerAccessor().getName();
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getLogType() {
        return "jdk";
    }

    public String getLevel() {
        return getLoggerAccessor().getLevel();
        /*
        try {
            Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
            return (String) MethodUtils.invokeMethod(level, "getName", null);
        } catch (Exception e) {
            log.error(getTarget() + ".getLevel() failed", e);
        }
        return null;
         */
    }

    public void setLevel(String newLevelStr) {
        try {
            Object level = MethodUtils.invokeMethod(getTarget(), "getLevel", null);
            Object newLevel = MethodUtils.invokeMethod(level, "parse", newLevelStr);
            MethodUtils.invokeMethod(getTarget(), "setLevel", newLevel);
        } catch (Exception e) {
            log.error(getTarget() + ".setLevel(\"" + newLevelStr + "\") failed", e);
        }
    }

    public String[] getValidLevels() {
        return new String[] {"OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL"};
    }

}
