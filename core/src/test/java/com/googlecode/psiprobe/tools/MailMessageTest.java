package com.googlecode.psiprobe.tools;

import org.junit.Ignore;
import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class MailMessageTest.
 */
public class MailMessageTest {

  /**
   * Javabean tester.
   */
  // TODO 1/25/16 Waiting on fix in JavaBeanTester due to no no-arg constructor
  @Ignore
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(MailMessage.class).loadData().test();
  }

}
