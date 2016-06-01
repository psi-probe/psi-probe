package psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.collectors.DatasourceStatsCollectorBean;

/**
 * The Class DatasourceStatsCollectorBeanTest.
 */
public class DatasourceStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DatasourceStatsCollectorBean.class).loadData().test();
  }

}
