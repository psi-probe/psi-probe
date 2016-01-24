package com.googlecode.psiprobe.controllers.wrapper;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

public class StopJvmControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(StopJvmController.class).skip("applicationContext", "supportedMethods").test();
  }

}
