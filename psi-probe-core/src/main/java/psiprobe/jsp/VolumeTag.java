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

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

import psiprobe.tools.SizeExpression;

/**
 * JSP tag to convert size from bytes into human readable form: KB, MB, GB or TB depending on how
 * large the value in bytes is.
 */
public class VolumeTag extends TagSupport {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The value. */
  private long value;

  /** The fractions. */
  private int fractions;

  /**
   * Sets the value.
   *
   * @param value the new value
   */
  public void setValue(long value) {
    this.value = value;
  }

  /**
   * Gets the fractions.
   *
   * @return the fractions
   */
  public int getFractions() {
    return fractions;
  }

  /**
   * Sets the fractions.
   *
   * @param fractions the new fractions
   */
  public void setFractions(int fractions) {
    this.fractions = fractions;
  }

  @Override
  public int doStartTag() throws JspException {
    String title = Long.toString(value);
    String newValue = SizeExpression.format(value, fractions, true);
    try {
      pageContext.getOut().write("<span title=\"" + title + "\">" + newValue + "</span>");
    } catch (IOException e) {
      throw new JspException("Exception writing value to JspWriter", e);
    }

    return EVAL_BODY_INCLUDE;
  }

}
