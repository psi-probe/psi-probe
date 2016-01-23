package com.googlecode.psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ClusterStatsCollectorBeanTest.
 */
public class ClusterStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ClusterStatsCollectorBean.class).loadData().test();
  }

}
