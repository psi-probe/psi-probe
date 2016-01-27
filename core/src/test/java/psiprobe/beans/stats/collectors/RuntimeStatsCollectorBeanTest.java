package psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.collectors.RuntimeStatsCollectorBean;

/**
 * The Class RuntimeStatsCollectorBeanTest.
 */
public class RuntimeStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(RuntimeStatsCollectorBean.class).loadData().test();
  }

}
