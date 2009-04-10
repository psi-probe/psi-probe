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

package org.jstripe.tomcat.probe.beans.stats.collectors;

import org.apache.catalina.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jstripe.tomcat.probe.beans.ContainerWrapperBean;
import org.jstripe.tomcat.probe.model.Application;
import org.jstripe.tomcat.probe.model.stats.StatsCollection;
import org.jstripe.tomcat.probe.tools.ApplicationUtils;
import org.jstripe.tomcat.probe.TomcatContainer;
import org.jfree.data.xy.XYDataItem;

import java.util.Iterator;
import java.util.List;

/**
 * Collects application statistics
 * <p/>
 * Author: Andy Shapoval
 */
public class AppStatsCollector extends BaseStatsCollectorBean {
    private Log logger = LogFactory.getLog(AppStatsCollector.class);
    private ContainerWrapperBean containerWrapper;
    protected static final String APP_STATS_PREFIX = "app.";
    protected static final String AVG_PROC_TIME_SUFFIX = ".avg_proc_time";

    public ContainerWrapperBean getContainerWrapper() {
        return containerWrapper;
    }

    public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
        this.containerWrapper = containerWrapper;
    }

    public void collect() {
        long t = System.currentTimeMillis();
        if (containerWrapper == null) {
            logger.error("Cannot collect application stats. Container wrapper is not set.");
        } else {
            TomcatContainer tomcatContainer = getContainerWrapper().getTomcatContainer();
            // check if the containerWtapper has been initialized
            if (tomcatContainer != null ) {
                List ctxs = tomcatContainer.findContexts();
                for (Iterator i = ctxs.iterator(); i.hasNext();) {
                    Context ctx = (Context) i.next();
                    if (ctx != null && ctx.getName() != null) {
                        Application app = new Application();
                        ApplicationUtils.collectApplicationServletStats(ctx, app);
                        final String statName = APP_STATS_PREFIX + ctx.getName();
                        long reqDelta = buildDeltaStats(statName + ".requests", app.getRequestCount());
                        long procTimeDelta = buildDeltaStats(statName + ".proc_time", app.getProcessingTime());
                        buildDeltaStats(statName + ".errors", app.getErrorCount());
                        buildAbsoluteStats(statName + AVG_PROC_TIME_SUFFIX, reqDelta == 0 ? 0 : procTimeDelta / reqDelta);
                    }
                }
            }
        }
        logger.info("app stats collected in " + (System.currentTimeMillis() - t) + "ms.");
    }

    /**
     * Retrieves the value of application average processing (response) time from a stats collection 
     * @param appName application name
     * @return average processing (response) time for the application
     */
    public long getAvgProcTime(String appName) {
        long avgTime = 0;

        StatsCollection statsCollection = getStatsCollection();
        if (statsCollection != null) {
            List stats = statsCollection.getStats(APP_STATS_PREFIX + appName + AVG_PROC_TIME_SUFFIX);
            if (stats != null && ! stats.isEmpty()) {
                XYDataItem xy = (XYDataItem) stats.get(stats.size() - 1);
                if (xy != null && xy.getY() != null) {
                    avgTime = xy.getY().longValue();
                }
            }
        }

        return avgTime;
    }
}
