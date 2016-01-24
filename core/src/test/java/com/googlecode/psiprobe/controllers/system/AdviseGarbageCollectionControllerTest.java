package com.googlecode.psiprobe.controllers.system;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class AdviseGarbageCollectionControllerTest.
 */
public class AdviseGarbageCollectionControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(AdviseGarbageCollectionController.class).skip("applicationContext", "supportedMethods").test();
  }

}
