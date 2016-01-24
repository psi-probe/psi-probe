package psiprobe.model;

import org.junit.Ignore;
import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.DataSourceInfoGroup;

/**
 * The Class DataSourceInfoGroupTest.
 */
public class DataSourceInfoGroupTest {

  /**
   * Javabean tester.
   */
  @Ignore
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DataSourceInfoGroup.class).loadData().test();
  }

}
