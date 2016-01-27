package psiprobe.tools.logging.slf4jlogback;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.tools.logging.slf4jlogback.TomcatSlf4jLogbackAppenderAccessor;

/**
 * The Class TomcatSlf4jLogbackAppenderAccessorTest.
 */
public class TomcatSlf4jLogbackAppenderAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(TomcatSlf4jLogbackAppenderAccessor.class).skip("level").test();
  }

}
