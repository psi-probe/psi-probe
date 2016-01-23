package com.googlecode.psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class RuntimeStatsCollectorBeanTest.
 */
public class RuntimeStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(RuntimeStatsCollectorBean.class).loadData().test();
  }

}
