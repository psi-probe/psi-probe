package psiprobe.controllers.apps;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class ReloadContextControllerTest.
 */
public class ReloadContextControllerTest {

    /**
     * Javabean tester.
     */
    @Test
    public void javabeanTester() {
      JavaBeanTester.builder(ReloadContextController.class)
          .skip("applicationContext", "supportedMethods").test();
    }

}
