package psiprobe.controllers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.BeanToXmlController;

/**
 * The Class BeanToXmlControllerTest.
 */
public class BeanToXmlControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(BeanToXmlController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
