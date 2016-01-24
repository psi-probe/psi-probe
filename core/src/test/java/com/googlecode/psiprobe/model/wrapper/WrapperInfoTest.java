package com.googlecode.psiprobe.model.wrapper;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class WrapperInfoTest.
 */
public class WrapperInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(WrapperInfo.class).loadData().test();
  }

}
