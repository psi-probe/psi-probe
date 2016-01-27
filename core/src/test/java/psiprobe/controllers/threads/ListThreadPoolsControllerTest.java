package psiprobe.controllers.threads;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.threads.ListThreadPoolsController;

/**
 * The Class ListThreadPoolsControllerTest.
 */
public class ListThreadPoolsControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ListThreadPoolsController.class).skip("applicationContext", "supportedMethods").test();
  }

}
