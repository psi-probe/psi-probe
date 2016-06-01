package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.ApplicationParam;

/**
 * The Class ApplicationParamTest.
 */
public class ApplicationParamTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ApplicationParam.class).loadData().test();
  }

}
