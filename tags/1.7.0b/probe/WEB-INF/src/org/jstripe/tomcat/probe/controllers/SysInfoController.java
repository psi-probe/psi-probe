/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.controllers;

import org.jstripe.tomcat.probe.model.SystemInformation;
import org.jstripe.tomcat.probe.beans.RuntimeInfoAccessorBean;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Creates an instance of SystemInformation POJO.
 * <p/>
 * Author: Vlad Ilyushchenko
 */
public class SysInfoController extends TomcatContainerController {

    private List filterOutKeys = new ArrayList();
    private RuntimeInfoAccessorBean runtimeInfoAccessor;

    public List getFilterOutKeys() {
        return filterOutKeys;
    }

    public void setFilterOutKeys(List filterOutKeys) {
        this.filterOutKeys = filterOutKeys;
    }

    public RuntimeInfoAccessorBean getRuntimeInfoAccessor() {
        return runtimeInfoAccessor;
    }

    public void setRuntimeInfoAccessor(RuntimeInfoAccessorBean runtimeInfoAccessor) {
        this.runtimeInfoAccessor = runtimeInfoAccessor;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SystemInformation systemInformation = new SystemInformation();
        systemInformation.setAppBase(getContainerWrapper().getTomcatContainer().getAppBase().getAbsolutePath());
        systemInformation.setConfigBase(getContainerWrapper().getTomcatContainer().getConfigBase());

        Map sysProps = new Properties();
        sysProps.putAll(System.getProperties());

        if (!request.isUserInRole(getServletContext().getInitParameter("attribute.value.role"))) {
            for (Iterator it = filterOutKeys.iterator(); it.hasNext();) {
                sysProps.remove(it.next());
            }
        }

        systemInformation.setSystemProperties(sysProps);

        return new ModelAndView(getViewName(), "systemInformation", systemInformation).addObject("runtime", runtimeInfoAccessor.getRuntimeInformation());
    }
}
