package com.googlecode.psiprobe.controllers.threads;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ThreadStackControllerTest.
 */
public class ThreadStackControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ThreadStackController.class).skip("applicationContext", "supportedMethods").test();
  }

}
