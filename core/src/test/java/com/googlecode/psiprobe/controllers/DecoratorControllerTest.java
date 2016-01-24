package com.googlecode.psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class DecoratorControllerTest.
 */
public class DecoratorControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DecoratorController.class).skip("applicationContext", "supportedMethods").test();
  }

}
