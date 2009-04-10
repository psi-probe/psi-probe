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
package org.jstripe.tomcat.probe.beans;

import org.jstripe.tomcat.probe.model.stats.StatsCollection;
import org.jfree.data.xy.XYDataItem;

import java.util.List;

/**
 * Retrieves application statistics from stats collection
 * @author Andy Shapoval
 */
public class AppStatsAccessor {
    private StatsCollection statsCollection;

    public StatsCollection getStatsCollection() {
        return statsCollection;
    }

    public void setStatsCollection(StatsCollection statsCollection) {
        this.statsCollection = statsCollection;
    }

    /**
     * Retrieves the value of application average processing (response) time from a stats collection
     * @param appName application name
     * @return average processing (response) time for the application
     */
    public long getAvgProcTime(String appName) {
        return getLastStatsValue("app." + appName + ".avg_proc_time");
    }

    /**
     * Retrieves the value of total average response time from stats collection
     * @param appName
     * @return average processing time of all applications
     */
    public long getTotalAvgProcTime(String appName) {
        return getLastStatsValue("total.avg_proc_time");
    }

    private long getLastStatsValue(String statsName) {
        long statsValue = 0;

        StatsCollection statsCollection = getStatsCollection();
        if (statsCollection != null) {
            List stats = statsCollection.getStats(statsName);
            if (stats != null && ! stats.isEmpty()) {
                XYDataItem xy = (XYDataItem) stats.get(stats.size() - 1);
                if (xy != null && xy.getY() != null) {
                    statsValue = xy.getY().longValue();
                }
            }
        }

        return statsValue;
    }
}
