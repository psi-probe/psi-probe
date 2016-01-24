package com.googlecode.psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ClusterSenderTest.
 */
public class ClusterSenderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ClusterSender.class).loadData().test();
  }

}
