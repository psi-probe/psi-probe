package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.ServletMapping;

/**
 * The Class ServletMappingTest.
 */
public class ServletMappingTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ServletMapping.class).loadData().test();
  }

}
