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
package psiprobe.controllers.cluster;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import psiprobe.TomcatContainer;
import psiprobe.beans.ClusterWrapperBean;
import psiprobe.controllers.AbstractTomcatContainerController;
import psiprobe.model.jmx.Cluster;

/**
 * The Class BaseClusterStatsController.
 */
public class BaseClusterStatsController extends AbstractTomcatContainerController {

  /** The cluster wrapper. */
  @Inject
  private ClusterWrapperBean clusterWrapper;

  /** The load members. */
  private boolean loadMembers = true;

  /** The collection period. */
  private long collectionPeriod;

  /**
   * Gets the cluster wrapper.
   *
   * @return the cluster wrapper
   */
  public ClusterWrapperBean getClusterWrapper() {
    return clusterWrapper;
  }

  /**
   * Sets the cluster wrapper.
   *
   * @param clusterWrapper the new cluster wrapper
   */
  public void setClusterWrapper(ClusterWrapperBean clusterWrapper) {
    this.clusterWrapper = clusterWrapper;
  }

  /**
   * Checks if is load members.
   *
   * @return true, if is load members
   */
  public boolean isLoadMembers() {
    return loadMembers;
  }

  /**
   * Sets the load members.
   *
   * @param loadMembers the new load members
   */
  public void setLoadMembers(boolean loadMembers) {
    this.loadMembers = loadMembers;
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
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    TomcatContainer container = getContainerWrapper().getTomcatContainer();
    Cluster cluster = getClusterWrapper().getCluster(container.getName(), container.getHostName(),
        isLoadMembers());
    return new ModelAndView(getViewName()).addObject("cluster", cluster)
        .addObject("collectionPeriod", getCollectionPeriod());
  }

}
