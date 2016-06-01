package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.ThreadStackElement;

/**
 * The Class ThreadStackElementTest.
 */
public class ThreadStackElementTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ThreadStackElement.class).loadData().test();
  }

}
