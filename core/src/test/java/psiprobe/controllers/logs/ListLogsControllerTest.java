package psiprobe.controllers.logs;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.logs.ListLogsController;

/**
 * The Class ListLogsControllerTest.
 */
public class ListLogsControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ListLogsController.class).skip("applicationContext", "supportedMethods").test();
  }

}
