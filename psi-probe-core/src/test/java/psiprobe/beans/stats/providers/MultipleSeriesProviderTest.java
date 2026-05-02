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
package psiprobe.beans.stats.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.codebox.bean.JavaBeanTester;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import psiprobe.model.stats.StatsCollection;

/**
 * The Class MultipleSeriesProviderTest.
 */
class MultipleSeriesProviderTest {

  private MultipleSeriesProvider provider;
  private StatsCollection statsCollection;
  private DefaultTableXYDataset dataset;

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(MultipleSeriesProvider.class).loadData().test();
  }

  @BeforeEach
  void setUp() {
    provider = new MultipleSeriesProvider();
    provider.setStatNamePrefix("stat.app.");
    provider.setTop(0);
    provider.setMovingAvgFrame(0);
    statsCollection = new StatsCollection();
    dataset = new DefaultTableXYDataset();
  }

  @Test
  void testPopulateAllSeries() {
    List<XYDataItem> items1 = statsCollection.newStats("stat.app.app1", 10);
    items1.add(new XYDataItem(1L, 100.0));
    items1.add(new XYDataItem(2L, 200.0));

    List<XYDataItem> items2 = statsCollection.newStats("stat.app.app2", 10);
    items2.add(new XYDataItem(1L, 300.0));

    HttpServletRequest request = mock(HttpServletRequest.class);
    provider.populate(dataset, statsCollection, request);

    assertEquals(2, dataset.getSeriesCount());
  }

  @Test
  void testPopulateTopNSeries() {
    // Create 3 series, request top 2
    List<XYDataItem> items1 = statsCollection.newStats("stat.app.app1", 10);
    items1.add(new XYDataItem(1L, 50.0));

    List<XYDataItem> items2 = statsCollection.newStats("stat.app.app2", 10);
    items2.add(new XYDataItem(1L, 200.0));

    List<XYDataItem> items3 = statsCollection.newStats("stat.app.app3", 10);
    items3.add(new XYDataItem(1L, 100.0));

    provider.setTop(2);
    HttpServletRequest request = mock(HttpServletRequest.class);
    provider.populate(dataset, statsCollection, request);

    assertEquals(2, dataset.getSeriesCount());
  }

  @Test
  void testPopulateNoMatchingStats() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    provider.populate(dataset, statsCollection, request);
    assertEquals(0, dataset.getSeriesCount());
  }

  @Test
  void testPopulateWithMovingAvgFrame() {
    provider.setMovingAvgFrame(2);
    provider.setTop(2);

    List<XYDataItem> items1 = statsCollection.newStats("stat.app.app1", 10);
    for (int i = 0; i < 10; i++) {
      items1.add(new XYDataItem((long) i, i * 10.0));
    }

    List<XYDataItem> items2 = statsCollection.newStats("stat.app.app2", 10);
    for (int i = 0; i < 10; i++) {
      items2.add(new XYDataItem((long) i, i * 5.0));
    }

    List<XYDataItem> items3 = statsCollection.newStats("stat.app.app3", 10);
    for (int i = 0; i < 10; i++) {
      items3.add(new XYDataItem((long) i, i * 1.0));
    }

    HttpServletRequest request = mock(HttpServletRequest.class);
    provider.populate(dataset, statsCollection, request);

    assertEquals(2, dataset.getSeriesCount());
  }

  @Test
  void testTopEqualToSize() {
    // top == size -> useTop is false, should return all
    List<XYDataItem> items1 = statsCollection.newStats("stat.app.app1", 10);
    items1.add(new XYDataItem(1L, 100.0));
    List<XYDataItem> items2 = statsCollection.newStats("stat.app.app2", 10);
    items2.add(new XYDataItem(1L, 200.0));

    provider.setTop(2); // top == size, useTop is false
    HttpServletRequest request = mock(HttpServletRequest.class);
    provider.populate(dataset, statsCollection, request);

    assertEquals(2, dataset.getSeriesCount());
  }
}
