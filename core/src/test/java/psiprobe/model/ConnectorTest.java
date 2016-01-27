package psiprobe.model;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.Connector;

/**
 * The Class ConnectorTest.
 */
public class ConnectorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Connector.class).loadData().test();
  }

}
