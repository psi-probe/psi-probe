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
package psiprobe.controllers.apps;

import jakarta.inject.Inject;

import org.springframework.stereotype.Controller;

import psiprobe.beans.stats.collectors.AppStatsCollectorBean;

/**
 * The Class ResetAppStatsController.
 */
// TODO 12/11/2016 JWL - This controller was not even setup...review it's need
@Controller
public class ResetAppStatsController extends AbstractNoSelfContextHandlerController {

  /** The stats collector. */
  @Inject
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

  @Override
  protected void executeAction(String contextName) throws Exception {
    statsCollector.reset(contextName);
  }

}
