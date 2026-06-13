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
package psiprobe.beans.stats.collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.XYDataItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import psiprobe.beans.stats.listeners.StatsCollectionEvent;
import psiprobe.beans.stats.listeners.StatsCollectionListener;
import psiprobe.model.stats.StatsCollection;

/**
 * Tests for {@link AbstractStatsCollectorBean}.
 */
class AbstractStatsCollectorBeanTest {

  /** A minimal concrete implementation for testing. */
  static class TestCollector extends AbstractStatsCollectorBean {
    @Override
    public void collect() throws Exception {
      // no-op for testing
    }

    // Expose protected methods for testing
    long testBuildDeltaStats(String name, long value) throws InterruptedException {
      return buildDeltaStats(name, value);
    }

    long testBuildDeltaStats(String name, long value, long time) throws InterruptedException {
      return buildDeltaStats(name, value, time);
    }

    void testBuildAbsoluteStats(String name, long value) throws InterruptedException {
      buildAbsoluteStats(name, value);
    }

    void testBuildAbsoluteStats(String name, long value, long time) throws InterruptedException {
      buildAbsoluteStats(name, value, time);
    }

    void testBuildTimePercentageStats(String name, long value, long time)
        throws InterruptedException {
      buildTimePercentageStats(name, value, time);
    }
  }

  /** A test listener to capture events. */
  static class TestListener implements StatsCollectionListener {
    List<StatsCollectionEvent> events = new ArrayList<>();
    boolean enabled = true;

    @Override
    public boolean isEnabled() {
      return enabled;
    }

    @Override
    public void statsCollected(StatsCollectionEvent event) {
      events.add(event);
    }

    void reset() {
      events.clear();
    }
  }

  private TestCollector collector;
  private StatsCollection statsCollection;

  @BeforeEach
  void setUp() {
    collector = new TestCollector();
    statsCollection = new StatsCollection();
    collector.setStatsCollection(statsCollection);
    collector.setMaxSeries(10);
  }

  @Test
  void testSettersAndGetters() {
    assertEquals(statsCollection, collector.getStatsCollection());
    assertEquals(10, collector.getMaxSeries());
  }

  @Test
  void testBuildAbsoluteStatsFirstTime() throws InterruptedException {
    // First call creates new stats (empty list is returned, not null)
    collector.testBuildAbsoluteStats("test.stat", 100L);
    assertNotNull(statsCollection.getStats("test.stat")); // first call creates an empty list
  }

  @Test
  void testBuildAbsoluteStatsSubsequentCalls() throws InterruptedException {
    // First call creates the stats list
    collector.testBuildAbsoluteStats("abs.stat", 100L);
    // Second call adds data
    collector.testBuildAbsoluteStats("abs.stat", 200L, System.currentTimeMillis());

    List<XYDataItem> stats = statsCollection.getStats("abs.stat");
    assertNotNull(stats);
    assertEquals(1, stats.size());
    assertEquals(200L, stats.get(0).getY().longValue());
  }

  @Test
  void testBuildDeltaStats() throws InterruptedException {
    statsCollection.newStats("delta.stat", 10);
    // First call - delta is value - 0 = value
    long delta1 = collector.testBuildDeltaStats("delta.stat", 100L);
    assertEquals(100L, delta1);

    // Second call - delta is new - previous
    long delta2 = collector.testBuildDeltaStats("delta.stat", 150L);
    assertEquals(50L, delta2);
  }

  @Test
  void testBuildDeltaStatsNegativeDeltaClampsToZero() throws InterruptedException {
    statsCollection.newStats("neg.stat", 10);
    collector.testBuildDeltaStats("neg.stat", 100L);
    // decrease should give 0
    long delta = collector.testBuildDeltaStats("neg.stat", 50L);
    assertEquals(0L, delta);
  }

  @Test
  void testBuildAbsoluteStatsWithListener() throws InterruptedException {
    TestListener listener = new TestListener();
    List<StatsCollectionListener> listeners = new ArrayList<>();
    listeners.add(listener);
    collector.setListeners(listeners);

    // First call creates stats
    collector.testBuildAbsoluteStats("listen.stat", 100L);
    // Second call triggers listener
    collector.testBuildAbsoluteStats("listen.stat", 200L, System.currentTimeMillis());

    assertEquals(1, listener.events.size());
    assertEquals("listen.stat", listener.events.get(0).getName());
  }

  @Test
  void testBuildAbsoluteStatsWithDisabledListener() throws InterruptedException {
    TestListener listener = new TestListener();
    listener.enabled = false;
    List<StatsCollectionListener> listeners = new ArrayList<>();
    listeners.add(listener);
    collector.setListeners(listeners);

    collector.testBuildAbsoluteStats("dl.stat", 100L);
    collector.testBuildAbsoluteStats("dl.stat", 200L, System.currentTimeMillis());

    // disabled listener should not receive events
    assertEquals(0, listener.events.size());
  }

  @Test
  void testBuildTimePercentageStats() throws InterruptedException {
    statsCollection.newStats("time.stat", 10);
    long now = System.currentTimeMillis();

    // First call - stores baseline
    collector.testBuildTimePercentageStats("time.stat", 0L, now);
    // Second call - calculates percentage
    collector.testBuildTimePercentageStats("time.stat", 500L, now + 1000L);

    // Should have created stats and added data
    assertTrue(statsCollection.isCollected("time.stat"));
  }

  @Test
  void testMaxSeriesHousekeeping() throws InterruptedException {
    collector.setMaxSeries(3);
    statsCollection.newStats("hs.stat", 3);
    // Add more than maxSeries entries via second call (first call creates)
    long time = System.currentTimeMillis();
    for (int i = 0; i < 6; i++) {
      collector.testBuildAbsoluteStats("hs.stat", i * 100L, time + i * 1000L);
    }
    List<XYDataItem> stats = statsCollection.getStats("hs.stat");
    // housekeeping should trim to maxSeries
    assertTrue(stats.size() <= 3);
  }

  @Test
  void testNullStatsCollection() throws InterruptedException {
    collector.setStatsCollection(null);
    // Should not throw
    long delta = collector.testBuildDeltaStats("x", 100L);
    assertEquals(0L, delta);
  }
}
