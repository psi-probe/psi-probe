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

import org.apache.catalina.Context;
import org.jstripe.tomcat.probe.model.Application;
import org.jstripe.tomcat.probe.model.stats.StatsCollection;
import org.jstripe.tomcat.probe.tools.ApplicationUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

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
    private StatsCollection statsCollection;

    public boolean isExtendedInfo() {
        return extendedInfo;
    }

    public void setExtendedInfo(boolean extendedInfo) {
        this.extendedInfo = extendedInfo;
    }

    public StatsCollection getStatsCollection() {
        return statsCollection;
    }

    public void setStatsCollection(StatsCollection statsCollection) {
        this.statsCollection = statsCollection;
    }

    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        String privelegedRole = getServletContext().getInitParameter("attribute.value.role");
        boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false) && request.isUserInRole(privelegedRole);

        Application app = ApplicationUtils.getApplication(
                context, isExtendedInfo() ? getContainerWrapper().getResourceResolver() : null, calcSize);

        if (isExtendedInfo() && getStatsCollection() != null) {
            String avgStatisticName = "app.avg_proc_time." + app.getName();
            app.setAvgTime(getStatsCollection().getLastValueForStat(avgStatisticName));
        }

        return new ModelAndView(getViewName(), "app", app).
                addObject("no_resources", Boolean.valueOf(!getContainerWrapper().getResourceResolver().supportsPrivateResources()));
    }
}
