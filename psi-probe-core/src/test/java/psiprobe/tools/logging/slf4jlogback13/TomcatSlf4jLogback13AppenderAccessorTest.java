/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.tools.logging.slf4jlogback13;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;

/**
 * The Class TomcatSlf4jLogbackAppenderAccessorTest.
 */
class TomcatSlf4jLogback13AppenderAccessorTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(TomcatSlf4jLogback13AppenderAccessor.class).skip("level").test();
  }

}
