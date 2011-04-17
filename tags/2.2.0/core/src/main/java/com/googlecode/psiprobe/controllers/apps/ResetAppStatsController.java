package com.googlecode.psiprobe.controllers.apps;

import com.googlecode.psiprobe.beans.stats.collectors.AppStatsCollectorBean;

/**
 *
 * @author Mark Lewis
 */
public class ResetAppStatsController extends NoSelfContextHandlerController {

    private AppStatsCollectorBean statsCollector;

    public AppStatsCollectorBean getStatsCollector() {
        return statsCollector;
    }

    public void setStatsCollector(AppStatsCollectorBean statsCollector) {
        this.statsCollector = statsCollector;
    }

    protected void executeAction(String contextName) throws Exception {
        statsCollector.reset(contextName);
    }

}
