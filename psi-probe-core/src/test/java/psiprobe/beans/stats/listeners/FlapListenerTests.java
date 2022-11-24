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
 * The Class FlapListenerTests.
 */
class FlapListenerTests {

  /** The default threshold. */
  private final int defaultThreshold = 10;

  /** The default interval. */
  private final int defaultInterval = 10;

  /** The default start threshold. */
  private final float defaultStartThreshold = 0.29F;

  /** The default stop threshold. */
  private final float defaultStopThreshold = 0.49F;

  /** The default low weight. */
  private final float defaultLowWeight = 1.0F;

  /** The default high weight. */
  private final float defaultHighWeight = 1.0F;

  /** The listener. */
  private MockFlapListener listener = new MockFlapListener(defaultThreshold, defaultInterval,
      defaultStartThreshold, defaultStopThreshold, defaultLowWeight, defaultHighWeight);

  /** The below threshold. */
  private StatsCollectionEvent belowThreshold = new StatsCollectionEvent("test", 0, 0);

  /** The above threshold. */
  private StatsCollectionEvent aboveThreshold = new StatsCollectionEvent("test", 0, 20);

  /**
   * Fill.
   *
   * @param sce the sce
   */
  private void fill(StatsCollectionEvent sce) {
    listener.reset();
    add(sce, defaultInterval);
  }

  /**
   * Adds the.
   *
   * @param sce the sce
   * @param quantity the quantity
   */
  private void add(StatsCollectionEvent sce, int quantity) {
    for (int i = 0; i < quantity; i++) {
      listener.statsCollected(sce);
    }
  }

  /**
   * Test below threshold not flapping.
   */
  @Test
  void testBelowThresholdNotFlapping() {
    listener.reset();
    listener.statsCollected(aboveThreshold);
    listener.statsCollected(belowThreshold);
    Assertions.assertTrue(listener.isBelowThresholdNotFlapping());
  }

  /**
   * Test above threshold not flapping.
   */
  @Test
  void testAboveThresholdNotFlapping() {
    listener.reset();
    listener.statsCollected(belowThreshold);
    listener.statsCollected(aboveThreshold);
    Assertions.assertTrue(listener.isAboveThresholdNotFlapping());
  }

  /**
   * Test still below threshold.
   */
  @Test
  void testStillBelowThreshold() {
    listener.reset();
    listener.statsCollected(belowThreshold);
    for (int i = 0; i < defaultInterval; i++) {
      listener.statsCollected(belowThreshold);
      Assertions.assertFalse(listener.isBelowThresholdNotFlapping());
    }
  }

  /**
   * Test still above threshold.
   */
  @Test
  void testStillAboveThreshold() {
    listener.reset();
    listener.statsCollected(aboveThreshold);
    for (int i = 0; i < defaultInterval; i++) {
      listener.statsCollected(aboveThreshold);
      Assertions.assertFalse(listener.isAboveThresholdNotFlapping());
    }
  }

  /**
   * Test flapping started.
   */
  @Test
  void testFlappingStarted() {
    fill(belowThreshold);
    listener.statsCollected(aboveThreshold);
    listener.statsCollected(belowThreshold);
    listener.statsCollected(aboveThreshold);
    Assertions.assertTrue(listener.isFlappingStarted());
  }

  /**
   * Test flapping started2.
   */
  @Test
  void testFlappingStarted2() {
    fill(aboveThreshold);
    listener.statsCollected(belowThreshold);
    listener.statsCollected(aboveThreshold);
    listener.statsCollected(belowThreshold);
    Assertions.assertTrue(listener.isFlappingStarted());
  }

  /**
   * Test below threshold flapping stopped below.
   */
  @Test
  void testBelowThresholdFlappingStoppedBelow() {
    fill(belowThreshold);
    listener.statsCollected(aboveThreshold);
    listener.statsCollected(belowThreshold);
    listener.statsCollected(aboveThreshold);
    Assertions.assertTrue(listener.isFlappingStarted());
    add(belowThreshold, 5);
    Assertions.assertTrue(listener.isBelowThresholdFlappingStopped());
  }

  /**
   * Test below threshold flapping stopped above.
   */
  @Test
  void testBelowThresholdFlappingStoppedAbove() {
    fill(belowThreshold);
    listener.statsCollected(aboveThreshold);
    listener.statsCollected(belowThreshold);
    listener.statsCollected(aboveThreshold);
    Assertions.assertTrue(listener.isFlappingStarted());
    add(aboveThreshold, 5);
    Assertions.assertTrue(listener.isAboveThresholdFlappingStopped());
  }

