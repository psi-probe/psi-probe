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
import com.googlecode.psiprobe.tools.SizeExpression;
import java.util.HashMap;

/**
 *
 * @author Mark Lewis
 */
public abstract class ThresholdListener extends AbstractStatsCollectionListener {
    
    public static final long DEFAULT_THRESHOLD = Long.MAX_VALUE;
    public static final long DEFAULT_VALUE = Long.MIN_VALUE;
    
    private HashMap/*String, Long*/ previousValues = new HashMap();
    private HashMap/*String, Boolean*/ seriesDisabled = new HashMap();

    protected abstract void crossedAboveThreshold(StatsCollectionEvent sce);

    protected abstract void crossedBelowThreshold(StatsCollectionEvent sce);

    protected abstract void remainedAboveThreshold(StatsCollectionEvent sce);

    protected abstract void remainedBelowThreshold(StatsCollectionEvent sce);

    public void statsCollected(StatsCollectionEvent sce) {
        String name = sce.getName();
        if (isSeriesDisabled(name)) {
            return;
        }
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
        return previousValue != DEFAULT_VALUE && previousValue > threshold;
    }

    protected boolean isValueAboveThreshold(StatsCollectionEvent sce) {
        String name = sce.getName();
        long value = sce.getValue();
        long threshold = getThreshold(name);
        return value > threshold;
    }
    
    protected long getThreshold(String name) {
        String threshold = getPropertyValue(name, "threshold");
        if (threshold == null && !isSeriesDisabled(name)) {
            logger.info("Required property " + getPropertyKey(name, "threshold") + " is not defined or inherited.  Disabling listener for \"" + name + "\" series.");
            setSeriesDisabled(name, true);
            return DEFAULT_THRESHOLD;
        } else {
            try {
                return SizeExpression.parse(threshold);
            } catch (NumberFormatException ex) {
                return DEFAULT_THRESHOLD;
            }
        }
    }

    protected long getPreviousValue(String name) {
        Long l = (Long) previousValues.get(name);
        return Utils.toLong(l, DEFAULT_VALUE);
    }

    protected void setPreviousValue(String name, long previousValue) {
        Long l = new Long(previousValue);
        previousValues.put(name, l);
    }

    protected boolean isSeriesDisabled(String name) {
        Boolean disabled = (Boolean) seriesDisabled.get(name);
        if (disabled == null) {
            disabled = Boolean.FALSE;
        }
        return disabled.booleanValue();
    }

    protected void setSeriesDisabled(String name, boolean disabled) {
        seriesDisabled.put(name, Boolean.valueOf(disabled));
    }

}
