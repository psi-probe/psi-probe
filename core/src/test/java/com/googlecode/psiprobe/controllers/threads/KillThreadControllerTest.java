package com.googlecode.psiprobe.controllers.threads;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class KillThreadControllerTest.
 */
public class KillThreadControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(KillThreadController.class).skip("applicationContext", "supportedMethods").test();
  }

}
