package psiprobe.beans.stats.providers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.providers.StandardSeriesProvider;

/**
 * The Class StandardSeriesProviderTest.
 */
public class StandardSeriesProviderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(StandardSeriesProvider.class).loadData().test();
  }

}
