package psiprobe.controllers.apps;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class NoSelfContextHandlerControllerTest.
 */
public class NoSelfContextHandlerControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(NoSelfContextHandlerControllerImpl.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
