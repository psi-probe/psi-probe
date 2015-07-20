/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.beans;

import com.googlecode.psiprobe.model.jmx.RuntimeInformation;
import com.googlecode.psiprobe.tools.JmxTools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.modeler.Registry;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class RuntimeInfoAccessorBean {

  private Log logger = LogFactory.getLog(RuntimeInfoAccessorBean.class);

  public RuntimeInformation getRuntimeInformation() throws Exception {
    MBeanServer mbeanServer = new Registry().getMBeanServer();
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
        ri.setTotalPhysicalMemorySize(JmxTools.getLongAttr(mbeanServer, osOName,
            "TotalPhysicalMemorySize"));
        ri.setCommittedVirtualMemorySize(JmxTools.getLongAttr(mbeanServer, osOName,
            "CommittedVirtualMemorySize"));
        ri.setFreePhysicalMemorySize(JmxTools.getLongAttr(mbeanServer, osOName,
            "FreePhysicalMemorySize"));
        ri.setFreeSwapSpaceSize(JmxTools.getLongAttr(mbeanServer, osOName, "FreeSwapSpaceSize"));
        ri.setTotalSwapSpaceSize(JmxTools.getLongAttr(mbeanServer, osOName, "TotalSwapSpaceSize"));
        ri.setProcessCpuTime(JmxTools.getLongAttr(mbeanServer, osOName, "ProcessCpuTime"));
        ri.setAvailableProcessors(Runtime.getRuntime().availableProcessors());
      } else {
        ri.setTotalPhysicalMemorySize(JmxTools.getLongAttr(mbeanServer, osOName,
            "TotalPhysicalMemory"));
      }

      if (JmxTools.hasAttribute(mbeanServer, osOName, "OpenFileDescriptorCount")
          && JmxTools.hasAttribute(mbeanServer, osOName, "MaxFileDescriptorCount")) {

        ri.setOpenFileDescriptorCount(JmxTools.getLongAttr(mbeanServer, osOName,
            "OpenFileDescriptorCount"));
        ri.setMaxFileDescriptorCount(JmxTools.getLongAttr(mbeanServer, osOName,
            "MaxFileDescriptorCount"));
      }

      return ri;
    } catch (Exception e) {
      logger.debug("OS information is unavailable");
      return null;
    }
  }
}
