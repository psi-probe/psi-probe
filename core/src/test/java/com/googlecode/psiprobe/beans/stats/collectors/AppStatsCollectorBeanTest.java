package com.googlecode.psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class AppStatsCollectorBeanTest.
 */
public class AppStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(AppStatsCollectorBean.class).loadData().test();
  }

}
