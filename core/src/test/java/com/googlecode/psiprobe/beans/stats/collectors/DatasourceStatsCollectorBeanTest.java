package com.googlecode.psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class DatasourceStatsCollectorBeanTest.
 */
public class DatasourceStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DatasourceStatsCollectorBean.class).loadData().test();
  }

}
