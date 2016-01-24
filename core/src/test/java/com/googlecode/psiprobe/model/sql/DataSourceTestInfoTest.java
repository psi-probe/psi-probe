package com.googlecode.psiprobe.model.sql;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class DataSourceTestInfoTest.
 */
public class DataSourceTestInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DataSourceTestInfo.class).loadData().test();
  }

}
