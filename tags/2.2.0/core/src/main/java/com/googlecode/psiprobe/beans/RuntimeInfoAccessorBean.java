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

import com.googlecode.psiprobe.model.jmx.RuntimeInformation;
import com.googlecode.psiprobe.tools.JmxTools;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.modeler.Registry;

public class RuntimeInfoAccessorBean {

    private Log logger = LogFactory.getLog(RuntimeInfoAccessorBean.class);

    public RuntimeInformation getRuntimeInformation() throws Exception {
        MBeanServer mBeanServer = new Registry().getMBeanServer();
        RuntimeInformation ri = new RuntimeInformation();

        try {
            ObjectName runtimeOName = new ObjectName("java.lang:type=Runtime");
            ri.setStartTime(JmxTools.getLongAttr(mBeanServer, runtimeOName, "StartTime"));
            ri.setUptime(JmxTools.getLongAttr(mBeanServer, runtimeOName, "Uptime"));
            ri.setVmVendor(JmxTools.getStringAttr(mBeanServer,runtimeOName,"VmVendor"));

            ObjectName osOName = new ObjectName("java.lang:type=OperatingSystem");
            ri.setOsName(JmxTools.getStringAttr(mBeanServer, osOName, "Name"));
            ri.setOsVersion(JmxTools.getStringAttr(mBeanServer, osOName, "Version"));

            if(!ri.getVmVendor().startsWith("IBM Corporation")){
                ri.setTotalPhysicalMemorySize(JmxTools.getLongAttr(mBeanServer, osOName, "TotalPhysicalMemorySize"));
                ri.setCommittedVirtualMemorySize(JmxTools.getLongAttr(mBeanServer, osOName, "CommittedVirtualMemorySize"));
                ri.setFreePhysicalMemorySize(JmxTools.getLongAttr(mBeanServer, osOName, "FreePhysicalMemorySize"));
                ri.setFreeSwapSpaceSize(JmxTools.getLongAttr(mBeanServer, osOName, "FreeSwapSpaceSize"));
                ri.setTotalSwapSpaceSize(JmxTools.getLongAttr(mBeanServer, osOName, "TotalSwapSpaceSize"));
                ri.setProcessCpuTime(JmxTools.getLongAttr(mBeanServer, osOName, "ProcessCpuTime"));
            } else {
                ri.setTotalPhysicalMemorySize(JmxTools.getLongAttr(mBeanServer, osOName, "TotalPhysicalMemory"));
            }

            return ri;
        } catch (Exception e) {
            logger.debug("OS information is unavailable");
            return null;
        }
    }
}
