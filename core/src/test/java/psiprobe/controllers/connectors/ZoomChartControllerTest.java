package psiprobe.controllers.connectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.connectors.ZoomChartController;

/**
 * The Class ZoomChartControllerTest.
 */
public class ZoomChartControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ZoomChartController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
