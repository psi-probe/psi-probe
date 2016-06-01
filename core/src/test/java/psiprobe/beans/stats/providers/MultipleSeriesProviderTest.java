package psiprobe.beans.stats.providers;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.providers.MultipleSeriesProvider;

/**
 * The Class MultipleSeriesProviderTest.
 */
public class MultipleSeriesProviderTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(MultipleSeriesProvider.class).loadData().test();
  }

}
