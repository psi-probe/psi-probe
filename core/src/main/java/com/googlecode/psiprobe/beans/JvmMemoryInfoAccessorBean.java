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

import com.googlecode.psiprobe.model.jmx.MemoryPool;
import com.googlecode.psiprobe.tools.JmxTools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.modeler.Registry;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;

/**
 * The Class JvmMemoryInfoAccessorBean.
 *
 * @author Vlad Ilyushchenko
 */
public class JvmMemoryInfoAccessorBean {

  /** The logger. */
  private final Log logger = LogFactory.getLog(this.getClass());

  /**
   * Gets the pools.
   *
   * @return the pools
   * @throws Exception the exception
   */
  public List<MemoryPool> getPools() throws Exception {

    List<MemoryPool> memoryPools = new LinkedList<MemoryPool>();
    MBeanServer mbeanServer = new Registry().getMBeanServer();
    Set<ObjectInstance> memoryOPools =
        mbeanServer.queryMBeans(new ObjectName("java.lang:type=MemoryPool,*"), null);

    // totals
    long totalInit = 0;
    long totalMax = 0;
    long totalUsed = 0;
    long totalCommitted = 0;

    for (ObjectInstance oi : memoryOPools) {
      ObjectName objName = oi.getObjectName();
      MemoryPool memoryPool = new MemoryPool();
      memoryPool.setName(JmxTools.getStringAttr(mbeanServer, objName, "Name"));
      memoryPool.setType(JmxTools.getStringAttr(mbeanServer, objName, "Type"));

      CompositeDataSupport cd = (CompositeDataSupport) mbeanServer.getAttribute(objName, "Usage");
      /*
       * It seems that "Usage" attribute of one of the pools may turn into null intermittently. We
       * better have a dip in the graph then an NPE though.
       */
      if (cd != null) {
        memoryPool.setMax(JmxTools.getLongAttr(cd, "max"));
        memoryPool.setUsed(JmxTools.getLongAttr(cd, "used"));
        memoryPool.setInit(JmxTools.getLongAttr(cd, "init"));
        memoryPool.setCommitted(JmxTools.getLongAttr(cd, "committed"));
      } else {
        logger.error("Oops, JVM problem? " + objName.toString() + " \"Usage\" attribute is NULL!");
      }

      totalInit += memoryPool.getInit();
      totalMax += memoryPool.getMax();
      totalUsed += memoryPool.getUsed();
      totalCommitted += memoryPool.getCommitted();

      memoryPools.add(memoryPool);
    }

    if (!memoryPools.isEmpty()) {
      MemoryPool pool = new MemoryPool();
      pool.setName("Total");
      pool.setType("TOTAL");
      pool.setInit(totalInit);
      pool.setUsed(totalUsed);
      pool.setMax(totalMax);
      pool.setCommitted(totalCommitted);
      memoryPools.add(pool);
    }

    return memoryPools;

  }
}
