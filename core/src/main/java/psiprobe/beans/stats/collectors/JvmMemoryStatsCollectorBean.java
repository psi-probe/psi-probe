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

package psiprobe.beans.stats.collectors;

import psiprobe.beans.JvmMemoryInfoAccessorBean;
import psiprobe.model.jmx.MemoryPool;

import java.util.List;

/**
 * The Class JvmMemoryStatsCollectorBean.
 *
 * @author Vlad Ilyushchenko
 */
public class JvmMemoryStatsCollectorBean extends AbstractStatsCollectorBean {

  /** The jvm memory info accessor. */
  private JvmMemoryInfoAccessorBean jvmMemoryInfoAccessor;

  /**
   * Gets the jvm memory info accessor.
   *
   * @return the jvm memory info accessor
   */
  public JvmMemoryInfoAccessorBean getJvmMemoryInfoAccessor() {
    return jvmMemoryInfoAccessor;
  }

  /**
   * Sets the jvm memory info accessor.
   *
   * @param jvmMemoryInfoAccessor the new jvm memory info accessor
   */
  public void setJvmMemoryInfoAccessor(JvmMemoryInfoAccessorBean jvmMemoryInfoAccessor) {
    this.jvmMemoryInfoAccessor = jvmMemoryInfoAccessor;
  }

  @Override
  public void collect() throws Exception {
    List<MemoryPool> pools = jvmMemoryInfoAccessor.getPools();
    long time = System.currentTimeMillis();
    for (MemoryPool pool : pools) {
      buildAbsoluteStats("memory.pool." + pool.getName(), pool.getUsed(), time);
    }
  }
}
