package com.googlecode.psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class FilterMappingTest.
 */
public class FilterMappingTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(FilterMapping.class).loadData().test();
  }

}
