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
import org.jstripe.tomcat.probe.TomcatContainer;
import org.jstripe.tomcat.probe.beans.ContainerWrapperBean;
import org.jstripe.tomcat.probe.model.Application;
import org.jstripe.tomcat.probe.tools.ApplicationUtils;

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

    public ContainerWrapperBean getContainerWrapper() {
        return containerWrapper;
    }

    public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
        this.containerWrapper = containerWrapper;
    }

    public void collect() throws Exception {
        long t = System.currentTimeMillis();
        if (containerWrapper == null) {
            logger.error("Cannot collect application stats. Container wrapper is not set.");
        } else {
            TomcatContainer tomcatContainer = getContainerWrapper().getTomcatContainer();
            // check if the containerWtapper has been initialized
            if (tomcatContainer != null ) {
                long totalReqDelta = 0;
                long totalAvgProcTime = 0;
                int appCount = 0;
                List ctxs = tomcatContainer.findContexts();
                for (Iterator i = ctxs.iterator(); i.hasNext();) {
                    Context ctx = (Context) i.next();
                    if (ctx != null && ctx.getName() != null) {
                        Application app = new Application();
                        ApplicationUtils.collectApplicationServletStats(ctx, app);
                        final String statName = "app." + (ctx.getName().equals("") ? "/" : ctx.getName()) + ".";
                        long reqDelta = buildDeltaStats(statName + "requests", app.getRequestCount());
                        long procTimeDelta = buildDeltaStats(statName + "proc_time", app.getProcessingTime());
                        buildDeltaStats(statName + "errors", app.getErrorCount());
                        long avgProcTime = reqDelta == 0 ? 0 : procTimeDelta / reqDelta;
                        buildAbsoluteStats(statName + "avg_proc_time", avgProcTime);
                        totalReqDelta += reqDelta;
                        totalAvgProcTime += avgProcTime;
                        appCount++;
                    }
                }
                // building cumulative statistics for all applications
                buildAbsoluteStats("total.requests", totalReqDelta);
                buildAbsoluteStats("total.avg_proc_time", appCount == 0 ? 0 : totalAvgProcTime / appCount);
            }
        }
        logger.info("app stats collected in " + (System.currentTimeMillis() - t) + "ms.");
    }
}
