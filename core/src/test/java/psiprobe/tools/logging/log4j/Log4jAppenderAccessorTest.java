/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.tools.logging.log4j;

import com.codebox.bean.JavaBeanTester;
import org.junit.jupiter.api.Test;

/**
 * The Class Log4jAppenderAccessorTest.
 */
public class Log4jAppenderAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(Log4JAppenderAccessor.class).skip("level").test();
  }

}
