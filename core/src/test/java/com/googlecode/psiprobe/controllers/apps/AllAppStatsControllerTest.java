package com.googlecode.psiprobe.controllers.apps;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class AllAppStatsControllerTest.
 */
public class AllAppStatsControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(AllAppStatsController.class).skip("applicationContext", "supportedMethods").test();
  }

}
