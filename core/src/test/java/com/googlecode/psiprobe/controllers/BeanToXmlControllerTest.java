package com.googlecode.psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class BeanToXmlControllerTest.
 */
public class BeanToXmlControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(BeanToXmlController.class).skip("applicationContext", "supportedMethods").test();
  }

}
