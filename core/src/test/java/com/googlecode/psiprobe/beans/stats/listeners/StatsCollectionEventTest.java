package com.googlecode.psiprobe.beans.stats.listeners;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class StatsCollectionEventTest.
 */
public class StatsCollectionEventTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(StatsCollectionEvent.class).loadData().test();
  }

}
