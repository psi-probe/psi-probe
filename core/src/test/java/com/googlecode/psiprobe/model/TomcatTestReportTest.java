package com.googlecode.psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class TomcatTestReportTest.
 */
public class TomcatTestReportTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(TomcatTestReport.class).loadData().test();
  }

}
