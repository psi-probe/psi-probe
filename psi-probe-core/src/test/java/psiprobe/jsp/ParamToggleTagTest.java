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
 * The Class ParamToggleTagTest.
 */
class ParamToggleTagTest {

  private ParamToggleTag tag;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockPageContext pageContext;

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(ParamToggleTag.class).loadData().skipStrictSerializable().test();
  }

  @BeforeEach
  void setUp() {
    MockServletContext servletContext = new MockServletContext();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    pageContext = new MockPageContext(servletContext, request, response);
    tag = new ParamToggleTag();
    tag.setPageContext(pageContext);
  }

  @Test
  void testDoStartTagTogglesFalseToTrue() throws Exception {
    request.addParameter("size", "false");
    int result = tag.doStartTag();
    assertEquals(ParamToggleTag.EVAL_BODY_INCLUDE, result);
    String output = response.getContentAsString();
    assertTrue(output.contains("size=true"));
  }

  @Test
  void testDoStartTagTogglesTrueToFalse() throws Exception {
    request.addParameter("size", "true");
    int result = tag.doStartTag();
    assertEquals(ParamToggleTag.EVAL_BODY_INCLUDE, result);
    String output = response.getContentAsString();
    assertTrue(output.contains("size=false"));
  }

  @Test
  void testDoStartTagWithOtherParams() throws Exception {
    request.addParameter("foo", "bar");
    request.addParameter("baz", "qux");
    int result = tag.doStartTag();
    assertEquals(ParamToggleTag.EVAL_BODY_INCLUDE, result);
    String output = response.getContentAsString();
    // Other params should be included in query
    assertTrue(output.contains("foo=bar") || output.contains("baz=qux"));
    assertTrue(output.contains("size="));
  }

  @Test
  void testDoStartTagCustomParam() throws Exception {
    tag.setParam("expanded");
    request.addParameter("expanded", "true");
    int result = tag.doStartTag();
    assertEquals(ParamToggleTag.EVAL_BODY_INCLUDE, result);
    String output = response.getContentAsString();
    assertTrue(output.contains("expanded=false"));
  }
}
