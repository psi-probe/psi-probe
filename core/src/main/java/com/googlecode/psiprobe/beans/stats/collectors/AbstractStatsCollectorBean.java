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

import com.googlecode.psiprobe.Utils;
import com.googlecode.psiprobe.beans.stats.listeners.StatsCollectionEvent;
import com.googlecode.psiprobe.beans.stats.listeners.StatsCollectionListener;
import com.googlecode.psiprobe.model.stats.StatsCollection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jfree.data.xy.XYDataItem;

public abstract class AbstractStatsCollectorBean {

    private StatsCollection statsCollection;
    private int maxSeries = 240;
    private List listeners;
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

    public List getListeners() {
        return listeners;
    }

    public void setListeners(List listeners) {
        this.listeners = listeners;
    }

    public abstract void collect() throws Exception;

    protected long buildDeltaStats(String name, long value) throws InterruptedException {
        return buildDeltaStats(name, value, System.currentTimeMillis());
    }

    protected long buildDeltaStats(String name, long value, long time) throws InterruptedException {
        long delta = 0;
        if (statsCollection != null) {
            long previousValue = Utils.toLong((Long) previousData.get(name), 0);
            delta = value - previousValue;
            delta = delta > 0 ? delta : 0;
            buildAbsoluteStats(name, delta, time);
            previousData.put(name, new Long(value));
        }
        return delta;
    }

    protected void buildAbsoluteStats(String name, long value) throws InterruptedException {
        buildAbsoluteStats(name, value, System.currentTimeMillis());
    }


    protected void buildAbsoluteStats(String name, long value, long time) throws InterruptedException {
        List stats = statsCollection.getStats(name);
        if (stats == null) {
            stats = statsCollection.newStats(name, maxSeries);
        } else {
            XYDataItem data = new XYDataItem(time, value);
            statsCollection.lockForUpdate();
            try {
                stats.add(data);
                houseKeepStats(stats);
            } finally {
                statsCollection.releaseLock();
            }
            if (listeners != null) {
                StatsCollectionEvent event = new StatsCollectionEvent(name, data);
                for (Iterator it = listeners.iterator(); it.hasNext();) {
                    Object o = it.next();
                    if (o instanceof StatsCollectionListener) {
                        StatsCollectionListener listener = (StatsCollectionListener) o;
                        listener.statsCollected(event);
                    }
                }
            }
        }
    }

    private class Entry {
        long time;
        long value;
    }

    /**
     * If there is a value indicating the accumulated amount of time spent on something it is possible to build a
     * series of values representing the percentage of time spent on doing something. For example:
     * <p/>
     * at point T1 the system has spent A milliseconds performing tasks
     * at point T2 the system has spent B milliseconds performing tasks
     * <p/>
     * so between in a timeframe T2-T1 the system spent B-A milliseconds being busy. Thus (B - A)/(T2 - T1) * 100
     * is the percentage of all time the system spent being busy.
     *
     * @param name
     * @param value time in milliseconds
     * @param time
     * @throws InterruptedException
     */
    protected void buildTimePercentageStats(String name, long value, long time) throws InterruptedException {
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
            statsCollection.lockForUpdate();
            try {
                List stats = statsCollection.getStats(name);
                if (stats == null) {
                    stats = statsCollection.newStats(name, maxSeries);
                }
                stats.add(stats.size(), new XYDataItem(time, statValue));
                houseKeepStats(stats);
            } finally {
                statsCollection.releaseLock();
            }
        }
    }

    protected void resetStats(String name) {
        statsCollection.resetStats(name);
    }

    private void houseKeepStats(List stats) {
        while (stats.size() > maxSeries) {
            stats.remove(0);
        }
    }
}
