package com.googlecode.psiprobe.tools.logging;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

public class DefaultAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DefaultAccessor.class).loadData().test();
  }

}
