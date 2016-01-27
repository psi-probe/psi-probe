package psiprobe.controllers.system;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.system.MemoryStatsController;

/**
 * The Class MemoryStatsControllerTest.
 */
public class MemoryStatsControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(MemoryStatsController.class).skip("applicationContext", "supportedMethods").test();
  }

}
