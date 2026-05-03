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
import static org.mockito.Mockito.when;

import com.codebox.bean.JavaBeanTester;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import psiprobe.model.stats.StatsCollection;

/**
 * The Class StandardSeriesProviderTest.
 */
class StandardSeriesProviderTest {

  private StandardSeriesProvider provider;
  private StatsCollection statsCollection;
  private DefaultTableXYDataset dataset;

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(StandardSeriesProvider.class).loadData().test();
  }

  @BeforeEach
  void setUp() {
    provider = new StandardSeriesProvider();
    statsCollection = new StatsCollection();
    dataset = new DefaultTableXYDataset();
  }

  @Test
  void testPopulateWithStaticStatName() {
    List<XYDataItem> items = statsCollection.newStats("stat.memory.heap", 10);
    items.add(new XYDataItem(1L, 512.0));
    items.add(new XYDataItem(2L, 1024.0));

    provider.setStatNames(Arrays.asList("stat.memory.heap"));

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("sp")).thenReturn(null);
    when(request.getParameter("s1l")).thenReturn("Heap");

    provider.populate(dataset, statsCollection, request);

    assertEquals(1, dataset.getSeriesCount());
    assertEquals(2, dataset.getItemCount(0));
  }

  @Test
  void testPopulateWithSeriesParam() {
    List<XYDataItem> items = statsCollection.newStats("stat.connector.http-8080.requests", 10);
    items.add(new XYDataItem(1L, 42.0));

    provider.setStatNames(Arrays.asList("stat.connector.{0}.requests"));

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("sp")).thenReturn("http-8080");
    when(request.getParameter("s1l")).thenReturn("Requests");

    provider.populate(dataset, statsCollection, request);

    assertEquals(1, dataset.getSeriesCount());
  }

  @Test
  void testPopulateMultipleSeries() {
    List<XYDataItem> items1 = statsCollection.newStats("stat.mem.heap", 10);
    items1.add(new XYDataItem(1L, 100.0));

    List<XYDataItem> items2 = statsCollection.newStats("stat.mem.nonheap", 10);
    items2.add(new XYDataItem(1L, 50.0));

    provider.setStatNames(Arrays.asList("stat.mem.heap", "stat.mem.nonheap"));

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("sp")).thenReturn(null);
    when(request.getParameter("s1l")).thenReturn("Heap");
    when(request.getParameter("s2l")).thenReturn("NonHeap");

    provider.populate(dataset, statsCollection, request);

    assertEquals(2, dataset.getSeriesCount());
  }

  @Test
  void testPopulateMissingStats() {
    provider.setStatNames(Arrays.asList("stat.nonexistent"));

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("sp")).thenReturn(null);

    provider.populate(dataset, statsCollection, request);

    assertEquals(0, dataset.getSeriesCount());
  }
}
