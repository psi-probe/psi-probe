package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.FilterMapping;

/**
 * The Class FilterMappingTest.
 */
public class FilterMappingTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(FilterMapping.class).loadData().test();
  }

}
