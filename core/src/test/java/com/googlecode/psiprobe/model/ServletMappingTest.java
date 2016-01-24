package com.googlecode.psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ServletMappingTest.
 */
public class ServletMappingTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ServletMapping.class).loadData().test();
  }

}
