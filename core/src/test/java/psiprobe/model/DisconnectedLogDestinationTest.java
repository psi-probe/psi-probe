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
package psiprobe.model;

import org.junit.Ignore;
import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

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
