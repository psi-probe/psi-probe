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

import java.util.List;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractSeriesProvider.
 */
public abstract class AbstractSeriesProvider implements SeriesProvider {

  /** The logger. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /** The lock. */
  private final Object lockObj = new Object();
  
  /**
   * To series.
   *
   * @param legend the legend
   * @param stats the stats
   *
   * @return the XY series
   */
  protected XYSeries toSeries(String legend, List<XYDataItem> stats) {
    XYSeries xySeries = new XYSeries(legend, true, false);
    synchronized (lockObj) {
      for (XYDataItem item : stats) {
        xySeries.addOrUpdate(item.getX(), item.getY());
      }
    }
    return xySeries;
  }

}
