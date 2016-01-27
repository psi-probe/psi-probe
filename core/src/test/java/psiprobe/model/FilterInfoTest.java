package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.FilterInfo;

/**
 * The Class FilterInfoTest.
 */
public class FilterInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(FilterInfo.class).loadData().test();
  }

}
