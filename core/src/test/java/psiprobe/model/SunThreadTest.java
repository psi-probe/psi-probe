package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.SunThread;

/**
 * The Class SunThreadTest.
 */
public class SunThreadTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(SunThread.class).loadData().test();
  }

}
