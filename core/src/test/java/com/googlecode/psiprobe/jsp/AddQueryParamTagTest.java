package com.googlecode.psiprobe.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class AddQueryParamTagTest.
 */
public class AddQueryParamTagTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(AddQueryParamTag.class).loadData().test();
  }

}
