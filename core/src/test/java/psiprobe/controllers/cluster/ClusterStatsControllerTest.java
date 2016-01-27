package psiprobe.controllers.cluster;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.cluster.ClusterStatsController;

/**
 * The Class ClusterStatsControllerTest.
 */
public class ClusterStatsControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ClusterStatsController.class).skip("applicationContext", "supportedMethods").test();
  }

}
