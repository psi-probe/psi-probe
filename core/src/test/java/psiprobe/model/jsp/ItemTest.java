package psiprobe.model.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.jsp.Item;

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
