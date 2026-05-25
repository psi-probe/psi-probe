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
package psiprobe.controllers.error;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * The Class Error403ControllerTest.
 */
class Error403ControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(Error403Controller.class).skip("applicationContext", "supportedMethods")
        .test();
  }

  @Test
  void handleRequestReturnsAjaxViewForAjaxUris() throws Exception {
    Error403Controller controller = new Error403Controller();
    controller.setViewName("errors/403");
    controller.setAjaxViewName("errors/403_ajax");
    controller.setAjaxExtension(".ajax");

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/403.htm");
    request.setAttribute("jakarta.servlet.error.request_uri", "/sql/query.ajax");

    assertEquals("errors/403_ajax",
        controller.handleRequest(request, new MockHttpServletResponse()).getViewName());
  }

  @Test
  void handleRequestReturnsDefaultViewForNonAjaxUris() throws Exception {
    Error403Controller controller = new Error403Controller();
    controller.setViewName("errors/403");
    controller.setAjaxViewName("errors/403_ajax");
    controller.setAjaxExtension(".ajax");

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/403.htm");
    request.setAttribute("jakarta.servlet.error.request_uri", "/home.htm");

    assertEquals("errors/403",
        controller.handleRequest(request, new MockHttpServletResponse()).getViewName());
  }

}
