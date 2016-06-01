package psiprobe.tools;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.tools.MailMessage;

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
