package psiprobe.controllers.threads;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.threads.ImplSelectorController;

/**
 * The Class ImplSelectorControllerTest.
 */
public class ImplSelectorControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ImplSelectorController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
