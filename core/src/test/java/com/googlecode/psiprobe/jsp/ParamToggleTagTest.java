package com.googlecode.psiprobe.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ParamToggleTagTest.
 */
public class ParamToggleTagTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ParamToggleTag.class).loadData().test();
  }

}
