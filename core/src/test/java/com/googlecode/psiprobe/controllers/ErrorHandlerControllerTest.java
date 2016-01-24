package com.googlecode.psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

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
