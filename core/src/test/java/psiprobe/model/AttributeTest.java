package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.Attribute;

/**
 * The Class AttributeTest.
 */
public class AttributeTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Attribute.class).loadData().test();
  }

}
