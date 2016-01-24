package com.googlecode.psiprobe.beans.stats.providers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class StandardSeriesProviderTest.
 */
public class StandardSeriesProviderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(StandardSeriesProvider.class).loadData().test();
  }

}
