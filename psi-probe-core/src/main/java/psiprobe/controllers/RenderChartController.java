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

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import psiprobe.Utils;
import psiprobe.beans.stats.providers.SeriesProvider;
import psiprobe.jfreechart.XYLine3DRenderer;
import psiprobe.model.stats.StatsCollection;

/**
 * Plots data from "statsCollection" bean. The data is converted to XYSeries using SeriesProvider,
 * name of which would be passed as a request parameter. The servlet can only plot up to 9 series.
 * It is customizable using these request parameters:
 * <ul>
 * <li>s1c, s2c, ... s9c - Series #i main color</li>
 * <li>s1o, s2o, ... s9o - Series #i outline color</li>
 * <li>bc - Chart background color</li>
 * <li>gc - Chart grid lines color</li>
 * <li>xl - X axis label</li>
 * <li>yl - Y axis label</li>
 * <li>xz - image width</li>
 * <li>yx - image height</li>
 * <li>l - show legend (boolean: true|false)</li>
 * <li>p - name of series provider bean</li>
 * </ul>
 */
@Controller
public class RenderChartController extends AbstractController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(RenderChartController.class);

  /** The stats collection. */
  @Inject
  private StatsCollection statsCollection;

  /**
   * Gets the stats collection.
   *
   * @return the stats collection
   */
  public StatsCollection getStatsCollection() {
    return statsCollection;
  }

  /**
   * Sets the stats collection.
   *
   * @param statsCollection the new stats collection
   */
  public void setStatsCollection(StatsCollection statsCollection) {
    this.statsCollection = statsCollection;
  }

  @RequestMapping(path = "/chart.png")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    // the max number of series
    final int seriesMaxCount = 9;

    // get Series Color from the request
    int[] seriesColor = new int[seriesMaxCount];
    seriesColor[0] = Utils.toIntHex(request.getParameter("s1c"), 0x9bd2fb);
    seriesColor[1] = Utils.toIntHex(request.getParameter("s2c"), 0xFF0606);
    for (int i = 2; i < seriesMaxCount; i++) {
      seriesColor[i] = Utils.toIntHex(request.getParameter("s" + (i + 1) + "c"), -1);
    }

    // get Series Outline Color from the request
    int[] seriesOutlineColor = new int[seriesMaxCount];
    seriesOutlineColor[0] = Utils.toIntHex(request.getParameter("s1o"), 0x0665aa);
    seriesOutlineColor[1] = Utils.toIntHex(request.getParameter("s2o"), 0x9d0000);
    for (int i = 2; i < seriesMaxCount; i++) {
      seriesOutlineColor[i] = Utils.toIntHex(request.getParameter("s" + (i + 1) + "o"), -1);
    }

    // background color
    int backgroundColor = Utils.toIntHex(request.getParameter("bc"), 0xFFFFFF);

    // grid color
    int gridColor = Utils.toIntHex(request.getParameter("gc"), 0);

    // X axis title
    String labelX = ServletRequestUtils.getStringParameter(request, "xl", "");

    // Y axis title
    String labelY = ServletRequestUtils.getStringParameter(request, "yl", "");

    // image width
    int width = ServletRequestUtils.getIntParameter(request, "xz", 800);

    // image height
    int height = ServletRequestUtils.getIntParameter(request, "yz", 400);

    // show legend?
    boolean showLegend = ServletRequestUtils.getBooleanParameter(request, "l", true);

    // Series provider
    String provider = ServletRequestUtils.getStringParameter(request, "p");

    // Chart type
    String chartType = ServletRequestUtils.getStringParameter(request, "ct", "area");

    DefaultTableXYDataset ds = new DefaultTableXYDataset();

    if (provider != null) {
      Object series = getApplicationContext().getBean(provider);
      if (series instanceof SeriesProvider) {
        ((SeriesProvider) series).populate(ds, statsCollection, request);
      } else {
        logger.error("SeriesProvider '{}' does not implement '{}'", provider, SeriesProvider.class);
      }
    }

    // Build series data from the give statistic
    JFreeChart chart = null;
    if ("area".equals(chartType)) {
      chart = ChartFactory.createXYAreaChart("", labelX, labelY, ds, PlotOrientation.VERTICAL,
          showLegend, false, false);

      ((XYAreaRenderer) chart.getXYPlot().getRenderer()).setOutline(true);

    } else if ("stacked".equals(chartType)) {
      chart = ChartFactory.createStackedXYAreaChart("", labelX, labelY, ds,
          PlotOrientation.VERTICAL, showLegend, false, false);

    } else if ("line".equals(chartType)) {
      chart = ChartFactory.createXYLineChart("", labelX, labelY, ds, PlotOrientation.VERTICAL,
          showLegend, false, false);

      final XYLine3DRenderer renderer = new XYLine3DRenderer();
      renderer.setDrawOutlines(true);
      for (int i = 0; i < seriesMaxCount; i++) {
        renderer.setSeriesLinesVisible(i, true);
        renderer.setSeriesShapesVisible(i, true);
        renderer.setSeriesStroke(i, new BasicStroke(2));
      }
      renderer.setXOffset(1);
      renderer.setYOffset(1);
      chart.getXYPlot().setRenderer(renderer);
    }

    if (chart != null) {
      chart.setAntiAlias(true);
      chart.setBackgroundPaint(new Color(backgroundColor));
      for (int i = 0; i < seriesMaxCount; i++) {
        if (seriesColor[i] >= 0) {
          chart.getXYPlot().getRenderer().setSeriesPaint(i, new Color(seriesColor[i]));
        }
        if (seriesOutlineColor[i] >= 0) {
          chart.getXYPlot().getRenderer().setSeriesOutlinePaint(i,
              new Color(seriesOutlineColor[i]));
        }
      }
      chart.getXYPlot().setDomainGridlinePaint(new Color(gridColor));
      chart.getXYPlot().setRangeGridlinePaint(new Color(gridColor));
      chart.getXYPlot().setDomainAxis(0, new DateAxis());
      chart.getXYPlot().setDomainAxis(1, new DateAxis());
      chart.getXYPlot().setInsets(new RectangleInsets(-15, 0, 0, 10));

      response.setHeader("Content-type", "image/png");
      response.getOutputStream()
          .write(ChartUtils.encodeAsPNG(chart.createBufferedImage(width, height)));
    }

    return null;
  }
}
