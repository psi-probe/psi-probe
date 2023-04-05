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

import jakarta.servlet.http.HttpServletRequest;

import org.jfree.data.xy.DefaultTableXYDataset;

import psiprobe.model.stats.StatsCollection;

/**
 * Classes implementing this interface can be wired up with RenderChartController to provide Series
 * data based on StatsCollection instance.
 */
public interface SeriesProvider {

  /**
   * Populate.
   *
   * @param dataset the dataset
   * @param statsCollection the stats collection
   * @param request the request
   */
  void populate(DefaultTableXYDataset dataset, StatsCollection statsCollection,
      HttpServletRequest request);
}
