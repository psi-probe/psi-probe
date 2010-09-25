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
package com.googlecode.psiprobe.beans.stats.collectors;

import com.googlecode.psiprobe.TomcatContainer;
import com.googlecode.psiprobe.beans.ContainerWrapperBean;
import com.googlecode.psiprobe.tools.ApplicationUtils;
import com.googlecode.psiprobe.model.Application;
import java.util.List;
import java.util.Iterator;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.catalina.Context;
import org.springframework.web.context.ServletContextAware;

/**
 * Collects application statistics
 * 
 * @author Andy Shapoval
 */
public class AppStatsCollectorBean extends AbstractStatsCollectorBean implements ServletContextAware {

    private Log logger = LogFactory.getLog(AppStatsCollectorBean.class);

    private ContainerWrapperBean containerWrapper;
    private ServletContext servletContext;
    private boolean selfIgnored;

    public ContainerWrapperBean getContainerWrapper() {
        return containerWrapper;
    }

    public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
        this.containerWrapper = containerWrapper;
    }

    public boolean isSelfIgnored() {
        return selfIgnored;
    }

    public void setSelfIgnored(boolean selfIgnored) {
        this.selfIgnored = selfIgnored;
    }

    protected ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
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
                            if (!excludeFromTotal(ctx)) {
                                totalReqDelta += reqDelta;
                                totalAvgProcTime += avgProcTime;
                                participatingAppCount++;
                            }
                        }
                    }
                }
                // build totals for all applications
                buildAbsoluteStats("total.requests", totalReqDelta, currentTime);
                buildAbsoluteStats("total.avg_proc_time", participatingAppCount == 0 ? 0 : totalAvgProcTime / participatingAppCount, currentTime);
            }
            logger.debug("app stats collected in " + (System.currentTimeMillis() - currentTime) + "ms.");
        }
    }

    private boolean excludeFromTotal(Context ctx) {
        return isSelfIgnored() && getServletContext().equals(ctx.getServletContext());
    }

    public void reset() {
        if (containerWrapper == null) {
            logger.error("Cannot reset application stats. Container wrapper is not set.");
        } else {
            TomcatContainer tomcatContainer = getContainerWrapper().getTomcatContainer();
            if (tomcatContainer != null) {
                List contexts = tomcatContainer.findContexts();
                for (Iterator i = contexts.iterator(); i.hasNext(); ) {
                    Context ctx = (Context) i.next();

                    if (ctx != null && ctx.getName() != null) {
                        String appName = "".equals(ctx.getName()) ? "/" : ctx.getName();
                        reset(appName);
                    }
                }
            }
        }
        resetStats("total.requests");
        resetStats("total.avg_proc_time");
    }

    public void reset(String appName) {
        resetStats("app.requests." + appName);
        resetStats("app.proc_time." + appName);
        resetStats("app.errors." + appName);
        resetStats("app.avg_proc_time." + appName);
    }

}
