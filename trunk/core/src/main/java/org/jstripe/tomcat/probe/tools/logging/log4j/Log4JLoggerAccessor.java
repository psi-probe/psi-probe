/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 *  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 *  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.tomcat.probe.tools.logging.log4j;

import org.apache.commons.beanutils.MethodUtils;
import org.jstripe.tomcat.probe.tools.logging.DefaultAccessor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Log4JLoggerAccessor extends DefaultAccessor {

    public List getAppenders() {
        List appenders = new ArrayList();
        try {
            populateAppenders(getTarget(), appenders);
            Object hierarchy = MethodUtils.invokeMethod(getTarget(), "getLoggerRepository", null);
            if (hierarchy != null) {
                Enumeration e = (Enumeration) MethodUtils.invokeMethod(hierarchy, "getCurrentLoggers", null);
                while (e.hasMoreElements()) {
                    populateAppenders(e.nextElement(), appenders);
                }
            }
        } catch (Exception e) {
            log.error(getTarget()+".getAppenders() failed", e);
        }
        return appenders;
    }

    private void populateAppenders(Object o, List appenderList) throws Exception {
        Enumeration e = (Enumeration) MethodUtils.invokeMethod(o, "getAllAppenders", null);
        while(e.hasMoreElements()) {
            Log4JAppenderAccessor aa = new Log4JAppenderAccessor();
            aa.setTarget(e.nextElement());
            aa.setLoggerAccessor(this);
            aa.setLogClass("log4j");
            aa.setApplication(getApplication());
            appenderList.add(aa);
        }
    }
}
