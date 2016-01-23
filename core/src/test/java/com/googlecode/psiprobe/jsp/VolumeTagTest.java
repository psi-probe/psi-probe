package com.googlecode.psiprobe.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class VolumeTagTest.
 */
public class VolumeTagTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(VolumeTag.class).loadData().test();
  }

}
