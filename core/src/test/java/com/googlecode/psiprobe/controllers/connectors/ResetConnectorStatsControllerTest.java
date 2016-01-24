package com.googlecode.psiprobe.controllers.connectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ResetConnectorStatsControllerTest.
 */
public class ResetConnectorStatsControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ResetConnectorStatsController.class).skip("applicationContext", "supportedMethods").test();
  }

}
