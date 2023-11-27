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
package psiprobe.beans;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import psiprobe.model.jmx.AsyncClusterSender;
import psiprobe.model.jmx.Cluster;
import psiprobe.model.jmx.ClusterSender;
import psiprobe.model.jmx.PooledClusterSender;
import psiprobe.model.jmx.SyncClusterSender;
import psiprobe.tools.JmxTools;

/**
 * The Class ClusterWrapperBean.
 */
public class ClusterWrapperBean {

  /**
   * Gets the cluster.
   *
   * @param serverName the server name
   * @param hostName the host name
   * @param loadMembers the load members
   *
   * @return the cluster
   *
   * @throws MalformedObjectNameException the malformed object name exception
   */
  public Cluster getCluster(String serverName, String hostName, boolean loadMembers)
      throws MalformedObjectNameException {

    Cluster cluster = null;

    MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
    ObjectName membershipOName =
        new ObjectName(serverName + ":type=ClusterMembership,host=" + hostName);
    ObjectName receiverOName =
        new ObjectName(serverName + ":type=ClusterReceiver,host=" + hostName);
    ObjectName senderOName = new ObjectName(serverName + ":type=ClusterSender,host=" + hostName);

    /*
     * should be just one set, this is just to find out if this instance is cluster-enabled and the
     * cluster supports JMX
     */
    Set<ObjectInstance> clusters =
        mbeanServer.queryMBeans(new ObjectName("*:type=Cluster,host=" + hostName), null);
    Set<ObjectInstance> membership = mbeanServer.queryMBeans(membershipOName, null);
    if (clusters != null && !clusters.isEmpty() && membership != null && !membership.isEmpty()) {
      ObjectName clusterOName = clusters.iterator().next().getObjectName();
      cluster = new Cluster();

      cluster.setName(JmxTools.getStringAttr(mbeanServer, clusterOName, "clusterName"));
      cluster.setInfo(JmxTools.getStringAttr(mbeanServer, clusterOName, "info"));
      cluster.setManagerClassName(
          JmxTools.getStringAttr(mbeanServer, clusterOName, "managerClassName"));

      cluster.setMcastAddress(JmxTools.getStringAttr(mbeanServer, membershipOName, "mcastAddr"));
      cluster.setMcastBindAddress(
          JmxTools.getStringAttr(mbeanServer, membershipOName, "mcastBindAddress"));
      cluster.setMcastClusterDomain(
          JmxTools.getStringAttr(mbeanServer, membershipOName, "mcastClusterDomain"));
      cluster.setMcastDropTime(JmxTools.getLongAttr(mbeanServer, membershipOName, "mcastDropTime"));
      cluster
          .setMcastFrequency(JmxTools.getLongAttr(mbeanServer, membershipOName, "mcastFrequency"));
      cluster.setMcastPort(JmxTools.getIntAttr(mbeanServer, membershipOName, "mcastPort"));
      cluster
          .setMcastSoTimeout(JmxTools.getIntAttr(mbeanServer, membershipOName, "mcastSoTimeout"));
      cluster.setMcastTtl(JmxTools.getIntAttr(mbeanServer, membershipOName, "mcastTTL"));

      cluster.setTcpListenAddress(
          JmxTools.getStringAttr(mbeanServer, receiverOName, "tcpListenAddress"));
      cluster.setTcpListenPort(JmxTools.getIntAttr(mbeanServer, receiverOName, "tcpListenPort"));
      cluster.setNrOfMsgsReceived(
          JmxTools.getLongAttr(mbeanServer, receiverOName, "nrOfMsgsReceived"));
      cluster.setTotalReceivedBytes(
          JmxTools.getLongAttr(mbeanServer, receiverOName, "totalReceivedBytes"));
      // cluster.setTcpSelectorTimeout(
      // JmxTools.getLongAttr(mbeanServer, receiverOName, "tcpSelectorTimeout"));
      // cluster.setTcpThreadCount(
      // JmxTools.getIntAttr(mbeanServer, receiverOName, "tcpThreadCount"));

      cluster.setSenderAckTimeout(JmxTools.getLongAttr(mbeanServer, senderOName, "ackTimeout"));
      cluster
          .setSenderAutoConnect(JmxTools.getBooleanAttr(mbeanServer, senderOName, "autoConnect"));
      cluster.setSenderFailureCounter(
          JmxTools.getLongAttr(mbeanServer, senderOName, "failureCounter"));
      cluster.setSenderNrOfRequests(JmxTools.getLongAttr(mbeanServer, senderOName, "nrOfRequests"));
      cluster.setSenderReplicationMode(
          JmxTools.getStringAttr(mbeanServer, senderOName, "replicationMode"));
      cluster.setSenderTotalBytes(JmxTools.getLongAttr(mbeanServer, senderOName, "totalBytes"));

      if (loadMembers) {
        ObjectName[] senders =
            (ObjectName[]) JmxTools.getAttribute(mbeanServer, senderOName, "senderObjectNames");
        for (ObjectName localSenderOName : senders) {
          ClusterSender sender;

          if ("pooled".equals(cluster.getSenderReplicationMode())) {
            sender = new PooledClusterSender();
          } else if ("synchronous".equals(cluster.getSenderReplicationMode())) {
            sender = new SyncClusterSender();
          } else if ("asynchronous".equals(cluster.getSenderReplicationMode())
              || "fastasyncqueue".equals(cluster.getSenderReplicationMode())) {
            sender = new AsyncClusterSender();
          } else {
            sender = new ClusterSender();
          }

          sender.setAddress(JmxTools.getStringAttr(mbeanServer, localSenderOName, "address"));
          sender.setPort(JmxTools.getIntAttr(mbeanServer, localSenderOName, "port"));

          sender.setAvgMessageSize(
              JmxTools.getLongAttr(mbeanServer, localSenderOName, "avgMessageSize", -1));
          sender.setAvgProcessingTime(
              JmxTools.getLongAttr(mbeanServer, localSenderOName, "avgProcessingTime", -1));

          sender.setConnectCounter(
              JmxTools.getLongAttr(mbeanServer, localSenderOName, "connectCounter"));
          sender.setDisconnectCounter(
              JmxTools.getLongAttr(mbeanServer, localSenderOName, "disconnectCounter"));
          sender.setConnected(JmxTools.getBooleanAttr(mbeanServer, localSenderOName, "connected"));
          sender.setKeepAliveTimeout(
              JmxTools.getLongAttr(mbeanServer, localSenderOName, "keepAliveTimeout"));
          sender
              .setNrOfRequests(JmxTools.getLongAttr(mbeanServer, localSenderOName, "nrOfRequests"));
          sender.setTotalBytes(JmxTools.getLongAttr(mbeanServer, localSenderOName, "totalBytes"));
          sender.setResend(JmxTools.getBooleanAttr(mbeanServer, localSenderOName, "resend"));
          sender.setSuspect(JmxTools.getBooleanAttr(mbeanServer, localSenderOName, "suspect"));

          if (sender instanceof PooledClusterSender) {
            ((PooledClusterSender) sender).setMaxPoolSocketLimit(
                JmxTools.getIntAttr(mbeanServer, localSenderOName, "maxPoolSocketLimit"));
          }

          if (sender instanceof SyncClusterSender) {
            SyncClusterSender syncSender = (SyncClusterSender) sender;
            syncSender.setDataFailureCounter(
                JmxTools.getLongAttr(mbeanServer, localSenderOName, "dataFailureCounter"));
            syncSender.setDataResendCounter(
                JmxTools.getLongAttr(mbeanServer, localSenderOName, "dataResendCounter"));
            syncSender.setSocketOpenCounter(
                JmxTools.getIntAttr(mbeanServer, localSenderOName, "socketOpenCounter"));
            syncSender.setSocketCloseCounter(
                JmxTools.getIntAttr(mbeanServer, localSenderOName, "socketCloseCounter"));
            syncSender.setSocketOpenFailureCounter(
                JmxTools.getIntAttr(mbeanServer, localSenderOName, "socketOpenFailureCounter"));
          }

          if (sender instanceof AsyncClusterSender) {
            AsyncClusterSender asyncSender = (AsyncClusterSender) sender;
            asyncSender.setInQueueCounter(
                JmxTools.getLongAttr(mbeanServer, localSenderOName, "inQueueCounter"));
            asyncSender.setOutQueueCounter(
                JmxTools.getLongAttr(mbeanServer, localSenderOName, "outQueueCounter"));
            asyncSender
                .setQueueSize(JmxTools.getIntAttr(mbeanServer, localSenderOName, "queueSize"));
            asyncSender.setQueuedNrOfBytes(
                JmxTools.getLongAttr(mbeanServer, localSenderOName, "queuedNrOfBytes"));
          }
          cluster.getMembers().add(sender);
        }
      }
    }
    return cluster;
  }

}
