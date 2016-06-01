package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.ThreadPool;

/**
 * The Class ThreadPoolTest.
 */
public class ThreadPoolTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ThreadPool.class).loadData().test();
  }

}
