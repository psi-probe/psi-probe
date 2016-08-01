package psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.RenderChartController;

/**
 * The Class RenderChartControllerTest.
 */
public class RenderChartControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(RenderChartController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
