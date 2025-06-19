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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import psiprobe.model.jmx.Cluster;

class ClusterWrapperBeanTest {

  @Test
  void testGetClusterReturnsClusterWhenJmxPresent() throws Exception {
    // Arrange
    String serverName = "Catalina";
    String hostName = "localhost";
    boolean loadMembers = false;

    MBeanServer mbeanServer = mock(MBeanServer.class);
    ObjectName clusterObjName = new ObjectName(serverName + ":type=Cluster,host=" + hostName);
    ObjectInstance clusterInstance = mock(ObjectInstance.class);
    when(clusterInstance.getObjectName()).thenReturn(clusterObjName);

    Set<ObjectInstance> clusters = Collections.singleton(clusterInstance);
    Set<ObjectInstance> membership = Collections.singleton(clusterInstance);

    when(mbeanServer.queryMBeans(new ObjectName("*:type=Cluster,host=" + hostName), null))
        .thenReturn(clusters);
    when(mbeanServer
        .queryMBeans(new ObjectName(serverName + ":type=ClusterMembership,host=" + hostName), null))
        .thenReturn(membership);

    // Mock JmxTools static methods
    try (MockedStatic<ManagementFactory> mgmtFactoryMock =
        Mockito.mockStatic(ManagementFactory.class)) {
      mgmtFactoryMock.when(ManagementFactory::getPlatformMBeanServer).thenReturn(mbeanServer);

      // Mock JmxTools methods as needed (example for getStringAttr)
      try (MockedStatic<psiprobe.tools.JmxTools> jmxToolsMock =
          Mockito.mockStatic(psiprobe.tools.JmxTools.class)) {
        jmxToolsMock.when(() -> psiprobe.tools.JmxTools.getStringAttr(any(), any(), any()))
            .thenReturn("test");
        jmxToolsMock.when(() -> psiprobe.tools.JmxTools.getLongAttr(any(), any(), any()))
            .thenReturn(1L);
        jmxToolsMock.when(() -> psiprobe.tools.JmxTools.getIntAttr(any(), any(), any()))
            .thenReturn(1);
        jmxToolsMock.when(() -> psiprobe.tools.JmxTools.getBooleanAttr(any(), any(), any()))
            .thenReturn(true);
        jmxToolsMock.when(() -> psiprobe.tools.JmxTools.getAttribute(any(), any(), any()))
            .thenReturn(new ObjectName[0]);

        ClusterWrapperBean bean = new ClusterWrapperBean();

        // Act
        Cluster cluster = bean.getCluster(serverName, hostName, loadMembers);

        // Assert
        assertNotNull(cluster);
        assertEquals("test", cluster.getName());
      }
    }
  }

  @Test
  void testGetClusterReturnsNullWhenNoCluster() throws Exception {
    String serverName = "Catalina";
    String hostName = "localhost";
    boolean loadMembers = false;

    MBeanServer mbeanServer = mock(MBeanServer.class);

    when(mbeanServer.queryMBeans(new ObjectName("*:type=Cluster,host=" + hostName), null))
        .thenReturn(Collections.emptySet());
    when(mbeanServer
        .queryMBeans(new ObjectName(serverName + ":type=ClusterMembership,host=" + hostName), null))
        .thenReturn(Collections.emptySet());

    try (MockedStatic<ManagementFactory> mgmtFactoryMock =
        Mockito.mockStatic(ManagementFactory.class)) {
      mgmtFactoryMock.when(ManagementFactory::getPlatformMBeanServer).thenReturn(mbeanServer);

      ClusterWrapperBean bean = new ClusterWrapperBean();

      Cluster cluster = bean.getCluster(serverName, hostName, loadMembers);

      assertNull(cluster);
    }
  }
}
