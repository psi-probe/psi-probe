package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.RequestProcessor;

/**
 * The Class RequestProcessorTest.
 */
public class RequestProcessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(RequestProcessor.class).loadData().test();
  }

}
