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
public class ThresholdListenerTests extends TestCase {

    private final long defaultThreshold = 10;

    private MockThresholdListener listener = new MockThresholdListener(defaultThreshold);
    private StatsCollectionEvent belowThreshold = new StatsCollectionEvent("test", 0, 0);
    private StatsCollectionEvent aboveThreshold = new StatsCollectionEvent("test", 0, 20);

    public void testFirstBelowThreshold() {
        listener.reset();
        listener.statsCollected(belowThreshold);
        Assert.assertTrue(listener.isRemainedBelowThreshold());
    }
    
    public void testFirstAboveThreshold() {
        listener.reset();
        listener.statsCollected(aboveThreshold);
        Assert.assertTrue(listener.isCrossedAboveThreshold());
    }
    
    public void testRemainBelowThreshold() {
        listener.reset();
        listener.statsCollected(belowThreshold);
        listener.statsCollected(belowThreshold);
        Assert.assertTrue(listener.isRemainedBelowThreshold());
    }
    
    public void testRemainAboveThreshold() {
        listener.reset();
        listener.statsCollected(aboveThreshold);
        listener.statsCollected(aboveThreshold);
        Assert.assertTrue(listener.isRemainedAboveThreshold());
    }
    
    public void testCrossedBelowThreshold() {
        listener.reset();
        listener.statsCollected(aboveThreshold);
        listener.statsCollected(belowThreshold);
        Assert.assertTrue(listener.isCrossedBelowThreshold());
    }
    
    public void testCrossedAboveThreshold() {
        listener.reset();
        listener.statsCollected(belowThreshold);
        listener.statsCollected(aboveThreshold);
        Assert.assertTrue(listener.isCrossedAboveThreshold());
    }
    
    public static class MockThresholdListener extends ThresholdListener {

        private final long threshold;

        private boolean crossedAboveThreshold;
        private boolean crossedBelowThreshold;
        private boolean remainedAboveThreshold;
        private boolean remainedBelowThreshold;

        public MockThresholdListener(long threshold) {
            this.threshold = threshold;
        }
        
        protected void crossedAboveThreshold(StatsCollectionEvent sce) {
            resetFlags();
            crossedAboveThreshold = true;
        }

        protected void crossedBelowThreshold(StatsCollectionEvent sce) {
            resetFlags();
            crossedBelowThreshold = true;
        }

        protected void remainedAboveThreshold(StatsCollectionEvent sce) {
            resetFlags();
            remainedAboveThreshold = true;
        }

        protected void remainedBelowThreshold(StatsCollectionEvent sce) {
            resetFlags();
            remainedBelowThreshold = true;
        }

        public long getThreshold(String name) {
            return threshold;
        }

        public void reset() {
            resetFlags();
            super.reset();
        }

        public void resetFlags() {
            crossedAboveThreshold = false;
            crossedBelowThreshold = false;
            remainedAboveThreshold = false;
            remainedBelowThreshold = false;
        }

        public boolean isCrossedAboveThreshold() {
            return crossedAboveThreshold;
        }

        public boolean isCrossedBelowThreshold() {
            return crossedBelowThreshold;
        }

        public boolean isRemainedAboveThreshold() {
            return remainedAboveThreshold;
        }

        public boolean isRemainedBelowThreshold() {
            return remainedBelowThreshold;
        }

    }

}
