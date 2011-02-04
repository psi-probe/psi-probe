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
package com.googlecode.psiprobe.tools.logging;

import com.googlecode.psiprobe.model.Application;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultAccessor  {

    protected final Log log = LogFactory.getLog(getClass());
    private Application application;
    private Object target;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getTargetClass() {
        return getTarget().getClass().getName();
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

}
