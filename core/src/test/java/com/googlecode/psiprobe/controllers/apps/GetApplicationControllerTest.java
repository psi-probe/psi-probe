package com.googlecode.psiprobe.controllers.apps;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class GetApplicationControllerTest.
 */
public class GetApplicationControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(GetApplicationController.class).skip("applicationContext", "supportedMethods").test();
  }

}
