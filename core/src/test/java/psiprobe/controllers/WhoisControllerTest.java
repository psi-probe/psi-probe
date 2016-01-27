package psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.WhoisController;

/**
 * The Class WhoisControllerTest.
 */
public class WhoisControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(WhoisController.class).skip("applicationContext", "supportedMethods").test();
  }

}
