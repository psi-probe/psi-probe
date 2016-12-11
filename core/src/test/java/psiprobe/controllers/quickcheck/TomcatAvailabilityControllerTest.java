/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.controllers.quickcheck;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class TomcatAvailabilityControllerTest.
 */
public class TomcatAvailabilityControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  public void javabeanTester() {
    JavaBeanTester.builder(TomcatAvailabilityController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester xml.
   */
  @Test
  public void javabeanTesterXml() {
    JavaBeanTester.builder(TomcatAvailabilityXmlController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
