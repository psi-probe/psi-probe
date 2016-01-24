package com.googlecode.psiprobe.tools.logging;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class FileLogAccessorTest.
 */
public class FileLogAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(FileLogAccessor.class).loadData().test();
  }

}
