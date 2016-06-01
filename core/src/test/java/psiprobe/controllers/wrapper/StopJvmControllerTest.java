package psiprobe.controllers.wrapper;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.wrapper.StopJvmController;

public class StopJvmControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(StopJvmController.class).skip("applicationContext", "supportedMethods").test();
  }

}
