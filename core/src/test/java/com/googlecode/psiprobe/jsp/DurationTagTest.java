package com.googlecode.psiprobe.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class DurationTagTest.
 */
public class DurationTagTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DurationTag.class).loadData().test();
  }

}
