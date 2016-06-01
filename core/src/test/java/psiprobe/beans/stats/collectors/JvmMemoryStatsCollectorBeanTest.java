package psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.collectors.JvmMemoryStatsCollectorBean;

/**
 * The Class JvmMemoryStatsCollectorBeanTest.
 */
public class JvmMemoryStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(JvmMemoryStatsCollectorBean.class).loadData().test();
  }

}
