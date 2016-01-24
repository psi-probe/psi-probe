package com.googlecode.psiprobe.controllers.apps;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ResetAppStatsControllerTest.
 */
public class ResetAppStatsControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ResetAppStatsController.class).skip("applicationContext", "supportedMethods").test();
  }

}
