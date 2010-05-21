package com.googlecode.psiprobe.controllers;

import com.googlecode.psiprobe.beans.stats.collectors.AppStatsCollector;

/**
 *
 * @author <a href="mailto:mlewis@itos.uga.edu">Mark Lewis</a>
 */
public class ResetAppStatsController extends NoSelfContextHandlerController {

    private AppStatsCollector statsCollector;

    public AppStatsCollector getStatsCollector() {
        return statsCollector;
    }

    public void setStatsCollector(AppStatsCollector statsCollector) {
        this.statsCollector = statsCollector;
    }

    protected void executeAction(String contextName) throws Exception {
        statsCollector.reset(contextName);
    }

}
