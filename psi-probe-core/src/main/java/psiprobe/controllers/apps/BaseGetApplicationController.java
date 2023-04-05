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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.beans.ResourceResolver;
import psiprobe.controllers.AbstractContextHandlerController;
import psiprobe.model.Application;
import psiprobe.model.stats.StatsCollection;
import psiprobe.tools.ApplicationUtils;
import psiprobe.tools.SecurityUtils;

/**
 * Retrieves Application model object populated with application information.
 */
public class BaseGetApplicationController extends AbstractContextHandlerController {

  /** denotes whether extended application information and statistics should be collected. */
  private boolean extendedInfo;

  /** The stats collection. */
  @Inject
  private StatsCollection statsCollection;

  /** The collection period. */
  private long collectionPeriod;

  /**
   * Checks if is extended info.
   *
   * @return true, if is extended info
   */
  public boolean isExtendedInfo() {
    return extendedInfo;
  }

  /**
   * Sets the extended info.
   *
   * @param extendedInfo the new extended info
   */
  public void setExtendedInfo(boolean extendedInfo) {
    this.extendedInfo = extendedInfo;
  }

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

  /**
   * Gets the collection period.
   *
   * @return the collection period
   */
  public long getCollectionPeriod() {
    return collectionPeriod;
  }

  /**
   * Sets the collection period.
   *
   * @param collectionPeriod the new collection period
   */
  public void setCollectionPeriod(long collectionPeriod) {
    this.collectionPeriod = collectionPeriod;
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false)
        && SecurityUtils.hasAttributeValueRole(getServletContext());

    ResourceResolver resourceResolver = getContainerWrapper().getResourceResolver();
    Application app = ApplicationUtils.getApplication(context,
        isExtendedInfo() ? resourceResolver : null, calcSize, getContainerWrapper());

    if (isExtendedInfo() && getStatsCollection() != null) {
      String avgStatisticName = "app.avg_proc_time." + app.getName();
      app.setAvgTime(getStatsCollection().getLastValueForStat(avgStatisticName));
    }

    return new ModelAndView(getViewName()).addObject("app", app)
        .addObject("no_resources", !resourceResolver.supportsPrivateResources())
        .addObject("collectionPeriod", getCollectionPeriod());
  }

}
