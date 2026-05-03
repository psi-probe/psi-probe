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
package psiprobe.model.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.jfree.data.xy.XYDataItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StatsCollection}.
 */
class StatsCollectionTest {

  private StatsCollection stats;

  @BeforeEach
  void setUp() {
    stats = new StatsCollection();
    stats.setSwapFileName("test-stats.xml");
    stats.setMaxFiles(2);
  }

  @Test
  void testNewStats() {
    List<XYDataItem> list = stats.newStats("test.stat", 100);
    assertNotNull(list);
    assertTrue(list.isEmpty());
    assertTrue(stats.isCollected("test.stat"));
  }

  @Test
  void testIsCollectedFalseForMissing() {
    assertFalse(stats.isCollected("nonexistent"));
  }

  @Test
  void testGetStats() {
    stats.newStats("my.stat", 10);
    List<XYDataItem> result = stats.getStats("my.stat");
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  void testGetStatsNull() {
    assertNull(stats.getStats("missing"));
  }

  @Test
  void testResetStats() {
    List<XYDataItem> list = stats.newStats("reset.stat", 10);
    list.add(new XYDataItem(1L, 100.0));
    list.add(new XYDataItem(2L, 200.0));
    assertEquals(2, list.size());

    stats.resetStats("reset.stat");
    assertEquals(0, list.size());
  }

  @Test
  void testResetStatsNonExistent() {
    // Should not throw
    stats.resetStats("nonexistent.stat");
  }

  @Test
  void testGetLastValueForStat() {
    List<XYDataItem> list = stats.newStats("last.stat", 10);
    list.add(new XYDataItem(1L, 42.0));
    list.add(new XYDataItem(2L, 99.0));

    assertEquals(99L, stats.getLastValueForStat("last.stat"));
  }

  @Test
  void testGetLastValueForStatEmpty() {
    stats.newStats("empty.stat", 10);
    assertEquals(0L, stats.getLastValueForStat("empty.stat"));
  }

  @Test
  void testGetLastValueForStatNonExistent() {
    assertEquals(0L, stats.getLastValueForStat("nonexistent"));
  }

  @Test
  void testGetStatsByPrefix() {
    stats.newStats("connector.abc.requests", 10);
    stats.newStats("connector.abc.errors", 10);
    stats.newStats("memory.heap", 10);

    Map<String, List<XYDataItem>> result = stats.getStatsByPrefix("connector.");
    assertEquals(2, result.size());
    assertTrue(result.containsKey("connector.abc.requests"));
    assertTrue(result.containsKey("connector.abc.errors"));
    assertFalse(result.containsKey("memory.heap"));
  }

  @Test
  void testGetStatsByPrefixNoMatch() {
    stats.newStats("memory.heap", 10);
    Map<String, List<XYDataItem>> result = stats.getStatsByPrefix("connector.");
    assertTrue(result.isEmpty());
  }

  @Test
  void testSetMaxFiles() {
    stats.setMaxFiles(5);
    assertEquals(5, stats.getMaxFiles());
  }

  @Test
  void testSetMaxFilesZeroDefaultsToTwo() {
    stats.setMaxFiles(0);
    assertEquals(2, stats.getMaxFiles());
  }

  @Test
  void testSetMaxFilesNegativeDefaultsToTwo() {
    stats.setMaxFiles(-1);
    assertEquals(2, stats.getMaxFiles());
  }

  @Test
  void testGetSwapFileName() {
    assertEquals("test-stats.xml", stats.getSwapFileName());
  }

  @Test
  void testSetStoragePath() {
    stats.setStoragePath("/tmp/mystats");
    assertEquals("/tmp/mystats", stats.getStoragePath());
  }

  @Test
  void testLockForUpdateAndRelease() throws InterruptedException {
    stats.lockForUpdate();
    stats.releaseLock();
    // No exception means it works
  }

  @Test
  void testNewStatsOverwritesPrevious() {
    stats.newStats("dup.stat", 5);
    stats.newStats("dup.stat", 10);
    // Should be collected (second newStats)
    assertTrue(stats.isCollected("dup.stat"));
  }
}
