package psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.jmx.PooledClusterSender;

/**
 * The Class PooledClusterSenderTest.
 */
public class PooledClusterSenderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(PooledClusterSender.class).loadData().test();
  }

}
