package com.googlecode.psiprobe.controllers.deploy;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class UndeployContextControllerTest.
 */
public class UndeployContextControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(UndeployContextController.class).skip("applicationContext", "supportedMethods").test();
  }

}
