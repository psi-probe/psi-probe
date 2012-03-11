/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.beans.stats.collectors;

import com.googlecode.psiprobe.TomcatContainer;
import com.googlecode.psiprobe.beans.ClusterWrapperBean;
import com.googlecode.psiprobe.beans.ContainerWrapperBean;
import com.googlecode.psiprobe.model.jmx.Cluster;

public class ClusterStatsCollectorBean extends AbstractStatsCollectorBean {
    private ContainerWrapperBean containerWrapper;
    private ClusterWrapperBean clusterWrapper;

    public ContainerWrapperBean getContainerWrapper() {
        return containerWrapper;
    }

    public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
        this.containerWrapper = containerWrapper;
    }

    public ClusterWrapperBean getClusterWrapper() {
        return clusterWrapper;
    }

    public void setClusterWrapper(ClusterWrapperBean clusterWrapper) {
        this.clusterWrapper = clusterWrapper;
    }

    public void collect() throws Exception {
        // Job can be called before the servlet finished intialisation. Make sure
        // we dont get an NPE.
        TomcatContainer container = containerWrapper.getTomcatContainer();
        if (container != null) {
            Cluster cluster = clusterWrapper.getCluster(container.getName(), container.getHostName(), false);
            if (cluster != null) {
                buildDeltaStats("cluster.received", cluster.getTotalReceivedBytes());
                buildDeltaStats("cluster.sent", cluster.getSenderTotalBytes());
                buildDeltaStats("cluster.req.received", cluster.getNrOfMsgsReceived());
                buildDeltaStats("cluster.req.sent", cluster.getSenderNrOfRequests());
            }
        }
    }
}
