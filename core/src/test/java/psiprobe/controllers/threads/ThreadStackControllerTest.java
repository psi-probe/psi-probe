package psiprobe.controllers.threads;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.threads.ThreadStackController;

/**
 * The Class ThreadStackControllerTest.
 */
public class ThreadStackControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ThreadStackController.class).skip("applicationContext", "supportedMethods").test();
  }

}
