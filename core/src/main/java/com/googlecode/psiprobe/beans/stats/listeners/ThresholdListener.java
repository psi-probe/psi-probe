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
package com.googlecode.psiprobe.beans.stats.listeners;

import com.googlecode.psiprobe.Utils;
import java.util.HashMap;

/**
 *
 * @author Mark Lewis
 */
public abstract class ThresholdListener extends AbstractStatsCollectionListener {
    
    public static final long DEFAULT_THRESHOLD = -1;
    public static final long DEFAULT_VALUE = -1;
    
    private HashMap/*String, Long*/ previousValues = new HashMap();

    protected abstract void crossedAboveThreshold(StatsCollectionEvent sce);

    protected abstract void crossedBelowThreshold(StatsCollectionEvent sce);

    protected abstract void remainedAboveThreshold(StatsCollectionEvent sce);

    protected abstract void remainedBelowThreshold(StatsCollectionEvent sce);

    public void statsCollected(StatsCollectionEvent sce) {
        String name = sce.getName();
        long value = sce.getValue();
        if (isValueAboveThreshold(sce)) {
            if (isPreviousValueAboveThreshold(sce)) {
                remainedAboveThreshold(sce);
            } else {
                crossedAboveThreshold(sce);
            }
        } else {
            if (isPreviousValueAboveThreshold(sce)) {
                crossedBelowThreshold(sce);
            } else {
                remainedBelowThreshold(sce);
            }
        }
        setPreviousValue(name, value);
    }

    public void reset() {
        previousValues.clear();
        super.reset();
    }

    protected boolean isPreviousValueAboveThreshold(StatsCollectionEvent sce) {
        String name = sce.getName();
        long threshold = getThreshold(name);
        long previousValue = getPreviousValue(name);
        if (threshold != DEFAULT_THRESHOLD) {
            return previousValue != DEFAULT_VALUE && previousValue > threshold;
        } else {
            throw new RuntimeException("Required property " + getPropertyKey(name, "threshold") + " is not defined");
        }
    }

    protected boolean isValueAboveThreshold(StatsCollectionEvent sce) {
        String name = sce.getName();
        long value = sce.getValue();
        long threshold = getThreshold(name);
        if (threshold != DEFAULT_THRESHOLD) {
            return value > threshold;
        } else {
            throw new RuntimeException("Required property " + getPropertyKey(name, "threshold") + " is not defined");
        }
    }
    
    protected long getThreshold(String name) {
        String threshold = getPropertyValue(name, "threshold");
        return Utils.toLong(threshold, DEFAULT_THRESHOLD);
    }

    protected long getPreviousValue(String name) {
        Long l = (Long) previousValues.get(name);
        return Utils.toLong(l, DEFAULT_VALUE);
    }

    protected void setPreviousValue(String name, long previousValue) {
        Long l = new Long(previousValue);
        previousValues.put(name, l);
    }

}
