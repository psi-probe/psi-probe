package psiprobe.beans.stats.listeners;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.stats.listeners.MemoryPoolMailingListener;

/**
 * The Class MemoryPoolMailingListenerTest.
 */
public class MemoryPoolMailingListenerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(MemoryPoolMailingListener.class).loadData().test();
  }

}
