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
package psiprobe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LogbackConfigTest.
 */
public class LogbackConfigTest {

  /**
   * Test.
   */
  @Test
  public void test() {
    Logger log = LoggerFactory.getLogger(getClass());
    Assertions.assertNotEquals("slf4j-jdk14-1.7.7.jar is on the classpath, but it should NOT be.",
        "org.slf4j.impl.JDK14LoggerAdapter", log.getClass().getName());
  }

}
