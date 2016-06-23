package psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.jmx.RuntimeInformation;

/**
 * The Class RuntimeInformationTest.
 */
public class RuntimeInformationTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(RuntimeInformation.class).loadData().test();
  }

}
