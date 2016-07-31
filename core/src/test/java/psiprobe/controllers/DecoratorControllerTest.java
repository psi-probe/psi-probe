package psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.DecoratorController;

/**
 * The Class DecoratorControllerTest.
 */
public class DecoratorControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DecoratorController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
