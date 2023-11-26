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
      ObjectName runtimeOName = new ObjectName("java.lang:type=Runtime");
      ri.setStartTime(JmxTools.getLongAttr(mbeanServer, runtimeOName, "StartTime"));
      ri.setUptime(JmxTools.getLongAttr(mbeanServer, runtimeOName, "Uptime"));
      ri.setVmVendor(JmxTools.getStringAttr(mbeanServer, runtimeOName, "VmVendor"));

      ObjectName osOName = new ObjectName("java.lang:type=OperatingSystem");
      ri.setOsName(JmxTools.getStringAttr(mbeanServer, osOName, "Name"));
      ri.setOsVersion(JmxTools.getStringAttr(mbeanServer, osOName, "Version"));

      if (!ri.getVmVendor().startsWith("IBM Corporation")) {
        ri.setTotalPhysicalMemorySize(
            JmxTools.getLongAttr(mbeanServer, osOName, "TotalPhysicalMemorySize"));
        ri.setCommittedVirtualMemorySize(
            JmxTools.getLongAttr(mbeanServer, osOName, "CommittedVirtualMemorySize"));
        ri.setFreePhysicalMemorySize(
            JmxTools.getLongAttr(mbeanServer, osOName, "FreePhysicalMemorySize"));
        ri.setFreeSwapSpaceSize(JmxTools.getLongAttr(mbeanServer, osOName, "FreeSwapSpaceSize"));
        ri.setTotalSwapSpaceSize(JmxTools.getLongAttr(mbeanServer, osOName, "TotalSwapSpaceSize"));
        ri.setProcessCpuTime(JmxTools.getLongAttr(mbeanServer, osOName, "ProcessCpuTime"));
        ri.setAvailableProcessors(Runtime.getRuntime().availableProcessors());
      } else {
        ri.setTotalPhysicalMemorySize(
            JmxTools.getLongAttr(mbeanServer, osOName, "TotalPhysicalMemory"));
      }

      if (JmxTools.hasAttribute(mbeanServer, osOName, "OpenFileDescriptorCount")
          && JmxTools.hasAttribute(mbeanServer, osOName, "MaxFileDescriptorCount")) {

        ri.setOpenFileDescriptorCount(
            JmxTools.getLongAttr(mbeanServer, osOName, "OpenFileDescriptorCount"));
        ri.setMaxFileDescriptorCount(
            JmxTools.getLongAttr(mbeanServer, osOName, "MaxFileDescriptorCount"));
      }

      return ri;
    } catch (MalformedObjectNameException e) {
      logger.debug("OS information is unavailable");
      logger.trace("", e);
      return null;
    }
  }
}
