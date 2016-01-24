package com.googlecode.psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class TomcatAvailabilityControllerTest.
 */
public class TomcatAvailabilityControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(TomcatAvailabilityController.class).skip("applicationContext", "supportedMethods").test();
  }

}
