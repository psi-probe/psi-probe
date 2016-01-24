package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.TransportableModel;

/**
 * The Class TransportableModelTest.
 */
public class TransportableModelTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(TransportableModel.class).loadData().test();
  }

}
