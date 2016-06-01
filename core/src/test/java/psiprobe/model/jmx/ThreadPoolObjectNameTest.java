package psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.jmx.ThreadPoolObjectName;

public class ThreadPoolObjectNameTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ThreadPoolObjectName.class).loadData().test();
  }

}
