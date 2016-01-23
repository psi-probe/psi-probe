package com.googlecode.psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class AsyncClusterSenderTest.
 */
public class AsyncClusterSenderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(AsyncClusterSender.class).loadData().test();
  }

}
