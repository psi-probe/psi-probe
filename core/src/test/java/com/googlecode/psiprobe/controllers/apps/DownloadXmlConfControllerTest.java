package com.googlecode.psiprobe.controllers.apps;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class DownloadXmlConfControllerTest.
 */
public class DownloadXmlConfControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DownloadXmlConfController.class).skip("applicationContext", "supportedMethods").test();
  }

}
