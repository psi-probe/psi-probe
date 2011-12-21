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

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author Mark Lewis
 */
public class FlapListenerTests extends TestCase {

    private final int defaultThreshold = 10;
    private final int defaultInterval = 10;
    private final float defaultStartThreshold = 0.19f;
    private final float defaultStopThreshold = 0.49f;

    private MockFlapListener listener = new MockFlapListener(defaultThreshold, defaultInterval, defaultStartThreshold, defaultStopThreshold);
    private StatsCollectionEvent belowThreshold = new StatsCollectionEvent("test", 0, 0);
    private StatsCollectionEvent aboveThreshold = new StatsCollectionEvent("test", 0, 20);

    protected void fill(StatsCollectionEvent sce) {
        listener.reset();
        for (int i = 0; i < defaultInterval; i++) {
            listener.statsCollected(sce);
        }
    }
    
    protected void add(StatsCollectionEvent sce, int quantity) {
        for (int i = 0; i < quantity; i++) {
            listener.statsCollected(sce);
        }
    }
    
    public void testBelowThresholdNotFlapping() {
        listener.reset();
        for (int i = 0; i < defaultInterval; i++) {
            listener.statsCollected(belowThreshold);
            Assert.assertTrue(listener.isBelowThresholdNotFlapping());
        }
    }

    public void testAboveThresholdNotFlapping() {
        listener.reset();
        for (int i = 0; i < defaultInterval; i++) {
            listener.statsCollected(aboveThreshold);
            Assert.assertTrue(listener.isAboveThresholdNotFlapping());
        }
    }

    public void testFlappingStarted() {
        fill(belowThreshold);
        listener.statsCollected(aboveThreshold);
        listener.statsCollected(belowThreshold);
        Assert.assertTrue(listener.isFlappingStarted());
    }

    public void testFlappingStarted2() {
        fill(aboveThreshold);
        listener.statsCollected(belowThreshold);
        listener.statsCollected(aboveThreshold);
        Assert.assertTrue(listener.isFlappingStarted());
    }

    public void testBelowThresholdFlappingStoppedBelow() {
        fill(belowThreshold);
        listener.statsCollected(aboveThreshold);
        listener.statsCollected(belowThreshold);
        Assert.assertTrue(listener.isFlappingStarted());
        add(belowThreshold, 5);
        Assert.assertTrue(listener.isBelowThresholdFlappingStopped());
    }

    public void testBelowThresholdFlappingStoppedAbove() {
        fill(belowThreshold);
        listener.statsCollected(aboveThreshold);
        listener.statsCollected(belowThreshold);
        Assert.assertTrue(listener.isFlappingStarted());
        add(aboveThreshold, 5);
        Assert.assertTrue(listener.isAboveThresholdFlappingStopped());
    }

    public void testAboveThresholdFlappingStoppedBelow() {
        fill(aboveThreshold);
        listener.statsCollected(belowThreshold);
        listener.statsCollected(aboveThreshold);
        Assert.assertTrue(listener.isFlappingStarted());
        add(belowThreshold, 5);
        Assert.assertTrue(listener.isBelowThresholdFlappingStopped());
    }

    public void testAboveThresholdFlappingStoppedAbove() {
        fill(aboveThreshold);
        listener.statsCollected(belowThreshold);
        listener.statsCollected(aboveThreshold);
        Assert.assertTrue(listener.isFlappingStarted());
        add(aboveThreshold, 5);
        Assert.assertTrue(listener.isAboveThresholdFlappingStopped());
    }

    public static class MockFlapListener extends FlapListener {

        private final long threshold;
        private final int flapInterval;
        private final float flappingStartThreshold;
        private final float flappingStopThreshold;
        
        private boolean flappingStarted;
        private boolean aboveThresholdFlappingStopped;
        private boolean belowThresholdFlappingStopped;
        private boolean aboveThresholdNotFlapping;
        private boolean belowThresholdNotFlapping;

        public MockFlapListener(long threshold, int flapInterval, float flappingStartThreshold, float flappingStopThreshold) {
            this.threshold = threshold;
            this.flapInterval = flapInterval;
            this.flappingStartThreshold = flappingStartThreshold;
            this.flappingStopThreshold = flappingStopThreshold;
        }
        
        protected void flappingStarted(StatsCollectionEvent sce) {
            resetFlags();
            flappingStarted = true;
        }

        protected void aboveThresholdFlappingStopped(StatsCollectionEvent sce) {
            resetFlags();
            aboveThresholdFlappingStopped = true;
        }

        protected void belowThresholdFlappingStopped(StatsCollectionEvent sce) {
            resetFlags();
            belowThresholdFlappingStopped = true;
        }

        protected void aboveThresholdNotFlapping(StatsCollectionEvent sce) {
            resetFlags();
            aboveThresholdNotFlapping = true;
        }

        protected void belowThresholdNotFlapping(StatsCollectionEvent sce) {
            resetFlags();
            belowThresholdNotFlapping = true;
        }
        
        public long getThreshold(String name) {
            return threshold;
        }

        protected int getFlapInterval(String name) {
            return flapInterval;
        }

        protected float getFlappingStartThreshold(String name) {
            return flappingStartThreshold;
        }

        protected float getFlappingStopThreshold(String name) {
            return flappingStopThreshold;
        }

        public void reset() {
            resetFlags();
            super.reset();
        }

        public void resetFlags() {
            flappingStarted = false;
            aboveThresholdFlappingStopped = false;
            belowThresholdFlappingStopped = false;
            aboveThresholdNotFlapping = false;
            belowThresholdNotFlapping = false;
        }

        public boolean isAboveThresholdFlappingStopped() {
            return aboveThresholdFlappingStopped;
        }

        public boolean isAboveThresholdNotFlapping() {
            return aboveThresholdNotFlapping;
        }

        public boolean isBelowThresholdFlappingStopped() {
            return belowThresholdFlappingStopped;
        }

        public boolean isBelowThresholdNotFlapping() {
            return belowThresholdNotFlapping;
        }

        public boolean isFlappingStarted() {
            return flappingStarted;
        }

    }

}
