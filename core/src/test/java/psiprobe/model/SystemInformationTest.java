package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.SystemInformation;

/**
 * The Class SystemInformationTest.
 */
public class SystemInformationTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(SystemInformation.class).loadData().test();
  }

}
