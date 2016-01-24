package psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.collectors.ClusterStatsCollectorBean;

/**
 * The Class ClusterStatsCollectorBeanTest.
 */
public class ClusterStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ClusterStatsCollectorBean.class).loadData().test();
  }

}
