package com.googlecode.psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class JvmMemoryStatsCollectorBeanTest.
 */
public class JvmMemoryStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(JvmMemoryStatsCollectorBean.class).loadData().test();
  }

}
