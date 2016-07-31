package psiprobe.controllers.connectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.connectors.GetConnectorController;

/**
 * The Class GetConnectorControllerTest.
 */
public class GetConnectorControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(GetConnectorController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
