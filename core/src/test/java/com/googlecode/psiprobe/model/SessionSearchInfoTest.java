package com.googlecode.psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class SessionSearchInfoTest.
 */
public class SessionSearchInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(SessionSearchInfo.class).loadData().test();
  }

}
