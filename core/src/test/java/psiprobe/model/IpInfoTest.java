package psiprobe.model;

import org.junit.Ignore;
import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.IpInfo;

/**
 * The Class IpInfoTest.
 */
public class IpInfoTest {

  /**
   * Javabean tester.
   */
  // TODO JavaBeanTester doesn't currently handle complex object into constructor.
  @Ignore
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(IpInfo.class).loadData().test();
  }

}
