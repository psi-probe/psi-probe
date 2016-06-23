package psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.collectors.ConnectorStatsCollectorBean;

/**
 * The Class ConnectorStatsCollectorBeanTest.
 */
public class ConnectorStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ConnectorStatsCollectorBean.class).loadData().test();
  }

}
