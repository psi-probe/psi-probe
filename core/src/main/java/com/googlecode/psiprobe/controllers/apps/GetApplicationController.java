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
package com.googlecode.psiprobe.controllers.apps;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import com.googlecode.psiprobe.model.Application;
import com.googlecode.psiprobe.model.stats.StatsCollection;
import com.googlecode.psiprobe.tools.ApplicationUtils;
import com.googlecode.psiprobe.tools.SecurityUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Retrieves Application model object populated with application information.
 * 
 * @author Andy Shapoval
 */
public class GetApplicationController extends ContextHandlerController {
    /**
     * denotes whether extended application information and statistics should be collected
     */
    private boolean extendedInfo = false;
    private StatsCollection statsCollection;
    private long collectionPeriod;

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

    public long getCollectionPeriod() {
        return collectionPeriod;
    }

    public void setCollectionPeriod(long collectionPeriod) {
        this.collectionPeriod = collectionPeriod;
    }

    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false)
                && SecurityUtils.hasAttributeValueRole(getServletContext(), request);

        Application app = ApplicationUtils.getApplication(
                context, isExtendedInfo() ? getContainerWrapper().getResourceResolver() : null, calcSize);

        if (isExtendedInfo() && getStatsCollection() != null) {
            String avgStatisticName = "app.avg_proc_time." + app.getName();
            app.setAvgTime(getStatsCollection().getLastValueForStat(avgStatisticName));
        }

        return new ModelAndView(getViewName())
                .addObject("app", app)
                .addObject("no_resources", Boolean.valueOf(!getContainerWrapper().getResourceResolver().supportsPrivateResources()))
                .addObject("collectionPeriod", new Long(getCollectionPeriod()));
    }
}
