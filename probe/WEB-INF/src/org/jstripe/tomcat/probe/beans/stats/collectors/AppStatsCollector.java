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
import org.jstripe.tomcat.probe.tools.ApplicationUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Collects application statistics
 * <p/>
 * Author: Andy Shapoval
 */
public class AppStatsCollector extends BaseStatsCollectorBean {
    private Log logger = LogFactory.getLog(BaseStatsCollectorBean.class);
    private ContainerWrapperBean containerWrapper;

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
            List ctxs = getContainerWrapper().getTomcatContainer().findContexts();
            for (Iterator i = ctxs.iterator(); i.hasNext();) {
                Context ctx = (Context) i.next();
                if (ctx != null && ctx.getName() != null) {
                    Application app = new Application();
                    ApplicationUtils.collectApplicationServletStats(ctx, app);
                    String statName = "app." + ctx.getName();
                    buildDeltaStats(statName + ".requests", app.getRequestCount());
                    buildDeltaStats(statName + ".errors", app.getErrorCount());
                    buildAbsoluteStats(statName + ".avg_proc_time", app.getAvgTime());
                }
            }
        }
        logger.info("app stats collected in " + (System.currentTimeMillis() - t) + "ms.");
    }
}
