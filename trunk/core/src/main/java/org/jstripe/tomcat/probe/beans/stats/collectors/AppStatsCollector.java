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
package org.jstripe.tomcat.probe.beans.stats.collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.catalina.Context;
import org.jstripe.tomcat.probe.beans.ContainerWrapperBean;
import org.jstripe.tomcat.probe.TomcatContainer;
import org.jstripe.tomcat.probe.tools.ApplicationUtils;
import org.jstripe.tomcat.probe.model.Application;

import java.util.List;
import java.util.Iterator;

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

        long currentTime = System.currentTimeMillis();

        if (containerWrapper == null) {
            logger.error("Cannot collect application stats. Container wrapper is not set.");
        } else {
            TomcatContainer tomcatContainer = getContainerWrapper().getTomcatContainer();

            // check if the containerWtapper has been initialized
            if (tomcatContainer != null) {
                long totalReqDelta = 0;
                long totalAvgProcTime = 0;

                int participatingAppCount = 0;

                List contexts = tomcatContainer.findContexts();
                for (Iterator i = contexts.iterator(); i.hasNext(); ) {
                    Context ctx = (Context) i.next();

                    if (ctx != null && ctx.getName() != null) {
                        Application app = new Application();
                        ApplicationUtils.collectApplicationServletStats(ctx, app);

                        String appName = "".equals(ctx.getName()) ? "/" : ctx.getName();

                        long reqDelta = buildDeltaStats("app.requests." + appName, app.getRequestCount(), currentTime);
                        long procTimeDelta = buildDeltaStats("app.proc_time." + appName, app.getProcessingTime(), currentTime);
                        buildDeltaStats("app.errors." + appName, app.getErrorCount());

                        long avgProcTime = reqDelta == 0 ? 0 : procTimeDelta / reqDelta;
                        buildAbsoluteStats( "app.avg_proc_time." + appName, avgProcTime, currentTime);

                        // make sure applications that did not serve any requests
                        // do not participate in average response time equasion thus diluting the value
                        if (reqDelta > 0) {
                            totalReqDelta += reqDelta;
                            totalAvgProcTime += avgProcTime;
                            participatingAppCount++;
                        }
                    }
                }
                // build totals for all applications
                buildAbsoluteStats("total.requests", totalReqDelta, currentTime);
                buildAbsoluteStats("total.avg_proc_time", participatingAppCount == 0 ? 0 : totalAvgProcTime / participatingAppCount, currentTime);
            }
        }
        logger.debug("app stats collected in " + (System.currentTimeMillis() - currentTime) + "ms.");
    }
}
