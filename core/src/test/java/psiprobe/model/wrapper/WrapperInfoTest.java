package psiprobe.model.wrapper;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.model.wrapper.WrapperInfo;

/**
 * The Class WrapperInfoTest.
 */
public class WrapperInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(WrapperInfo.class).loadData().test();
  }

}
