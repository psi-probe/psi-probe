package psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.jmx.SyncClusterSender;

/**
 * The Class SyncClusterSenderTest.
 */
public class SyncClusterSenderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(SyncClusterSender.class).loadData().test();
  }

}
