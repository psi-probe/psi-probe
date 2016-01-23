package com.googlecode.psiprobe.controllers.apps;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ViewXmlConfControllerTest.
 */
public class ViewXmlConfControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ViewXmlConfController.class).skip("applicationContext", "supportedMethods").test();
  }

}
