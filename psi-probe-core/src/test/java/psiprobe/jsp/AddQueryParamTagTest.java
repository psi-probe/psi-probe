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
package psiprobe.jsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

/**
 * The Class AddQueryParamTagTest.
 */
class AddQueryParamTagTest {

  private AddQueryParamTag tag;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockPageContext pageContext;

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(AddQueryParamTag.class).loadData().skipStrictSerializable().test();
  }

  @BeforeEach
  void setUp() {
    MockServletContext servletContext = new MockServletContext();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    pageContext = new MockPageContext(servletContext, request, response);
    tag = new AddQueryParamTag();
    tag.setPageContext(pageContext);
  }

  @Test
  void testDoStartTagNoExistingParams() throws Exception {
    tag.setParam("sort");
    tag.setValue("name");
    int result = tag.doStartTag();
    assertEquals(AddQueryParamTag.EVAL_BODY_INCLUDE, result);
    String output = response.getContentAsString();
    assertEquals("sort=name", output);
  }

  @Test
  void testDoStartTagWithExistingParams() throws Exception {
    request.addParameter("page", "1");
    request.addParameter("filter", "active");
    tag.setParam("sort");
    tag.setValue("size");
    int result = tag.doStartTag();
    assertEquals(AddQueryParamTag.EVAL_BODY_INCLUDE, result);
    String output = response.getContentAsString();
    assertTrue(output.startsWith("sort=size"));
    assertTrue(output.contains("page=1"));
    assertTrue(output.contains("filter=active"));
  }

  @Test
  void testDoStartTagReplaceExistingParam() throws Exception {
    // The existing param with the same name should be excluded
    request.addParameter("sort", "oldvalue");
    tag.setParam("sort");
    tag.setValue("newvalue");
    int result = tag.doStartTag();
    assertEquals(AddQueryParamTag.EVAL_BODY_INCLUDE, result);
    String output = response.getContentAsString();
    assertEquals("sort=newvalue", output);
  }
}
