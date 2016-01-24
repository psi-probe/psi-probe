package psiprobe.model;

import org.junit.Ignore;
import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.DisconnectedLogDestination;

/**
 * The Class DisconnectedLogDestinationTest.
 */
public class DisconnectedLogDestinationTest {

  /**
   * Javabean tester.
   */
  @Ignore
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DisconnectedLogDestination.class).loadData().test();
  }

}
