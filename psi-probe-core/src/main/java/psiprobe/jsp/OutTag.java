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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.text.StringEscapeUtils;

/**
 * The Class OutTag.
 */
public class OutTag extends BodyTagSupport {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The max length. */
  private int maxLength = -1;

  /** The ellipsis right. */
  private boolean ellipsisRight = true;

  /** The value. */
  private Object value;

  /**
   * Gets the value.
   *
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value the new value
   */
  public void setValue(Object value) {
    this.value = value;
  }

  /**
   * Gets the max length.
   *
   * @return the max length
   */
  public int getMaxLength() {
    return maxLength;
  }

  /**
   * Sets the max length.
   *
   * @param maxLength the new max length
   */
  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * Checks if is ellipsis right.
   *
   * @return true, if is ellipsis right
   */
  public boolean isEllipsisRight() {
    return ellipsisRight;
  }

  /**
   * Sets the ellipsis right.
   *
   * @param ellipsisRight the new ellipsis right
   */
  public void setEllipsisRight(boolean ellipsisRight) {
    this.ellipsisRight = ellipsisRight;
  }

  @Override
  public int doStartTag() throws JspException {
    if (value != null) {
      print(value.toString(), pageContext.getOut());
      return SKIP_BODY;
    }
    return super.doStartTag();
  }

  @Override
  public int doAfterBody() throws JspException {
    print(getBodyContent().getString().trim(), getBodyContent().getEnclosingWriter());
    return SKIP_BODY;
  }

  /**
   * Prints the.
   *
   * @param displayValue the display value
   * @param out the out
   *
   * @throws JspException the jsp exception
   */
  private void print(String displayValue, JspWriter out) throws JspException {
    try {
      if (maxLength != -1 && displayValue.length() > maxLength) {
        String newValue;
        if (ellipsisRight) {
          newValue = displayValue.substring(0, maxLength - 3) + "...";
        } else {
          newValue = "..." + displayValue.substring(displayValue.length() - maxLength + 3);
        }
        String title = StringEscapeUtils.escapeHtml4(displayValue);
        out.print("<span title=\"" + title + "\">" + newValue + "</span>");
      } else {
        out.print(displayValue);
      }
    } catch (IOException e) {
      throw new JspException(e);
    }
  }

}
