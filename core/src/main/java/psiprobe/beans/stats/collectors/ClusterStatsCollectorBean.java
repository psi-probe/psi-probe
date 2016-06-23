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

package psiprobe.beans.stats.collectors;

import psiprobe.TomcatContainer;
import psiprobe.beans.ClusterWrapperBean;
import psiprobe.beans.ContainerWrapperBean;
import psiprobe.model.jmx.Cluster;

/**
 * The Class ClusterStatsCollectorBean.
 *
 * @author Vlad Ilyushchenko
 */
public class ClusterStatsCollectorBean extends AbstractStatsCollectorBean {

  /** The container wrapper. */
  private ContainerWrapperBean containerWrapper;

  /** The cluster wrapper. */
  private ClusterWrapperBean clusterWrapper;

  /**
   * Gets the container wrapper.
   *
   * @return the container wrapper
   */
  public ContainerWrapperBean getContainerWrapper() {
    return containerWrapper;
  }

  /**
   * Sets the container wrapper.
   *
   * @param containerWrapper the new container wrapper
   */
  public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
    this.containerWrapper = containerWrapper;
  }

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

  @Override
  public void collect() throws Exception {
    // Job can be called before the servlet finished intialisation. Make sure
    // we dont get an NPE.
    TomcatContainer container = containerWrapper.getTomcatContainer();
    if (container != null) {
      Cluster cluster =
          clusterWrapper.getCluster(container.getName(), container.getHostName(), false);
      if (cluster != null) {
        buildDeltaStats("cluster.received", cluster.getTotalReceivedBytes());
        buildDeltaStats("cluster.sent", cluster.getSenderTotalBytes());
        buildDeltaStats("cluster.req.received", cluster.getNrOfMsgsReceived());
        buildDeltaStats("cluster.req.sent", cluster.getSenderNrOfRequests());
      }
    }
  }
}
