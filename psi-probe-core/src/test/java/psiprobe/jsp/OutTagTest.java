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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codebox.bean.JavaBeanTester;

import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.BodyContent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class OutTagTest.
 */
class OutTagTest {

  private OutTag tag;
  private PageContext pageContext;
  private JspWriter out;

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(OutTag.class).loadData().skipStrictSerializable().test();
  }

  @BeforeEach
  void setUp() {
    tag = new OutTag();
    pageContext = mock(PageContext.class);
    out = mock(JspWriter.class);
    when(pageContext.getOut()).thenReturn(out);
    tag.setPageContext(pageContext);
  }

  @Test
  void testDoStartTagWithShortValue() throws Exception {
    tag.setValue("hello");
    int result = tag.doStartTag();
    assertEquals(OutTag.SKIP_BODY, result);
    verify(out).print("hello");
  }

  @Test
  void testDoStartTagWithNullValue() throws Exception {
    tag.setValue(null);
    // Should return super.doStartTag() which evaluates body
    int result = tag.doStartTag();
    assertEquals(OutTag.EVAL_BODY_BUFFERED, result);
  }

  @Test
  void testDoStartTagWithTruncationRight() throws Exception {
    tag.setValue("Hello World this is a long string");
    tag.setMaxLength(10);
    tag.setEllipsisRight(true);
    int result = tag.doStartTag();
    assertEquals(OutTag.SKIP_BODY, result);
    // Should write truncated with ... at end
    verify(out).print("<span title=\"Hello World this is a long string\">Hello W...</span>");
  }

  @Test
  void testDoStartTagWithTruncationLeft() throws Exception {
    tag.setValue("Hello World this is a long string");
    tag.setMaxLength(10);
    tag.setEllipsisRight(false);
    int result = tag.doStartTag();
    assertEquals(OutTag.SKIP_BODY, result);
    // Should write truncated with ... at beginning
    verify(out).print("<span title=\"Hello World this is a long string\">... string</span>");
  }

  @Test
  void testDoAfterBody() throws Exception {
    BodyContent bodyContent = mock(BodyContent.class);
    when(bodyContent.getString()).thenReturn("  body content  ");
    when(bodyContent.getEnclosingWriter()).thenReturn(out);
    tag.setBodyContent(bodyContent);

    int result = tag.doAfterBody();
    assertEquals(OutTag.SKIP_BODY, result);
    verify(out).print("body content");
  }
}
