package com.googlecode.psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ClusterTest.
 */
public class ClusterTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Cluster.class).loadData().test();
  }

}
