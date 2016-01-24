package com.googlecode.psiprobe.controllers.logs;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ListLogsControllerTest.
 */
public class ListLogsControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ListLogsController.class).skip("applicationContext", "supportedMethods").test();
  }

}
