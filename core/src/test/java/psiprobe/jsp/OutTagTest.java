package psiprobe.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.jsp.OutTag;

/**
 * The Class OutTagTest.
 */
public class OutTagTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(OutTag.class).loadData().test();
  }

}
