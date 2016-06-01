package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.SessionSearchInfo;

/**
 * The Class SessionSearchInfoTest.
 */
public class SessionSearchInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(SessionSearchInfo.class).loadData().test();
  }

}
