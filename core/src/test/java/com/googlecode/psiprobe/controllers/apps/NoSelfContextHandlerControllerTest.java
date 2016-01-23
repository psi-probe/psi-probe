package com.googlecode.psiprobe.controllers.apps;

import org.junit.Ignore;
import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class NoSelfContextHandlerControllerTest.
 */
public class NoSelfContextHandlerControllerTest {

  /**
   * Javabean tester.
   */
  @Ignore
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(NoSelfContextHandlerController.class).skip("applicationContext", "supportedMethods").test();
  }

}
