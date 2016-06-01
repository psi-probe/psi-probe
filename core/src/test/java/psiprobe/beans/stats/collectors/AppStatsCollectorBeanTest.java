package psiprobe.beans.stats.collectors;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.collectors.AppStatsCollectorBean;

/**
 * The Class AppStatsCollectorBeanTest.
 */
public class AppStatsCollectorBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(AppStatsCollectorBean.class).loadData().test();
  }

}
