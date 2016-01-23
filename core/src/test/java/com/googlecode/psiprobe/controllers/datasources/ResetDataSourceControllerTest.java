package com.googlecode.psiprobe.controllers.datasources;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ResetDataSourceControllerTest.
 */
public class ResetDataSourceControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ResetDataSourceController.class).skip("applicationContext", "supportedMethods").test();
  }

}
