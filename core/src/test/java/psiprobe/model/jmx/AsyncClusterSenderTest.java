package psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.jmx.AsyncClusterSender;

/**
 * The Class AsyncClusterSenderTest.
 */
public class AsyncClusterSenderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(AsyncClusterSender.class).loadData().test();
  }

}
