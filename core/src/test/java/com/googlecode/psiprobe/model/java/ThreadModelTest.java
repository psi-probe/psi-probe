package com.googlecode.psiprobe.model.java;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ThreadModelTest.
 */
public class ThreadModelTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ThreadModel.class).loadData().test();
  }

}
