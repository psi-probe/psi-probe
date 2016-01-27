package psiprobe.tools.url;

import org.junit.Ignore;
import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

import psiprobe.tools.url.UrlParser;

/**
 * The Class UrlParserTest.
 */
public class UrlParserTest {

  /**
   * Javabean tester.
   */
  // TODO 1/25/16 Waiting on fix in JavaBeanTester due to no no-arg constructor
  @Ignore
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(UrlParser.class).loadData().test();
  }

}
