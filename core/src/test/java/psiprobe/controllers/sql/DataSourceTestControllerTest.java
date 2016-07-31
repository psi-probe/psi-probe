package psiprobe.controllers.sql;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.controllers.sql.DataSourceTestController;

/**
 * The Class DataSourceTestControllerTest.
 */
public class DataSourceTestControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DataSourceTestController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
