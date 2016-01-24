package com.googlecode.psiprobe.controllers.system;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class SysInfoControllerTest.
 */
public class SysInfoControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(SysInfoController.class).skip("applicationContext", "supportedMethods").test();
  }

}
