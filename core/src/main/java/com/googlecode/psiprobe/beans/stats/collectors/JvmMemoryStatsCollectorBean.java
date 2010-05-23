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

import com.googlecode.psiprobe.beans.JvmMemoryInfoAccessorBean;
import com.googlecode.psiprobe.model.jmx.MemoryPool;
import java.util.Iterator;
import java.util.List;

public class JvmMemoryStatsCollectorBean extends AbstractStatsCollectorBean {
    private JvmMemoryInfoAccessorBean jvmMemoryInfoAccessor;

    public JvmMemoryInfoAccessorBean getJvmMemoryInfoAccessor() {
        return jvmMemoryInfoAccessor;
    }

    public void setJvmMemoryInfoAccessor(JvmMemoryInfoAccessorBean jvmMemoryInfoAccessor) {
        this.jvmMemoryInfoAccessor = jvmMemoryInfoAccessor;
    }

    public void collect() throws Exception {
        List pools = jvmMemoryInfoAccessor.getPools();
        long time = System.currentTimeMillis();
        for (Iterator it = pools.iterator(); it.hasNext(); ) {
            MemoryPool pool = (MemoryPool) it.next();
            buildAbsoluteStats("memory.pool."+pool.getName(), pool.getUsed(), time);
        }
    }
}
