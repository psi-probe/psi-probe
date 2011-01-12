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

    public List getAppenders() {
        List appenders = new ArrayList();
        try {
            Enumeration e = (Enumeration) MethodUtils.invokeMethod(getTarget(), "getAllAppenders", null);
            while(e.hasMoreElements()) {
                Log4JAppenderAccessor aa = new Log4JAppenderAccessor();
                aa.setTarget(e.nextElement());
                aa.setLoggerAccessor(this);
                aa.setLogClass("log4j");
                aa.setApplication(getApplication());
                appenders.add(aa);
            }
        } catch (Exception e) {
            log.error(getTarget()+".getAllAppenders() failed", e);
        }
        return appenders;
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

}
