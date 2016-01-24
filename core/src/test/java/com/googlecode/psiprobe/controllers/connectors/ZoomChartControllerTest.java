package com.googlecode.psiprobe.controllers.connectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ZoomChartControllerTest.
 */
public class ZoomChartControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ZoomChartController.class).skip("applicationContext", "supportedMethods").test();
  }

}
