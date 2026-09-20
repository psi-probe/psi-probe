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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import psiprobe.model.jmx.RuntimeInformation;
import psiprobe.tools.JmxTools;

/**
 * Tests for {@link RuntimeInfoAccessorBean}.
 */
class RuntimeInfoAccessorBeanTest {

  @Test
  void testGetRuntimeInformation_NonIbmVendor() throws Exception {
    MBeanServer mbeanServer = mock(MBeanServer.class);
    ObjectName runtimeObj = new ObjectName("java.lang:type=Runtime");
    ObjectName osObj = new ObjectName("java.lang:type=OperatingSystem");

    try (MockedStatic<ManagementFactory> mgmtFactoryMock =
        Mockito.mockStatic(ManagementFactory.class)) {
      mgmtFactoryMock.when(ManagementFactory::getPlatformMBeanServer).thenReturn(mbeanServer);

      try (MockedStatic<JmxTools> jmxToolsMock = Mockito.mockStatic(JmxTools.class)) {
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, runtimeObj, "StartTime"))
            .thenReturn(123L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, runtimeObj, "Uptime"))
            .thenReturn(456L);
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, runtimeObj, "VmVendor"))
            .thenReturn("Oracle Corporation");
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, osObj, "Name"))
            .thenReturn("Linux");
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, osObj, "Version"))
            .thenReturn("5.10");
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, osObj, "TotalPhysicalMemorySize"))
            .thenReturn(1000L);
        jmxToolsMock
            .when(() -> JmxTools.getLongAttr(mbeanServer, osObj, "CommittedVirtualMemorySize"))
            .thenReturn(2000L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, osObj, "FreePhysicalMemorySize"))
            .thenReturn(3000L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, osObj, "FreeSwapSpaceSize"))
            .thenReturn(4000L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, osObj, "TotalSwapSpaceSize"))
            .thenReturn(5000L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, osObj, "ProcessCpuTime"))
            .thenReturn(6000L);
        jmxToolsMock
            .when(() -> JmxTools.hasAttribute(mbeanServer, osObj, "OpenFileDescriptorCount"))
            .thenReturn(true);
        jmxToolsMock.when(() -> JmxTools.hasAttribute(mbeanServer, osObj, "MaxFileDescriptorCount"))
            .thenReturn(true);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, osObj, "OpenFileDescriptorCount"))
            .thenReturn(7L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, osObj, "MaxFileDescriptorCount"))
            .thenReturn(8L);

        try (MockedStatic<Runtime> runtimeMock = Mockito.mockStatic(Runtime.class)) {
          Runtime runtime = mock(Runtime.class);
          runtimeMock.when(Runtime::getRuntime).thenReturn(runtime);

          RuntimeInfoAccessorBean bean = new RuntimeInfoAccessorBean();
          RuntimeInformation info = bean.getRuntimeInformation();

          assertNotNull(info);
          assertEquals(123L, info.getStartTime());
          assertEquals(456L, info.getUptime());
          assertEquals("Oracle Corporation", info.getVmVendor());
          assertEquals("Linux", info.getOsName());
          assertEquals("5.10", info.getOsVersion());
          assertEquals(1000L, info.getTotalPhysicalMemorySize());
          assertEquals(2000L, info.getCommittedVirtualMemorySize());
          assertEquals(3000L, info.getFreePhysicalMemorySize());
          assertEquals(4000L, info.getFreeSwapSpaceSize());
          assertEquals(5000L, info.getTotalSwapSpaceSize());
          assertEquals(6000L, info.getProcessCpuTime());
          assertEquals(Runtime.getRuntime().availableProcessors(), info.getAvailableProcessors());
          assertEquals(7L, info.getOpenFileDescriptorCount());
          assertEquals(8L, info.getMaxFileDescriptorCount());
        }
      }
    }
  }

  @Test
  void testGetRuntimeInformation_IbmVendor() throws Exception {
    MBeanServer mbeanServer = mock(MBeanServer.class);
    ObjectName runtimeObj = new ObjectName("java.lang:type=Runtime");
    ObjectName osObj = new ObjectName("java.lang:type=OperatingSystem");

    try (MockedStatic<ManagementFactory> mgmtFactoryMock =
        Mockito.mockStatic(ManagementFactory.class)) {
      mgmtFactoryMock.when(ManagementFactory::getPlatformMBeanServer).thenReturn(mbeanServer);

      try (MockedStatic<JmxTools> jmxToolsMock = Mockito.mockStatic(JmxTools.class)) {
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, runtimeObj, "StartTime"))
            .thenReturn(123L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, runtimeObj, "Uptime"))
            .thenReturn(456L);
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, runtimeObj, "VmVendor"))
            .thenReturn("IBM Corporation");
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, osObj, "Name"))
            .thenReturn("AIX");
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, osObj, "Version"))
            .thenReturn("7.2");
        jmxToolsMock.when(() -> JmxTools.getLongAttr(mbeanServer, osObj, "TotalPhysicalMemory"))
            .thenReturn(9999L);
        jmxToolsMock
            .when(() -> JmxTools.hasAttribute(mbeanServer, osObj, "OpenFileDescriptorCount"))
            .thenReturn(false);
        jmxToolsMock.when(() -> JmxTools.hasAttribute(mbeanServer, osObj, "MaxFileDescriptorCount"))
            .thenReturn(false);

        RuntimeInfoAccessorBean bean = new RuntimeInfoAccessorBean();
        RuntimeInformation info = bean.getRuntimeInformation();

        assertNotNull(info);
        assertEquals(123L, info.getStartTime());
        assertEquals(456L, info.getUptime());
        assertEquals("IBM Corporation", info.getVmVendor());
        assertEquals("AIX", info.getOsName());
        assertEquals("7.2", info.getOsVersion());
        assertEquals(9999L, info.getTotalPhysicalMemorySize());
        assertEquals(0L, info.getOpenFileDescriptorCount());
        assertEquals(0L, info.getMaxFileDescriptorCount());
      }
    }
  }
}
