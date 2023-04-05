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
package psiprobe.beans.stats.collectors;

import jakarta.inject.Inject;

import org.springframework.beans.factory.annotation.Value;

import psiprobe.TomcatContainer;
import psiprobe.beans.ClusterWrapperBean;
import psiprobe.beans.ContainerWrapperBean;
import psiprobe.model.jmx.Cluster;
import psiprobe.tools.TimeExpression;

/**
 * The Class ClusterStatsCollectorBean.
 */
public class ClusterStatsCollectorBean extends AbstractStatsCollectorBean {

  /** The container wrapper. */
  @Inject
  private ContainerWrapperBean containerWrapper;

  /** The cluster wrapper. */
  @Inject
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
    // Job can be called before the servlet finished initialization. Make sure
    // we don't get an NPE.
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

  /**
   * Sets the max series expression.
   *
   * @param period the period
   * @param span the span
   */
  public void setMaxSeries(@Value("${psiprobe.beans.stats.collectors.cluster.period}") long period,
      @Value("${psiprobe.beans.stats.collectors.cluster.span}") long span) {
    super.setMaxSeries((int) TimeExpression.dataPoints(period, span));
  }

}
