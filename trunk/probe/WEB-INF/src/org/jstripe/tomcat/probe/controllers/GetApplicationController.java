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
package org.jstripe.tomcat.probe.controllers;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.ServletRequestUtils;
import org.apache.catalina.Context;
import org.jstripe.tomcat.probe.tools.ApplicationUtils;
import org.jstripe.tomcat.probe.beans.AppStatsAccessor;
import org.jstripe.tomcat.probe.model.Application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Retrieves Application model object populated with application information
 * <p/>
 * Author: Andy Shapoval
 */
public class GetApplicationController extends ContextHandlerController {
    /**
     * denotes whether extended application information and statistics should be collected
     */
    private boolean extendedInfo = false;
    /**
     * retrieves application avg response time application stats
     */
    private AppStatsAccessor appStatsAccessor;

    public boolean isExtendedInfo() {
        return extendedInfo;
    }

    public void setExtendedInfo(boolean extendedInfo) {
        this.extendedInfo = extendedInfo;
    }

    public AppStatsAccessor getAppStatsAccessor() {
        return appStatsAccessor;
    }

    public void setAppStatsAccessor(AppStatsAccessor appStatsAccessor) {
        this.appStatsAccessor = appStatsAccessor;
    }

    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        String privelegedRole = getServletContext().getInitParameter("attribute.value.role");

        boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false) && request.isUserInRole(privelegedRole);

        Application app = ApplicationUtils.getApplication(
                context, isExtendedInfo() ? getContainerWrapper().getResourceResolver() : null, calcSize);
        if (isExtendedInfo() && getAppStatsAccessor() != null) {
            app.setAvgTime(getAppStatsAccessor().getAvgProcTime(app.getName()));
        }
        ModelAndView mv = new ModelAndView(getViewName(), "app", app);

        if (! getContainerWrapper().getResourceResolver().supportsPrivateResources()) {
            mv.addObject("no_resources", Boolean.TRUE);
        }

        return mv;
    }
}
