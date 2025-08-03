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
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import psiprobe.model.jmx.MemoryPool;
import psiprobe.tools.JmxTools;

class JvmMemoryInfoAccessorBeanTest {

  @Test
  void testGetPoolsReturnsMemoryPools() throws Exception {
    // Arrange
    MBeanServer mbeanServer = mock(MBeanServer.class);
    ObjectName objName = new ObjectName("java.lang:type=MemoryPool,name=TestPool");
    ObjectInstance objectInstance = mock(ObjectInstance.class);
    when(objectInstance.getObjectName()).thenReturn(objName);

    Set<ObjectInstance> objectInstances = Collections.singleton(objectInstance);

    when(mbeanServer.queryMBeans(any(ObjectName.class), isNull())).thenReturn(objectInstances);

    // Mock CompositeDataSupport for Usage
    CompositeDataSupport usage = mock(CompositeDataSupport.class);

    try (MockedStatic<ManagementFactory> mgmtFactoryMock =
        Mockito.mockStatic(ManagementFactory.class)) {
      mgmtFactoryMock.when(ManagementFactory::getPlatformMBeanServer).thenReturn(mbeanServer);

      try (MockedStatic<JmxTools> jmxToolsMock = Mockito.mockStatic(JmxTools.class)) {
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, objName, "Name"))
            .thenReturn("TestPool");
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, objName, "Type"))
            .thenReturn("HEAP");
        jmxToolsMock.when(() -> JmxTools.getAttribute(mbeanServer, objName, "Usage"))
            .thenReturn(usage);

        jmxToolsMock.when(() -> JmxTools.getLongAttr(usage, "max")).thenReturn(100L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(usage, "used")).thenReturn(50L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(usage, "init")).thenReturn(10L);
        jmxToolsMock.when(() -> JmxTools.getLongAttr(usage, "committed")).thenReturn(80L);

        JvmMemoryInfoAccessorBean bean = new JvmMemoryInfoAccessorBean();

        // Act
        List<MemoryPool> pools = bean.getPools();

        // Assert
        assertNotNull(pools);
        assertEquals(2, pools.size()); // 1 pool + 1 total
        MemoryPool pool = pools.get(0);
        assertEquals("TestPool", pool.getName());
        assertEquals("HEAP", pool.getType());
        assertEquals(100L, pool.getMax());
        assertEquals(50L, pool.getUsed());
        assertEquals(10L, pool.getInit());
        assertEquals(80L, pool.getCommitted());

        MemoryPool total = pools.get(1);
        assertEquals("Total", total.getName());
        assertEquals("TOTAL", total.getType());
        assertEquals(100L, total.getMax());
        assertEquals(50L, total.getUsed());
        assertEquals(10L, total.getInit());
        assertEquals(80L, total.getCommitted());
      }
    }
  }

  @Test
  void testGetPoolsHandlesNullUsage() throws Exception {
    MBeanServer mbeanServer = mock(MBeanServer.class);
    ObjectName objName = new ObjectName("java.lang:type=MemoryPool,name=TestPool");
    ObjectInstance objectInstance = mock(ObjectInstance.class);
    when(objectInstance.getObjectName()).thenReturn(objName);

    Set<ObjectInstance> objectInstances = Collections.singleton(objectInstance);

    when(mbeanServer.queryMBeans(any(ObjectName.class), isNull())).thenReturn(objectInstances);

    try (MockedStatic<ManagementFactory> mgmtFactoryMock =
        Mockito.mockStatic(ManagementFactory.class)) {
      mgmtFactoryMock.when(ManagementFactory::getPlatformMBeanServer).thenReturn(mbeanServer);

      try (MockedStatic<JmxTools> jmxToolsMock = Mockito.mockStatic(JmxTools.class)) {
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, objName, "Name"))
            .thenReturn("TestPool");
        jmxToolsMock.when(() -> JmxTools.getStringAttr(mbeanServer, objName, "Type"))
            .thenReturn("HEAP");
        jmxToolsMock.when(() -> JmxTools.getAttribute(mbeanServer, objName, "Usage"))
            .thenReturn(null);

        JvmMemoryInfoAccessorBean bean = new JvmMemoryInfoAccessorBean();

        List<MemoryPool> pools = bean.getPools();

        assertNotNull(pools);
        assertEquals(2, pools.size()); // 1 pool + 1 total
        assertEquals(0L, pools.get(0).getMax());
        assertEquals(0L, pools.get(0).getUsed());
        assertEquals(0L, pools.get(0).getInit());
        assertEquals(0L, pools.get(0).getCommitted());
      }
    }
  }
}
