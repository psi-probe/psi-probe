package com.googlecode.psiprobe.tools;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class MailMessageTest.
 */
public class MailMessageTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(MailMessage.class).loadData().test();
  }

}
