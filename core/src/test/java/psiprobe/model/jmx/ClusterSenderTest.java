package psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.jmx.ClusterSender;

/**
 * The Class ClusterSenderTest.
 */
public class ClusterSenderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ClusterSender.class).loadData().test();
  }

}
