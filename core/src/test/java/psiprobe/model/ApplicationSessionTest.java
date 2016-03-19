package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.ApplicationSession;

/**
 * The Class ApplicationSessionTest.
 */
public class ApplicationSessionTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ApplicationSession.class).loadData().skip("creationTime", "lastAccessTime").test();
  }

}
