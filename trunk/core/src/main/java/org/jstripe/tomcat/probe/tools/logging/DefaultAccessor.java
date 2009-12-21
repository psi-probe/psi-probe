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
package org.jstripe.tomcat.probe.tools.logging;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jstripe.tomcat.probe.model.Application;

import java.io.File;
import java.sql.Timestamp;

public class DefaultAccessor  {

    protected final Log log = LogFactory.getLog(getClass());
    private Application application;
    //
    // class of the log, e.g. "log4j", "jdk", "commons-" etc.
    // it is NOT a class name in Java sense of the word
    //
    private String logClass;
    private Object target;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getLogClass() {
        return logClass;
    }

    public void setLogClass(String logClass) {
        this.logClass = logClass;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    protected Object getProperty(Object o, String name, Object defaultValue) {
        try {
            return PropertyUtils.isReadable(o, name) ? PropertyUtils.getProperty(o, name) : defaultValue;
        } catch (Exception e) {
            log.debug(e);
            return defaultValue;
        }
    }

    protected Object invokeMethod(Object object, String name, Object param, Object defaultValue) {
        try {
            if (param == null) {
                return MethodUtils.invokeMethod(object, name, new Object[]{});
            } else {
                return MethodUtils.invokeMethod(object, name, param);
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    protected File getStdoutFile() {
        File f = new File(System.getProperty("catalina.base"), "logs/catalina.out");
        return f.exists() ? f : new File("stdout");
    }

    public File getFile() {
        return getStdoutFile();
    }

    public long getSize() {
        File f = getFile();
        return f != null && f.exists() ? f.length() : 0;
    }

    public Timestamp getLastModified() {
        File f = getFile();
        return f != null && f.exists() ? new Timestamp(f.lastModified()) : null;
    }
}
