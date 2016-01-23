package com.googlecode.psiprobe.beans.stats.listeners;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

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
