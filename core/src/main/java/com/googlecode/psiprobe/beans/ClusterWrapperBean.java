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
package com.googlecode.psiprobe.beans;

import com.googlecode.psiprobe.model.jmx.AsyncClusterSender;
import com.googlecode.psiprobe.model.jmx.Cluster;
import com.googlecode.psiprobe.model.jmx.ClusterSender;
import com.googlecode.psiprobe.model.jmx.PooledClusterSender;
import com.googlecode.psiprobe.model.jmx.SyncClusterSender;
import com.googlecode.psiprobe.tools.JmxTools;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import org.apache.commons.modeler.Registry;

public class ClusterWrapperBean {

    public Cluster getCluster(String serverName, String hostName, boolean loadMembers) throws Exception {
        Cluster cluster = null;

        MBeanServer mBeanServer = new Registry().getMBeanServer();
        ObjectName membershipOName = new ObjectName(serverName +":type=ClusterMembership,host=" + hostName);
        ObjectName receiverOName = new ObjectName(serverName +":type=ClusterReceiver,host=" + hostName);
        ObjectName senderOName = new ObjectName(serverName +":type=ClusterSender,host=" + hostName);

        //
        // should be just one set, this is just to find out if this instance
        // is cluster-enabled and the cluster supports JMX
        //
        Set clusters = mBeanServer.queryMBeans(new ObjectName("*:type=Cluster,host=" + hostName), null);
        Set membership = mBeanServer.queryMBeans(membershipOName, null);
        if (clusters != null && clusters.size() > 0 && membership != null && membership.size() > 0) {
            ObjectName clusterOName = ((ObjectInstance) clusters.iterator().next()).getObjectName();
            cluster = new Cluster();

            cluster.setName(JmxTools.getStringAttr(mBeanServer, clusterOName, "clusterName"));
            cluster.setInfo(JmxTools.getStringAttr(mBeanServer, clusterOName, "info"));
            cluster.setManagerClassName(JmxTools.getStringAttr(mBeanServer, clusterOName, "managerClassName"));

            cluster.setMcastAddress(JmxTools.getStringAttr(mBeanServer, membershipOName, "mcastAddr"));
            cluster.setMcastBindAddress(JmxTools.getStringAttr(mBeanServer, membershipOName, "mcastBindAddress"));
            cluster.setMcastClusterDomain(JmxTools.getStringAttr(mBeanServer, membershipOName, "mcastClusterDomain"));
            cluster.setMcastDropTime(JmxTools.getLongAttr(mBeanServer, membershipOName, "mcastDropTime"));
            cluster.setMcastFrequency(JmxTools.getLongAttr(mBeanServer, membershipOName, "mcastFrequency"));
            cluster.setMcastPort(JmxTools.getIntAttr(mBeanServer, membershipOName, "mcastPort"));
            cluster.setMcastSoTimeout(JmxTools.getIntAttr(mBeanServer, membershipOName, "mcastSoTimeout"));
            cluster.setMcastTTL(JmxTools.getIntAttr(mBeanServer, membershipOName, "mcastTTL"));

            cluster.setTcpListenAddress(JmxTools.getStringAttr(mBeanServer, receiverOName, "tcpListenAddress"));
            cluster.setTcpListenPort(JmxTools.getIntAttr(mBeanServer, receiverOName, "tcpListenPort"));
            cluster.setNrOfMsgsReceived(JmxTools.getLongAttr(mBeanServer, receiverOName, "nrOfMsgsReceived"));
            cluster.setTotalReceivedBytes(JmxTools.getLongAttr(mBeanServer, receiverOName, "totalReceivedBytes"));
//            cluster.setTcpSelectorTimeout(getLongAttr(mBeanServer, receiverOName, "tcpSelectorTimeout"));
//            cluster.setTcpThreadCount(getIntAttr(mBeanServer, receiverOName, "tcpThreadCount"));

            cluster.setSenderAckTimeout(JmxTools.getLongAttr(mBeanServer, senderOName, "ackTimeout"));
            cluster.setSenderAutoConnect(((Boolean) mBeanServer.getAttribute(senderOName, "autoConnect")).booleanValue());
            cluster.setSenderFailureCounter(JmxTools.getLongAttr(mBeanServer, senderOName, "failureCounter"));
            cluster.setSenderNrOfRequests(JmxTools.getLongAttr(mBeanServer, senderOName, "nrOfRequests"));
            cluster.setSenderReplicationMode(JmxTools.getStringAttr(mBeanServer, senderOName, "replicationMode"));
            cluster.setSenderTotalBytes(JmxTools.getLongAttr(mBeanServer, senderOName, "totalBytes"));

            if (loadMembers) {
                ObjectName senders[] = (ObjectName[]) mBeanServer.getAttribute(senderOName, "senderObjectNames");
                for (int i = 0; i < senders.length; i++) {

                    ClusterSender sender;

                    if ("pooled".equals(cluster.getSenderReplicationMode())) {
                        sender = new PooledClusterSender();
                    } else if ("synchronous".equals(cluster.getSenderReplicationMode())) {
                        sender = new SyncClusterSender();
                    } else if ("asynchronous".equals(cluster.getSenderReplicationMode()) ||
                            "fastasyncqueue".equals(cluster.getSenderReplicationMode())) {
                        sender = new AsyncClusterSender();
                    } else {
                        sender = new ClusterSender();
                    }
                    ObjectName localSenderOName = senders[i];

                    sender.setAddress(JmxTools.getStringAttr(mBeanServer, localSenderOName, "address"));
                    sender.setPort(JmxTools.getIntAttr(mBeanServer, localSenderOName, "port"));

                    sender.setAvgMessageSize(JmxTools.getLongAttr(mBeanServer, localSenderOName, "avgMessageSize", -1));
                    sender.setAvgProcessingTime(JmxTools.getLongAttr(mBeanServer, localSenderOName, "avgProcessingTime", -1));

                    sender.setConnectCounter(JmxTools.getLongAttr(mBeanServer, localSenderOName, "connectCounter"));
                    sender.setDisconnectCounter(JmxTools.getLongAttr(mBeanServer, localSenderOName, "disconnectCounter"));
                    sender.setConnected(((Boolean) mBeanServer.getAttribute(localSenderOName, "connected")).booleanValue());
                    sender.setKeepAliveTimeout(JmxTools.getLongAttr(mBeanServer, localSenderOName, "keepAliveTimeout"));
                    sender.setNrOfRequests(JmxTools.getLongAttr(mBeanServer, localSenderOName, "nrOfRequests"));
                    sender.setTotalBytes(JmxTools.getLongAttr(mBeanServer, localSenderOName, "totalBytes"));
                    sender.setResend(((Boolean) mBeanServer.getAttribute(localSenderOName, "resend")).booleanValue());
                    sender.setSuspect(((Boolean) mBeanServer.getAttribute(localSenderOName, "suspect")).booleanValue());

                    if (sender instanceof PooledClusterSender) {
                        ((PooledClusterSender) sender).setMaxPoolSocketLimit(JmxTools.getIntAttr(mBeanServer, localSenderOName, "maxPoolSocketLimit"));
                    }

                    if (sender instanceof SyncClusterSender) {
                        SyncClusterSender syncSender = (SyncClusterSender) sender;
                        syncSender.setDataFailureCounter(JmxTools.getLongAttr(mBeanServer, localSenderOName, "dataFailureCounter"));
                        syncSender.setDataResendCounter(JmxTools.getLongAttr(mBeanServer, localSenderOName, "dataResendCounter"));
                        syncSender.setSocketOpenCounter(JmxTools.getIntAttr(mBeanServer, localSenderOName, "socketOpenCounter"));
                        syncSender.setSocketCloseCounter(JmxTools.getIntAttr(mBeanServer, localSenderOName, "socketCloseCounter"));
                        syncSender.setSocketOpenFailureCounter(JmxTools.getIntAttr(mBeanServer, localSenderOName, "socketOpenFailureCounter"));
                    }

                    if (sender instanceof AsyncClusterSender) {
                        AsyncClusterSender asyncSender = (AsyncClusterSender) sender;
                        asyncSender.setInQueueCounter(JmxTools.getLongAttr(mBeanServer, localSenderOName, "inQueueCounter"));
                        asyncSender.setOutQueueCounter(JmxTools.getLongAttr(mBeanServer, localSenderOName, "outQueueCounter"));
                        asyncSender.setQueueSize(JmxTools.getIntAttr(mBeanServer, localSenderOName, "queueSize"));
                        asyncSender.setQueuedNrOfBytes(JmxTools.getLongAttr(mBeanServer, localSenderOName, "queuedNrOfBytes"));
                    }
                    cluster.getMembers().add(sender);
                }
            }
        }
        return cluster;
    }

}
