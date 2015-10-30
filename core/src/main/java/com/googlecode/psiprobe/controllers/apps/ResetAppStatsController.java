/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.controllers.apps;

import com.googlecode.psiprobe.beans.stats.collectors.AppStatsCollectorBean;

/**
 * The Class ResetAppStatsController.
 *
 * @author Mark Lewis
 */
public class ResetAppStatsController extends NoSelfContextHandlerController {

  /** The stats collector. */
  private AppStatsCollectorBean statsCollector;

  /**
   * Gets the stats collector.
   *
   * @return the stats collector
   */
  public AppStatsCollectorBean getStatsCollector() {
    return statsCollector;
  }

  /**
   * Sets the stats collector.
   *
   * @param statsCollector the new stats collector
   */
  public void setStatsCollector(AppStatsCollectorBean statsCollector) {
    this.statsCollector = statsCollector;
  }

  protected void executeAction(String contextName) throws Exception {
    statsCollector.reset(contextName);
  }

}
