package psiprobe.controllers.logs;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.logs.LogHandlerController;

/**
 * The Class LogHandlerControllerTest.
 */
public class LogHandlerControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(LogHandlerController.class).skip("applicationContext", "supportedMethods").test();
  }

}
