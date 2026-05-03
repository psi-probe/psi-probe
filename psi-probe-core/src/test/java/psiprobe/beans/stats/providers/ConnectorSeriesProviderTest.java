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

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import psiprobe.model.stats.StatsCollection;

/**
 * Tests for {@link ConnectorSeriesProvider}.
 */
class ConnectorSeriesProviderTest {

  private ConnectorSeriesProvider provider;
  private StatsCollection statsCollection;
  private DefaultTableXYDataset dataset;

  @BeforeEach
  void setUp() {
    provider = new ConnectorSeriesProvider();
    statsCollection = new StatsCollection();
    dataset = new DefaultTableXYDataset();
  }

  @Test
  void testPopulateAddsSeriesWhenStatsExist() {
    List<XYDataItem> items = statsCollection.newStats("stat.connector.http-8080.requests", 10);
    items.add(new XYDataItem(1L, 100.0));
    items.add(new XYDataItem(2L, 200.0));

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("cn")).thenReturn("http-8080");
    when(request.getParameter("st")).thenReturn("requests");
    when(request.getParameter("sl")).thenReturn("Requests");

    provider.populate(dataset, statsCollection, request);

    assertEquals(1, dataset.getSeriesCount());
    assertEquals(2, dataset.getItemCount(0));
  }

  @Test
  void testPopulateDoesNothingWhenNoStats() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("cn")).thenReturn("http-8080");
    when(request.getParameter("st")).thenReturn("requests");
    when(request.getParameter("sl")).thenReturn("Requests");

    provider.populate(dataset, statsCollection, request);

    assertEquals(0, dataset.getSeriesCount());
  }

  @Test
  void testPopulateDoesNothingWhenConnectorNameNull() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("cn")).thenReturn(null);
    when(request.getParameter("st")).thenReturn("requests");

    provider.populate(dataset, statsCollection, request);

    assertEquals(0, dataset.getSeriesCount());
  }

  @Test
  void testPopulateDoesNothingWhenStatTypeNull() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("cn")).thenReturn("http-8080");
    when(request.getParameter("st")).thenReturn(null);

    provider.populate(dataset, statsCollection, request);

    assertEquals(0, dataset.getSeriesCount());
  }
}
