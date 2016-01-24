package psiprobe.tools.logging.logback;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.tools.logging.logback.LogbackAppenderAccessor;

/**
 * The Class LogbackAppenderAccessorTest.
 */
public class LogbackAppenderAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(LogbackAppenderAccessor.class).skip("level").test();
  }

}
