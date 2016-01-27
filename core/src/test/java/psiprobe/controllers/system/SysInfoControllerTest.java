package psiprobe.controllers.system;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.system.SysInfoController;

/**
 * The Class SysInfoControllerTest.
 */
public class SysInfoControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(SysInfoController.class).skip("applicationContext", "supportedMethods").test();
  }

}
