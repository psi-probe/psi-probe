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

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import psiprobe.model.jmx.RuntimeInformation;
import psiprobe.tools.JmxTools;

/**
 * The Class RuntimeInfoAccessorBean.
 */
public class RuntimeInfoAccessorBean {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(RuntimeInfoAccessorBean.class);

  /**
   * Gets the runtime information.
   *
   * @return the runtime information
   */
  public RuntimeInformation getRuntimeInformation() {
    MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
    RuntimeInformation ri = new RuntimeInformation();

    try {
      ObjectName objectNameRuntime = new ObjectName("java.lang:type=Runtime");
      ri.setStartTime(JmxTools.getLongAttr(mbeanServer, objectNameRuntime, "StartTime"));
      ri.setUptime(JmxTools.getLongAttr(mbeanServer, objectNameRuntime, "Uptime"));
      ri.setVmVendor(JmxTools.getStringAttr(mbeanServer, objectNameRuntime, "VmVendor"));

      ObjectName objectNameOperationSystem = new ObjectName("java.lang:type=OperatingSystem");
      ri.setOsName(JmxTools.getStringAttr(mbeanServer, objectNameOperationSystem, "Name"));
      ri.setOsVersion(JmxTools.getStringAttr(mbeanServer, objectNameOperationSystem, "Version"));

      if (!ri.getVmVendor().startsWith("IBM Corporation")) {
        ri.setTotalPhysicalMemorySize(JmxTools.getLongAttr(mbeanServer, objectNameOperationSystem,
            "TotalPhysicalMemorySize"));
        ri.setCommittedVirtualMemorySize(JmxTools.getLongAttr(mbeanServer,
            objectNameOperationSystem, "CommittedVirtualMemorySize"));
        ri.setFreePhysicalMemorySize(
            JmxTools.getLongAttr(mbeanServer, objectNameOperationSystem, "FreePhysicalMemorySize"));
        ri.setFreeSwapSpaceSize(
            JmxTools.getLongAttr(mbeanServer, objectNameOperationSystem, "FreeSwapSpaceSize"));
        ri.setTotalSwapSpaceSize(
            JmxTools.getLongAttr(mbeanServer, objectNameOperationSystem, "TotalSwapSpaceSize"));
        ri.setProcessCpuTime(
            JmxTools.getLongAttr(mbeanServer, objectNameOperationSystem, "ProcessCpuTime"));
        ri.setAvailableProcessors(Runtime.getRuntime().availableProcessors());
      } else {
        ri.setTotalPhysicalMemorySize(
            JmxTools.getLongAttr(mbeanServer, objectNameOperationSystem, "TotalPhysicalMemory"));
      }

      if (JmxTools.hasAttribute(mbeanServer, objectNameOperationSystem, "OpenFileDescriptorCount")
          && JmxTools.hasAttribute(mbeanServer, objectNameOperationSystem,
              "MaxFileDescriptorCount")) {

        ri.setOpenFileDescriptorCount(JmxTools.getLongAttr(mbeanServer, objectNameOperationSystem,
            "OpenFileDescriptorCount"));
        ri.setMaxFileDescriptorCount(
            JmxTools.getLongAttr(mbeanServer, objectNameOperationSystem, "MaxFileDescriptorCount"));
      }

      return ri;
    } catch (MalformedObjectNameException e) {
      logger.debug("OS information is unavailable");
      logger.trace("", e);
      return null;
    }
  }
}
