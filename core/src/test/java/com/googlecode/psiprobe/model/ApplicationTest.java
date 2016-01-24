package com.googlecode.psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ApplicationTest.
 */
public class ApplicationTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Application.class).loadData().test();
  }

}
