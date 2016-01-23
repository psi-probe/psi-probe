package com.googlecode.psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class SyncClusterSenderTest.
 */
public class SyncClusterSenderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(SyncClusterSender.class).loadData().test();
  }

}
