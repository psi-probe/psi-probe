package psiprobe.jsp;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.jsp.VolumeTag;

/**
 * The Class VolumeTagTest.
 */
public class VolumeTagTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(VolumeTag.class).loadData().test();
  }

}
