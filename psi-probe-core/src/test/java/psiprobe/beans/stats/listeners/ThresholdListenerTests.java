/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.beans.stats.listeners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class ThresholdListenerTests.
 */
class ThresholdListenerTests {

  /** The default threshold. */
  private final long defaultThreshold = 10;

  /** The listener. */
  private final MockThresholdListener listener = new MockThresholdListener(defaultThreshold);

  /** The below threshold. */
  private final StatsCollectionEvent belowThreshold = new StatsCollectionEvent("test", 0, 0);

  /** The above threshold. */
  private final StatsCollectionEvent aboveThreshold = new StatsCollectionEvent("test", 0, 20);

  /**
   * Test first below threshold.
   */
  @Test
  void firstBelowThresholdTest() {
    listener.reset();
    listener.statsCollected(belowThreshold);
    Assertions.assertTrue(listener.isRemainedBelowThreshold());
  }

  /**
   * Test first above threshold.
   */
  @Test
  void firstAboveThresholdTest() {
    listener.reset();
    listener.statsCollected(aboveThreshold);
    Assertions.assertTrue(listener.isCrossedAboveThreshold());
  }

  /**
   * Test remain below threshold.
   */
  @Test
  void remainBelowThresholdTest() {
    listener.reset();
    listener.statsCollected(belowThreshold);
    listener.statsCollected(belowThreshold);
    Assertions.assertTrue(listener.isRemainedBelowThreshold());
  }

  /**
   * Test remain above threshold.
   */
  @Test
  void remainAboveThresholdTest() {
    listener.reset();
    listener.statsCollected(aboveThreshold);
    listener.statsCollected(aboveThreshold);
    Assertions.assertTrue(listener.isRemainedAboveThreshold());
  }

  /**
   * Test crossed below threshold.
   */
  @Test
  void crossedBelowThresholdTest() {
    listener.reset();
    listener.statsCollected(aboveThreshold);
    listener.statsCollected(belowThreshold);
    Assertions.assertTrue(listener.isCrossedBelowThreshold());
  }

  /**
   * Test crossed above threshold.
   */
  @Test
  void crossedAboveThresholdTest() {
    listener.reset();
    listener.statsCollected(belowThreshold);
    listener.statsCollected(aboveThreshold);
    Assertions.assertTrue(listener.isCrossedAboveThreshold());
  }

  /**
   * The listener interface for receiving mockThreshold events. The class that is interested in
   * processing a mockThreshold event implements this interface, and the object created with that
   * class is registered with a component using the component's
   * <code>addMockThresholdListener</code> method. When the mockThreshold event occurs, that
   * object's appropriate method is invoked.
   */
  public static class MockThresholdListener extends AbstractThresholdListener {

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
      crossedAboveThreshold = true;
    }

    @Override
    protected void crossedBelowThreshold(StatsCollectionEvent sce) {
      resetFlags();
      crossedBelowThreshold = true;
    }

    @Override
    protected void remainedAboveThreshold(StatsCollectionEvent sce) {
      resetFlags();
      remainedAboveThreshold = true;
    }

    @Override
    protected void remainedBelowThreshold(StatsCollectionEvent sce) {
      resetFlags();
      remainedBelowThreshold = true;
    }

    @Override
    public long getThreshold(String name) {
      return threshold;
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
      crossedAboveThreshold = false;
      crossedBelowThreshold = false;
      remainedAboveThreshold = false;
      remainedBelowThreshold = false;
    }

    /**
     * Checks if is crossed above threshold.
     *
     * @return true, if is crossed above threshold
     */
    public boolean isCrossedAboveThreshold() {
      return crossedAboveThreshold;
    }

    /**
     * Checks if is crossed below threshold.
     *
     * @return true, if is crossed below threshold
     */
    public boolean isCrossedBelowThreshold() {
      return crossedBelowThreshold;
    }

    /**
     * Checks if is remained above threshold.
     *
     * @return true, if is remained above threshold
     */
    public boolean isRemainedAboveThreshold() {
      return remainedAboveThreshold;
    }

    /**
     * Checks if is remained below threshold.
     *
     * @return true, if is remained below threshold
     */
    public boolean isRemainedBelowThreshold() {
      return remainedBelowThreshold;
    }

  }

}
