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
    ObjectName objectNameMembership =
        new ObjectName(serverName + ":type=ClusterMembership,host=" + hostName);
    ObjectName objectNameReceiver =
        new ObjectName(serverName + ":type=ClusterReceiver,host=" + hostName);
    ObjectName objectNameSender =
        new ObjectName(serverName + ":type=ClusterSender,host=" + hostName);

    /*
     * should be just one set, this is just to find out if this instance is cluster-enabled and the
     * cluster supports JMX
     */
    Set<ObjectInstance> clusters =
        mbeanServer.queryMBeans(new ObjectName("*:type=Cluster,host=" + hostName), null);
    Set<ObjectInstance> membership = mbeanServer.queryMBeans(objectNameMembership, null);
    if (clusters != null && !clusters.isEmpty() && membership != null && !membership.isEmpty()) {
      ObjectName objectNameCluster = clusters.iterator().next().getObjectName();
      cluster = new Cluster();

      cluster.setName(JmxTools.getStringAttr(mbeanServer, objectNameCluster, "clusterName"));
      cluster.setInfo(JmxTools.getStringAttr(mbeanServer, objectNameCluster, "info"));
      cluster.setManagerClassName(
          JmxTools.getStringAttr(mbeanServer, objectNameCluster, "managerClassName"));

      cluster
          .setMcastAddress(JmxTools.getStringAttr(mbeanServer, objectNameMembership, "mcastAddr"));
      cluster.setMcastBindAddress(
          JmxTools.getStringAttr(mbeanServer, objectNameMembership, "mcastBindAddress"));
      cluster.setMcastClusterDomain(
          JmxTools.getStringAttr(mbeanServer, objectNameMembership, "mcastClusterDomain"));
      cluster.setMcastDropTime(
          JmxTools.getLongAttr(mbeanServer, objectNameMembership, "mcastDropTime"));
      cluster.setMcastFrequency(
          JmxTools.getLongAttr(mbeanServer, objectNameMembership, "mcastFrequency"));
      cluster.setMcastPort(JmxTools.getIntAttr(mbeanServer, objectNameMembership, "mcastPort"));
      cluster.setMcastSoTimeout(
          JmxTools.getIntAttr(mbeanServer, objectNameMembership, "mcastSoTimeout"));
      cluster.setMcastTtl(JmxTools.getIntAttr(mbeanServer, objectNameMembership, "mcastTTL"));

      cluster.setTcpListenAddress(
          JmxTools.getStringAttr(mbeanServer, objectNameReceiver, "tcpListenAddress"));
      cluster
          .setTcpListenPort(JmxTools.getIntAttr(mbeanServer, objectNameReceiver, "tcpListenPort"));
      cluster.setNrOfMsgsReceived(
          JmxTools.getLongAttr(mbeanServer, objectNameReceiver, "nrOfMsgsReceived"));
      cluster.setTotalReceivedBytes(
          JmxTools.getLongAttr(mbeanServer, objectNameReceiver, "totalReceivedBytes"));
      // cluster.setTcpSelectorTimeout(
      // JmxTools.getLongAttr(mbeanServer, objectNameReceiver, "tcpSelectorTimeout"));
      // cluster.setTcpThreadCount(
      // JmxTools.getIntAttr(mbeanServer, objectNameReceiver, "tcpThreadCount"));

      cluster
          .setSenderAckTimeout(JmxTools.getLongAttr(mbeanServer, objectNameSender, "ackTimeout"));
      cluster.setSenderAutoConnect(
          JmxTools.getBooleanAttr(mbeanServer, objectNameSender, "autoConnect"));
      cluster.setSenderFailureCounter(
          JmxTools.getLongAttr(mbeanServer, objectNameSender, "failureCounter"));
      cluster.setSenderNrOfRequests(
          JmxTools.getLongAttr(mbeanServer, objectNameSender, "nrOfRequests"));
      cluster.setSenderReplicationMode(
          JmxTools.getStringAttr(mbeanServer, objectNameSender, "replicationMode"));
      cluster
          .setSenderTotalBytes(JmxTools.getLongAttr(mbeanServer, objectNameSender, "totalBytes"));

      if (loadMembers) {
        ObjectName[] senders = (ObjectName[]) JmxTools.getAttribute(mbeanServer, objectNameSender,
            "senderObjectNames");
        for (ObjectName objectNameLocalSender : senders) {
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

          sender.setAddress(JmxTools.getStringAttr(mbeanServer, objectNameLocalSender, "address"));
          sender.setPort(JmxTools.getIntAttr(mbeanServer, objectNameLocalSender, "port"));

          sender.setAvgMessageSize(
              JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "avgMessageSize", -1));
          sender.setAvgProcessingTime(
              JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "avgProcessingTime", -1));

          sender.setConnectCounter(
              JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "connectCounter"));
          sender.setDisconnectCounter(
              JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "disconnectCounter"));
          sender.setConnected(
              JmxTools.getBooleanAttr(mbeanServer, objectNameLocalSender, "connected"));
          sender.setKeepAliveTimeout(
              JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "keepAliveTimeout"));
          sender.setNrOfRequests(
              JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "nrOfRequests"));
          sender.setTotalBytes(
              JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "totalBytes"));
          sender.setResend(JmxTools.getBooleanAttr(mbeanServer, objectNameLocalSender, "resend"));
          sender.setSuspect(JmxTools.getBooleanAttr(mbeanServer, objectNameLocalSender, "suspect"));

          if (sender instanceof PooledClusterSender) {
            ((PooledClusterSender) sender).setMaxPoolSocketLimit(
                JmxTools.getIntAttr(mbeanServer, objectNameLocalSender, "maxPoolSocketLimit"));
          }

          if (sender instanceof SyncClusterSender) {
            SyncClusterSender syncSender = (SyncClusterSender) sender;
            syncSender.setDataFailureCounter(
                JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "dataFailureCounter"));
            syncSender.setDataResendCounter(
                JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "dataResendCounter"));
            syncSender.setSocketOpenCounter(
                JmxTools.getIntAttr(mbeanServer, objectNameLocalSender, "socketOpenCounter"));
            syncSender.setSocketCloseCounter(
                JmxTools.getIntAttr(mbeanServer, objectNameLocalSender, "socketCloseCounter"));
            syncSender.setSocketOpenFailureCounter(JmxTools.getIntAttr(mbeanServer,
                objectNameLocalSender, "socketOpenFailureCounter"));
          }

          if (sender instanceof AsyncClusterSender) {
            AsyncClusterSender asyncSender = (AsyncClusterSender) sender;
            asyncSender.setInQueueCounter(
                JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "inQueueCounter"));
            asyncSender.setOutQueueCounter(
                JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "outQueueCounter"));
            asyncSender
                .setQueueSize(JmxTools.getIntAttr(mbeanServer, objectNameLocalSender, "queueSize"));
            asyncSender.setQueuedNrOfBytes(
                JmxTools.getLongAttr(mbeanServer, objectNameLocalSender, "queuedNrOfBytes"));
          }
          cluster.getMembers().add(sender);
        }
      }
    }
    return cluster;
  }

}
