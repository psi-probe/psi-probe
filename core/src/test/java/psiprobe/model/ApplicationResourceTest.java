package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.ApplicationResource;

/**
 * The Class ApplicationResourceTest.
 */
public class ApplicationResourceTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ApplicationResource.class).loadData().test();
  }

}
