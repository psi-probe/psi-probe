package psiprobe.tools.logging.log4j;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.tools.logging.log4j.Log4JAppenderAccessor;

/**
 * The Class Log4jAppenderAccessorTest.
 */
public class Log4jAppenderAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Log4JAppenderAccessor.class).skip("level").test();
  }

}
