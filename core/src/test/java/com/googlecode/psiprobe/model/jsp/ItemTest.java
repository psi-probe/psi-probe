package com.googlecode.psiprobe.model.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ItemTest.
 */
public class ItemTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Item.class).skip("LastModified").test();
  }

}
