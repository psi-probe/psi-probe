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

import org.jfree.data.xy.XYDataItem;
import org.jstripe.tomcat.probe.Utils;
import org.jstripe.tomcat.probe.model.stats.StatsCollection;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BaseStatsCollectorBean {

    private StatsCollection statsCollection;
    private int maxSeries = 240;
    private Map previousData = new TreeMap();

    public StatsCollection getStatsCollection() {
        return statsCollection;
    }

    public void setStatsCollection(StatsCollection statsCollection) {
        this.statsCollection = statsCollection;
    }

    public int getMaxSeries() {
        return maxSeries;
    }

    public void setMaxSeries(int maxSeries) {
        this.maxSeries = maxSeries;
    }

    protected void buildDeltaStats(String name, long value) {
        buildDeltaStats(name, value, System.currentTimeMillis());
    }

    protected void buildDeltaStats(String name, long value, long time) {
        if (statsCollection != null) {
            buildAbsoluteStats(name, value - Utils.toLong((Long) previousData.get(name), -1), time);
            previousData.put(name, new Long(value));
        }
    }

    protected void buildAbsoluteStats(String name, long value) {
        buildAbsoluteStats(name, value, System.currentTimeMillis());
    }


    protected void buildAbsoluteStats(String name, long value, long time) {
        List stats = statsCollection.getStats(name);
        if (stats == null) {
            statsCollection.newStats(name, maxSeries);
        } else {
            stats.add(new XYDataItem(time, value));
            houseKeepStats(stats);
        }
    }

    private class Entry {
        long time;
        long value;
    }

    /**
     * If there is a value indicating the accumulated amount of time spent on something it is possible to build a
     * series of values representing the percentage of time spent on doing something. For example:
     *
     * at point T1 the system has spent A milliseconds performing tasks
     * at point T2 the system has spent B milliseconds performing tasks
     *
     * so between in a timeframe T2-T1 the system spent B-A milliseconds being busy. Thus (B - A)/(T2 - T1) * 100
     * is the percentage of all time the system spent being busy.
     *
     * @param name
     * @param value time in milliseconds
     * @param time
     */
    protected void buildTimePercentageStats(String name, long value, long time) {
        Entry entry = (Entry) previousData.get(name);
        if (entry == null) {
            entry = new Entry();
            entry.value = value;
            entry.time = time;
            previousData.put(name, entry);
        } else {
            double valueDelta = value - entry.value;
            double timeDelta = time - entry.time;
            double statValue = valueDelta * 100 / timeDelta;
            List stats = statsCollection.getStats(name);
            if (stats == null) stats = statsCollection.newStats(name, maxSeries);
            stats.add(stats.size(), new XYDataItem(time, statValue));
            houseKeepStats(stats);
        }
    }

    private void houseKeepStats(List stats) {
        while (stats.size() > maxSeries) stats.remove(0);
    }
}
