/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.beans.stats.listeners;

import org.junit.Assert;
import org.junit.Test;

import psiprobe.beans.stats.listeners.StatsCollectionEvent;
import psiprobe.beans.stats.listeners.ThresholdListener;

/**
 * The Class ThresholdListenerTests.
 *
 * @author Mark Lewis
 */
public class ThresholdListenerTests {

  /** The default threshold. */
  private final long defaultThreshold = 10;

  /** The listener. */
  private MockThresholdListener listener = new MockThresholdListener(this.defaultThreshold);
  
  /** The below threshold. */
  private StatsCollectionEvent belowThreshold = new StatsCollectionEvent("test", 0, 0);
  
  /** The above threshold. */
  private StatsCollectionEvent aboveThreshold = new StatsCollectionEvent("test", 0, 20);

  /**
   * Test first below threshold.
   */
  @Test
  public void testFirstBelowThreshold() {
    this.listener.reset();
    this.listener.statsCollected(this.belowThreshold);
    Assert.assertTrue(this.listener.isRemainedBelowThreshold());
  }

  /**
   * Test first above threshold.
   */
  @Test
  public void testFirstAboveThreshold() {
    this.listener.reset();
    this.listener.statsCollected(this.aboveThreshold);
    Assert.assertTrue(this.listener.isCrossedAboveThreshold());
  }

  /**
   * Test remain below threshold.
   */
  @Test
  public void testRemainBelowThreshold() {
    this.listener.reset();
    this.listener.statsCollected(this.belowThreshold);
    this.listener.statsCollected(this.belowThreshold);
    Assert.assertTrue(this.listener.isRemainedBelowThreshold());
  }

  /**
   * Test remain above threshold.
   */
  @Test
  public void testRemainAboveThreshold() {
    this.listener.reset();
    this.listener.statsCollected(this.aboveThreshold);
    this.listener.statsCollected(this.aboveThreshold);
    Assert.assertTrue(this.listener.isRemainedAboveThreshold());
  }

  /**
   * Test crossed below threshold.
   */
  @Test
  public void testCrossedBelowThreshold() {
    this.listener.reset();
    this.listener.statsCollected(this.aboveThreshold);
    this.listener.statsCollected(this.belowThreshold);
    Assert.assertTrue(this.listener.isCrossedBelowThreshold());
  }

  /**
   * Test crossed above threshold.
   */
  @Test
  public void testCrossedAboveThreshold() {
    this.listener.reset();
    this.listener.statsCollected(this.belowThreshold);
    this.listener.statsCollected(this.aboveThreshold);
    Assert.assertTrue(this.listener.isCrossedAboveThreshold());
  }

  /**
   * The listener interface for receiving mockThreshold events.
   * The class that is interested in processing a mockThreshold
   * event implements this interface, and the object created
   * with that class is registered with a component using the
   * component's <code>addMockThresholdListener<code> method. When
   * the mockThreshold event occurs, that object's appropriate
   * method is invoked.
   *
   * @see MockThresholdEvent
   */
  public static class MockThresholdListener extends ThresholdListener {

    /** The threshold. */
    private final long threshold;

    /** The crossed above threshold. */
    private boolean crossedAboveThreshold;
    
    /** The crossed below threshold. */
    private boolean crossedBelowThreshold;
    
    /** The remained above threshold. */
    private boolean remainedAboveThreshold;
    
    /** The remained below threshold. */
    private boolean remainedBelowThreshold;

    /**
     * Instantiates a new mock threshold listener.
     *
     * @param threshold the threshold
     */
    public MockThresholdListener(long threshold) {
      this.threshold = threshold;
    }

    @Override
    protected void crossedAboveThreshold(StatsCollectionEvent sce) {
      resetFlags();
      this.crossedAboveThreshold = true;
    }

    @Override
    protected void crossedBelowThreshold(StatsCollectionEvent sce) {
      resetFlags();
      this.crossedBelowThreshold = true;
    }

    @Override
    protected void remainedAboveThreshold(StatsCollectionEvent sce) {
      resetFlags();
      this.remainedAboveThreshold = true;
    }

    @Override
    protected void remainedBelowThreshold(StatsCollectionEvent sce) {
      resetFlags();
      this.remainedBelowThreshold = true;
    }

    @Override
    public long getThreshold(String name) {
      return this.threshold;
    }

    @Override
    public void reset() {
      resetFlags();
      super.reset();
    }

    /**
     * Reset flags.
     */
    public void resetFlags() {
      this.crossedAboveThreshold = false;
      this.crossedBelowThreshold = false;
      this.remainedAboveThreshold = false;
      this.remainedBelowThreshold = false;
    }

    /**
     * Checks if is crossed above threshold.
     *
     * @return true, if is crossed above threshold
     */
    public boolean isCrossedAboveThreshold() {
      return this.crossedAboveThreshold;
    }

    /**
     * Checks if is crossed below threshold.
     *
     * @return true, if is crossed below threshold
     */
    public boolean isCrossedBelowThreshold() {
      return this.crossedBelowThreshold;
    }

    /**
     * Checks if is remained above threshold.
     *
     * @return true, if is remained above threshold
     */
    public boolean isRemainedAboveThreshold() {
      return this.remainedAboveThreshold;
    }

    /**
     * Checks if is remained below threshold.
     *
     * @return true, if is remained below threshold
     */
    public boolean isRemainedBelowThreshold() {
      return this.remainedBelowThreshold;
    }

  }

}
