package com.googlecode.psiprobe.model.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

public class SummaryTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Summary.class).loadData().test();
  }

}