  /**
   * Test above threshold flapping stopped below.
   */
  @Test
  void testAboveThresholdFlappingStoppedBelow() {
    fill(aboveThreshold);
    listener.statsCollected(belowThreshold);
    listener.statsCollected(aboveThreshold);
    listener.statsCollected(belowThreshold);
    Assertions.assertTrue(listener.isFlappingStarted());
    add(belowThreshold, 5);
    Assertions.assertTrue(listener.isBelowThresholdFlappingStopped());
  }

  /**
   * Test above threshold flapping stopped above.
   */
  @Test
  void testAboveThresholdFlappingStoppedAbove() {
    fill(aboveThreshold);
    listener.statsCollected(belowThreshold);
    listener.statsCollected(aboveThreshold);
    listener.statsCollected(belowThreshold);
    Assertions.assertTrue(listener.isFlappingStarted());
    add(aboveThreshold, 5);
    Assertions.assertTrue(listener.isAboveThresholdFlappingStopped());
  }

  /**
   * The listener interface for receiving mockFlap events. The class that is interested in
   * processing a mockFlap event implements this interface, and the object created with that class
   * is registered with a component using the component's <code>addMockFlapListener</code> method.
   * When the mockFlap event occurs, that object's appropriate method is invoked.
   */
  public static class MockFlapListener extends AbstractFlapListener {

    /** The threshold. */
    private final long threshold;

    /** The flapping started. */
    private boolean flappingStarted;

    /** The above threshold flapping stopped. */
    private boolean aboveThresholdFlappingStopped;

    /** The below threshold flapping stopped. */
    private boolean belowThresholdFlappingStopped;

    /** The above threshold not flapping. */
    private boolean aboveThresholdNotFlapping;

    /** The below threshold not flapping. */
    private boolean belowThresholdNotFlapping;

    /**
     * Instantiates a new mock flap listener.
     *
     * @param threshold the threshold
     * @param flapInterval the flap interval
     * @param flapStartThreshold the flap start threshold
     * @param flapStopThreshold the flap stop threshold
     * @param lowWeight the low weight
     * @param highWeight the high weight
     */
    public MockFlapListener(long threshold, int flapInterval, float flapStartThreshold,
        float flapStopThreshold, float lowWeight, float highWeight) {

      this.threshold = threshold;
      setDefaultFlapInterval(flapInterval);
      setDefaultFlapStartThreshold(flapStartThreshold);
      setDefaultFlapStopThreshold(flapStopThreshold);
      setDefaultFlapLowWeight(lowWeight);
      setDefaultFlapHighWeight(highWeight);
    }

    @Override
    public void statsCollected(StatsCollectionEvent sce) {
      resetFlags();
      super.statsCollected(sce);
    }

    @Override
    protected void flappingStarted(StatsCollectionEvent sce) {
      flappingStarted = true;
    }

    @Override
    protected void aboveThresholdFlappingStopped(StatsCollectionEvent sce) {
      aboveThresholdFlappingStopped = true;
    }

    @Override
    protected void belowThresholdFlappingStopped(StatsCollectionEvent sce) {
      belowThresholdFlappingStopped = true;
    }

    @Override
    protected void aboveThresholdNotFlapping(StatsCollectionEvent sce) {
      aboveThresholdNotFlapping = true;
    }

    @Override
    protected void belowThresholdNotFlapping(StatsCollectionEvent sce) {
      belowThresholdNotFlapping = true;
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
      flappingStarted = false;
      aboveThresholdFlappingStopped = false;
      belowThresholdFlappingStopped = false;
      aboveThresholdNotFlapping = false;
      belowThresholdNotFlapping = false;
    }

    /**
     * Checks if is above threshold flapping stopped.
     *
     * @return true, if is above threshold flapping stopped
     */
    public boolean isAboveThresholdFlappingStopped() {
      return aboveThresholdFlappingStopped;
    }

    /**
     * Checks if is above threshold not flapping.
     *
     * @return true, if is above threshold not flapping
     */
    public boolean isAboveThresholdNotFlapping() {
      return aboveThresholdNotFlapping;
    }

    /**
     * Checks if is below threshold flapping stopped.
     *
     * @return true, if is below threshold flapping stopped
     */
    public boolean isBelowThresholdFlappingStopped() {
      return belowThresholdFlappingStopped;
    }

    /**
     * Checks if is below threshold not flapping.
     *
     * @return true, if is below threshold not flapping
     */
    public boolean isBelowThresholdNotFlapping() {
      return belowThresholdNotFlapping;
    }

    /**
     * Checks if is flapping started.
     *
     * @return true, if is flapping started
     */
    public boolean isFlappingStarted() {
      return flappingStarted;
    }

  }

}
