package psiprobe.tools.logging;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.tools.logging.FileLogAccessor;

/**
 * The Class FileLogAccessorTest.
 */
public class FileLogAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(FileLogAccessor.class).loadData().test();
  }

}
