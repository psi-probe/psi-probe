package com.googlecode.psiprobe.tools;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class MailerTest.
 */
public class MailerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Mailer.class).loadData().test();
  }

}
