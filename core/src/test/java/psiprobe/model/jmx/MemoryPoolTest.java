package psiprobe.model.jmx;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.jmx.MemoryPool;

public class MemoryPoolTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(MemoryPool.class).loadData().test();
  }

}
