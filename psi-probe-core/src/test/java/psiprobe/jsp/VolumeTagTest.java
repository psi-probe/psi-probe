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
 * The Class VolumeTagTest.
 */
class VolumeTagTest {

  private VolumeTag tag;
  private MockHttpServletResponse response;
  private MockPageContext pageContext;

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(VolumeTag.class).loadData().skipStrictSerializable().test();
  }

  @BeforeEach
  void setUp() {
    MockServletContext servletContext = new MockServletContext();
    MockHttpServletRequest request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    pageContext = new MockPageContext(servletContext, request, response);
    tag = new VolumeTag();
    tag.setPageContext(pageContext);
  }

  @Test
  void testDoStartTagWithSmallValue() throws Exception {
    tag.setValue(512L);
    tag.setFractions(0);
    int result = tag.doStartTag();
    assertEquals(VolumeTag.EVAL_BODY_INCLUDE, result);
    String output = response.getContentAsString();
    assertTrue(output.contains("512"));
    assertTrue(output.contains("title=\"512\""));
  }

  @Test
  void testDoStartTagWithLargeValue() throws Exception {
    tag.setValue(1024L * 1024L); // 1 MB
    tag.setFractions(0);
    int result = tag.doStartTag();
    assertEquals(VolumeTag.EVAL_BODY_INCLUDE, result);
    String output = response.getContentAsString();
    assertTrue(output.contains("MB") || output.contains("1"));
  }
}
