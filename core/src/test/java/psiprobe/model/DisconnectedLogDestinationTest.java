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
  // TODO JavaBeanTester doesn't currently handle complex object into constructor.
  @Ignore
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(DisconnectedLogDestination.class).loadData().test();
  }

}
