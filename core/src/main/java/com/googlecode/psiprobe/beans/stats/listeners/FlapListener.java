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

    private int defaultFlapInterval;
    private float defaultFlapStartThreshold;
    private float defaultFlapStopThreshold;
    private float defaultFlapLowWeight;
    private float defaultFlapHighWeight;
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
    
    protected void statsCollected(StatsCollectionEvent sce, boolean crossedThreshold, boolean above) {
        String name = sce.getName();
        boolean flappingStateChanged = checkFlappingStateChanged(name, crossedThreshold);
        boolean flappingState = getFlappingState(name);
        if (flappingStateChanged) {
            if (flappingState) {
                flappingStarted(sce);
            } else if (above) {
                aboveThresholdFlappingStopped(sce);
            } else {
                belowThresholdFlappingStopped(sce);
            }
        } else if (crossedThreshold) {
            if (above) {
                aboveThresholdNotFlapping(sce);
            } else {
                belowThresholdNotFlapping(sce);
            }
        }
    }

    protected boolean checkFlappingStateChanged(String name, boolean crossedThreshold) {
        addFlap(name, crossedThreshold);
        boolean oldFlappingState = getFlappingState(name);
        float transitionPercent = calculateStateTransitionPercentage(name, oldFlappingState);
        boolean newFlappingState;
        if (oldFlappingState) {
            newFlappingState = (transitionPercent <= getFlapStopThreshold(name));
        } else {
            newFlappingState = (transitionPercent > getFlapStartThreshold(name));
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
        String interval = getPropertyValue(name, "flapInterval");
        return Utils.toInt(interval, getDefaultFlapInterval());
    }

    protected float getFlapStartThreshold(String name) {
        String startThreshold = getPropertyValue(name, "flapStartThreshold");
        return Utils.toFloat(startThreshold, getDefaultFlapStartThreshold());
    }

    protected float getFlapStopThreshold(String name) {
        String stopThreshold = getPropertyValue(name, "flapStopThreshold");
        return Utils.toFloat(stopThreshold, getDefaultFlapStopThreshold());
    }

    protected float getFlapLowWeight(String name) {
        String lowWeight = getPropertyValue(name, "flapLowWeight");
        return Utils.toFloat(lowWeight, getDefaultFlapLowWeight());
    }
    
    protected float getFlapHighWeight(String name) {
        String highWeight = getPropertyValue(name, "flapHighWeight");
        return Utils.toFloat(highWeight, getDefaultFlapHighWeight());
    }

    public int getDefaultFlapInterval() {
        return defaultFlapInterval;
    }

    public void setDefaultFlapInterval(int defaultFlapInterval) {
        this.defaultFlapInterval = defaultFlapInterval;
    }

    public float getDefaultFlapStartThreshold() {
        return defaultFlapStartThreshold;
    }

    public void setDefaultFlapStartThreshold(float defaultFlapStartThreshold) {
        this.defaultFlapStartThreshold = defaultFlapStartThreshold;
    }

    public float getDefaultFlapStopThreshold() {
        return defaultFlapStopThreshold;
    }

    public void setDefaultFlapStopThreshold(float defaultFlapStopThreshold) {
        this.defaultFlapStopThreshold = defaultFlapStopThreshold;
    }

    public float getDefaultFlapLowWeight() {
        return defaultFlapLowWeight;
    }

    public void setDefaultFlapLowWeight(float defaultFlapLowWeight) {
        this.defaultFlapLowWeight = defaultFlapLowWeight;
    }

    public float getDefaultFlapHighWeight() {
        return defaultFlapHighWeight;
    }

    public void setDefaultFlapHighWeight(float defaultFlapHighWeight) {
        this.defaultFlapHighWeight = defaultFlapHighWeight;
    }

}
