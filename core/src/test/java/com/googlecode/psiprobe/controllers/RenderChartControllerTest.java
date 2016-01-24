package com.googlecode.psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class RenderChartControllerTest.
 */
public class RenderChartControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(RenderChartController.class).skip("applicationContext", "supportedMethods").test();
  }

}
