package psiprobe.controllers.threads;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.threads.KillThreadController;

/**
 * The Class KillThreadControllerTest.
 */
public class KillThreadControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(KillThreadController.class).skip("applicationContext", "supportedMethods").test();
  }

}
