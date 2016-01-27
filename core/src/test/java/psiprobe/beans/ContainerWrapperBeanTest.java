package psiprobe.beans;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.beans.ContainerWrapperBean;

/**
 * The Class ContainerWrapperBeanTest.
 */
public class ContainerWrapperBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(ContainerWrapperBean.class).loadData().test();
  }

}
