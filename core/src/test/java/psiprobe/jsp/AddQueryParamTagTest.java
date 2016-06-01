package psiprobe.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.jsp.AddQueryParamTag;

/**
 * The Class AddQueryParamTagTest.
 */
public class AddQueryParamTagTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(AddQueryParamTag.class).loadData().test();
  }

}
