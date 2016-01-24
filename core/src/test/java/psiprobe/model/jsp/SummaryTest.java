package psiprobe.model.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.jsp.Summary;

public class SummaryTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Summary.class).loadData().test();
  }

}
