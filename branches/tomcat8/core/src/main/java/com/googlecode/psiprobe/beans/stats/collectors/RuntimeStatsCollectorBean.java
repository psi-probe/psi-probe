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
package com.googlecode.psiprobe.beans.stats.collectors;

import com.googlecode.psiprobe.beans.RuntimeInfoAccessorBean;
import com.googlecode.psiprobe.model.jmx.RuntimeInformation;

/**
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class RuntimeStatsCollectorBean extends AbstractStatsCollectorBean {
    private RuntimeInfoAccessorBean runtimeInfoAccessorBean;

    public RuntimeInfoAccessorBean getRuntimeInfoAccessorBean() {
        return runtimeInfoAccessorBean;
    }

    public void setRuntimeInfoAccessorBean(RuntimeInfoAccessorBean runtimeInfoAccessorBean) {
        this.runtimeInfoAccessorBean = runtimeInfoAccessorBean;
    }

    public void collect() throws Exception {
        RuntimeInformation ri = runtimeInfoAccessorBean.getRuntimeInformation();
        if (ri != null) {
            long time = System.currentTimeMillis();
            buildAbsoluteStats("os.memory.committed", ri.getCommittedVirtualMemorySize()/1024, time);
            buildAbsoluteStats("os.memory.physical", (ri.getTotalPhysicalMemorySize() - ri.getFreePhysicalMemorySize())/1024, time);
            buildAbsoluteStats("os.memory.swap", (ri.getTotalSwapSpaceSize() - ri.getFreeSwapSpaceSize())/1024, time);

            buildAbsoluteStats("os.fd.open", ri.getOpenFDCount(), time);
            buildAbsoluteStats("os.fd.max", ri.getMaxFDCount(), time);
            //convert from nanoseconds so times use the same units
            long processCpuTimeMs = ri.getProcessCpuTime() / 1000000;
            //divide by the number of processors to reflect shared load (<= 100%)
            buildTimePercentageStats("os.cpu", processCpuTimeMs / ri.getAvailableProcessors(), time);
        }
    }
}
