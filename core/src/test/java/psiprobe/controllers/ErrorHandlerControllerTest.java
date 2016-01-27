package psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.ErrorHandlerController;

/**
 * The Class ErrorHandlerControllerTest.
 */
public class ErrorHandlerControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ErrorHandlerController.class).skip("applicationContext", "supportedMethods").test();
  }

}
