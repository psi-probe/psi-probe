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
package psiprobe.controllers.apps;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;

/**
 * The Class GetApplicationControllerTest.
 */
class GetApplicationControllerTest {

  /**
   * Javabean tester proc details.
   */
  @Test
  void javabeanTesterProcDetails() {
    JavaBeanTester.builder(GetApplicationProcDetailsController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester request details.
   */
  @Test
  void javabeanTesterRequestDetails() {
    JavaBeanTester.builder(GetApplicationRequestDetailsController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester runtime info.
   */
  @Test
  void javabeanTesterRuntimeInfo() {
    JavaBeanTester.builder(GetApplicationRuntimeInfoController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester summary.
   */
  @Test
  void javabeanTesterSummary() {
    JavaBeanTester.builder(GetApplicationSummaryController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

}
