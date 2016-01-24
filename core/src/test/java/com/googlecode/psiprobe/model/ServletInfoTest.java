package com.googlecode.psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ServletInfoTest.
 */
public class ServletInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ServletInfo.class).loadData().test();
  }

}
