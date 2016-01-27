package psiprobe.tools.logging;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.tools.logging.DefaultAccessor;

public class DefaultAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DefaultAccessor.class).loadData().test();
  }

}
