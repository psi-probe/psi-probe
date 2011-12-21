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
import java.util.LinkedList;

/**
 *
 * @author Mark Lewis
 * 
 * @see <a href="http://nagios.sourceforge.net/docs/3_0/flapping.html">Detection and Handling of State Flapping (nagios)</a>
 */
public abstract class FlapListener extends ThresholdListener {

    public static final int DEFAULT_FLAP_INTERVAL = 20;
    public static final float DEFAULT_FLAP_START_THRESHOLD = 0.2f;
    public static final float DEFAULT_FLAP_STOP_THRESHOLD = 0.5f;
    public static final float DEFAULT_FLAP_LOW_WEIGHT = 1.0f;
    public static final float DEFAULT_FLAP_HIGH_WEIGHT = 1.0f;

    private HashMap/*String, LinkedList*/ flaps = new HashMap();
    private HashMap/*String, Boolean*/ flappingStates = new HashMap();

    protected abstract void flappingStarted(StatsCollectionEvent sce);

    protected abstract void aboveThresholdFlappingStopped(StatsCollectionEvent sce);

    protected abstract void belowThresholdFlappingStopped(StatsCollectionEvent sce);

    protected abstract void aboveThresholdNotFlapping(StatsCollectionEvent sce);

    protected abstract void belowThresholdNotFlapping(StatsCollectionEvent sce);

    protected void crossedAboveThreshold(StatsCollectionEvent sce) {
        statsCollected(sce, true, true);
    }

    protected void crossedBelowThreshold(StatsCollectionEvent sce) {
        statsCollected(sce, true, false);
    }

    protected void remainedAboveThreshold(StatsCollectionEvent sce) {
        statsCollected(sce, false, true);
    }

    protected void remainedBelowThreshold(StatsCollectionEvent sce) {
        statsCollected(sce, false, false);
    }

    public void reset() {
        flaps.clear();
        flappingStates.clear();
        super.reset();
    }
    
    protected void statsCollected(StatsCollectionEvent sce, boolean flap, boolean above) {
        String name = sce.getName();
        boolean flappingStateChanged = checkFlappingStateChanged(name, flap);
        boolean flappingState = getFlappingState(name);
        if (flappingStateChanged) {
            if (flappingState) {
                flappingStarted(sce);
            } else if (above) {
                aboveThresholdFlappingStopped(sce);
            } else {
                belowThresholdFlappingStopped(sce);
            }
        } else if (!flappingState) {
            if (above) {
                aboveThresholdNotFlapping(sce);
            } else {
                belowThresholdNotFlapping(sce);
            }
        }
    }

    protected boolean checkFlappingStateChanged(String name, boolean flap) {
        addFlap(name, flap);
        boolean oldFlappingState = getFlappingState(name);
        float transitionPercent = calculateStateTransitionPercentage(name, oldFlappingState);
        boolean newFlappingState;
        if (oldFlappingState) {
            newFlappingState = (transitionPercent <= getFlappingStopThreshold(name));
        } else {
            newFlappingState = (transitionPercent > getFlappingStartThreshold(name));
        }
        setFlappingState(name, newFlappingState);
        return oldFlappingState != newFlappingState;
    }

    protected float calculateStateTransitionPercentage(String name, boolean flapping) {
        int flapInterval = getFlapInterval(name);
        LinkedList list = getFlaps(name);
        float lowWeight = getFlapLowWeight(name);
        float highWeight = getFlapHighWeight(name);
        float weightRange = highWeight - lowWeight;
        float result = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            boolean thisFlap = ((Boolean) list.get(i)).booleanValue();
            if (flapping != thisFlap) {
                float weight = lowWeight + (weightRange * i / (flapInterval - 1));
                result += weight;
            }
        }
        return result / flapInterval;
    }

    protected void addFlap(String name, boolean flap) {
        int flapInterval = getFlapInterval(name);
        LinkedList list = getFlaps(name);
        Boolean value = Boolean.valueOf(flap);
        list.addLast(value);
        while (list.size() > flapInterval) {
            list.removeFirst();
        }
    }

    protected boolean getFlappingState(String name) {
        Boolean flapping = (Boolean) flappingStates.get(name);
        if (flapping == null) {
            flapping = Boolean.FALSE;
            setFlappingState(name, false);
        }
        return flapping.booleanValue();
    }

    protected void setFlappingState(String name, boolean flapping) {
        flappingStates.put(name, Boolean.valueOf(flapping));
    }

    protected LinkedList getFlaps(String name) {
        LinkedList list = (LinkedList) flaps.get(name);
        if (list == null) {
            list = new LinkedList();
            flaps.put(name, list);
        }
        return list;
    }

    protected int getFlapInterval(String name) {
        String flapInterval = getPropertyValue(name, "flapInterval");
        return Utils.toInt(flapInterval, DEFAULT_FLAP_INTERVAL);
    }

    protected float getFlappingStartThreshold(String name) {
        String flapStartThreshold = getPropertyValue(name, "flapStartThreshold");
        return Utils.toFloat(flapStartThreshold, DEFAULT_FLAP_START_THRESHOLD);
    }

    protected float getFlappingStopThreshold(String name) {
        String flapStopThreshold = getPropertyValue(name, "flapStopThreshold");
        return Utils.toFloat(flapStopThreshold, DEFAULT_FLAP_STOP_THRESHOLD);
    }

    protected float getFlapLowWeight(String name) {
        String flapLowWeight = getPropertyValue(name, "flapLowWeight");
        return Utils.toFloat(flapLowWeight, DEFAULT_FLAP_LOW_WEIGHT);
    }
    
    protected float getFlapHighWeight(String name) {
        String flapHighWeight = getPropertyValue(name, "flapHighWeight");
        return Utils.toFloat(flapHighWeight, DEFAULT_FLAP_HIGH_WEIGHT);
    }

}
