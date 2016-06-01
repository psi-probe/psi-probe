package psiprobe.controllers.connectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.connectors.ListConnectorsController;

/**
 * The Class ListConnectorsControllerTest.
 */
public class ListConnectorsControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ListConnectorsController.class).skip("applicationContext", "supportedMethods").test();
  }

}
