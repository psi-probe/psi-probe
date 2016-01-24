package com.googlecode.psiprobe.tools.url;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class UrlParserTest.
 */
public class UrlParserTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(UrlParser.class).loadData().test();
  }

}
