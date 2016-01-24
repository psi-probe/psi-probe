package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.TomcatTestReport;

/**
 * The Class TomcatTestReportTest.
 */
public class TomcatTestReportTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(TomcatTestReport.class).loadData().test();
  }

}
