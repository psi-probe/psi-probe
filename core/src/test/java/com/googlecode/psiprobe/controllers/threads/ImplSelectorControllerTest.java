package com.googlecode.psiprobe.controllers.threads;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ImplSelectorControllerTest.
 */
public class ImplSelectorControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ImplSelectorController.class).skip("applicationContext", "supportedMethods").test();
  }

}
