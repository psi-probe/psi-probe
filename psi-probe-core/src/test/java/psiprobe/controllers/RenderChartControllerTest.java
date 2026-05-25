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
package psiprobe.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import psiprobe.beans.stats.providers.SeriesProvider;
import psiprobe.model.stats.StatsCollection;

/**
 * The Class RenderChartControllerTest.
 */
class RenderChartControllerTest {

  public static class TestSeriesProvider implements SeriesProvider {

    @Override
    public void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection,
        jakarta.servlet.http.HttpServletRequest request) {
      XYSeries series = new XYSeries("series-1", true, false);
      series.add(1L, 2L);
      dataset.addSeries(series);
    }
  }

  public static class NotASeriesProvider {
    // no-op
  }

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(RenderChartController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  @Test
  void handleRequestRendersLineChartFromSeriesProvider() throws Exception {
    RenderChartController controller = new RenderChartController();
    controller.setViewName("chart");
    controller.setStatsCollection(new StatsCollection());

    StaticApplicationContext applicationContext = new StaticApplicationContext();
    applicationContext.registerSingleton("seriesProvider", TestSeriesProvider.class);
    controller.setApplicationContext(applicationContext);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter("ct", "line");
    request.addParameter("p", "seriesProvider");
    request.addParameter("xz", "320");
    request.addParameter("yz", "200");

    MockHttpServletResponse response = new MockHttpServletResponse();

    assertNull(controller.handleRequest(request, response));
    assertEquals("image/png", response.getHeader("Content-type"));
    assertTrue(response.getContentAsByteArray().length > 0);
  }

  @Test
  void handleRequestRendersChartWhenProviderBeanDoesNotImplementSeriesProvider() throws Exception {
    RenderChartController controller = new RenderChartController();
    controller.setViewName("chart");
    controller.setStatsCollection(new StatsCollection());

    StaticApplicationContext applicationContext = new StaticApplicationContext();
    applicationContext.registerSingleton("badProvider", NotASeriesProvider.class);
    controller.setApplicationContext(applicationContext);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter("ct", "area");
    request.addParameter("p", "badProvider");

    MockHttpServletResponse response = new MockHttpServletResponse();

    assertNull(controller.handleRequest(request, response));
    assertEquals("image/png", response.getHeader("Content-type"));
    assertTrue(response.getContentAsByteArray().length > 0);
  }

}
