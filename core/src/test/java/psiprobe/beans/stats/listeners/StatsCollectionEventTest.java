package psiprobe.beans.stats.listeners;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.listeners.StatsCollectionEvent;

/**
 * The Class StatsCollectionEventTest.
 */
public class StatsCollectionEventTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(StatsCollectionEvent.class).loadData().test();
  }

}
