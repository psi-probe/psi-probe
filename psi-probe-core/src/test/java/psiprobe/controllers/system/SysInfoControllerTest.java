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
package psiprobe.controllers.system;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;

/**
 * The Class SysInfoControllerTest.
 */
class SysInfoControllerTest {

  /**
   * Javabean tester os info ajax.
   */
  @Test
  void javabeanTesterOsInfoAjax() {
    JavaBeanTester.builder(OsInfoAjaxController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester os info.
   */
  @Test
  void javabeanTesterOsInfo() {
    JavaBeanTester.builder(OsInfoController.class).skip("applicationContext", "supportedMethods")
        .test();
  }

  /**
   * Javabean tester sys info.
   */
  @Test
  void javabeanTesterSysInfo() {
    JavaBeanTester.builder(SysInfoController.class).skip("applicationContext", "supportedMethods")
        .test();
  }

  /**
   * Javabean tester sys props.
   */
  @Test
  void javabeanTesterSysProps() {
    JavaBeanTester.builder(SysPropsController.class).skip("applicationContext", "supportedMethods")
        .test();
  }

}
