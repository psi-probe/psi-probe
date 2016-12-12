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
package psiprobe.controllers.apps;

import org.junit.Test;

import com.codebox.bean.JavaBeanTester;

/**
 * The Class GetApplicationControllerTest.
 */
public class GetApplicationControllerTest {

  /**
   * Javabean tester proc details.
   */
  public void javabeanTesterProcDetails() {
    JavaBeanTester.builder(GetApplicationProcDetailsController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester request details.
   */
  @Test
  public void javabeanTesterRequestDetails() {
    JavaBeanTester.builder(GetApplicationRequestDetailsController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester runtime.
   */
  @Test
  public void javabeanTesterRuntime() {
    JavaBeanTester.builder(GetApplicationRuntimeInfoController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester summary.
   */
  @Test
  public void javabeanTesterSummary() {
    JavaBeanTester.builder(GetApplicationSummaryController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
