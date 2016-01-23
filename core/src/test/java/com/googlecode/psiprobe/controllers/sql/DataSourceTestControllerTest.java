package com.googlecode.psiprobe.controllers.sql;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class DataSourceTestControllerTest.
 */
public class DataSourceTestControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DataSourceTestController.class).skip("applicationContext", "supportedMethods").test();
  }

}
