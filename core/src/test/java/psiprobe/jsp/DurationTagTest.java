package psiprobe.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.jsp.DurationTag;

/**
 * The Class DurationTagTest.
 */
public class DurationTagTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DurationTag.class).loadData().test();
  }

}
