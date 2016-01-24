package com.googlecode.psiprobe.tools.logging.jdk;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class Jdk14HandlerAccessorTest.
 */
public class Jdk14HandlerAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Jdk14HandlerAccessor.class).skip("level").test();
  }

}
