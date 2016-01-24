package psiprobe.controllers.apps;

import org.junit.Ignore;
import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.apps.NoSelfContextHandlerController;

/**
 * The Class NoSelfContextHandlerControllerTest.
 */
public class NoSelfContextHandlerControllerTest {

  /**
   * Javabean tester.
   */
  @Ignore
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(NoSelfContextHandlerController.class).skip("applicationContext", "supportedMethods").test();
  }

}
