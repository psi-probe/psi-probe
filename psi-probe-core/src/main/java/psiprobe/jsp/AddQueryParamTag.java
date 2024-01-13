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
import java.util.Collections;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * The Class AddQueryParamTag.
 */
public class AddQueryParamTag extends TagSupport {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The param. */
  private String param;

  /** The value. */
  private String value;

  @Override
  public int doStartTag() throws JspException {
    StringBuilder query = new StringBuilder();
    query.append(param).append('=').append(value);
    for (String name : Collections.list(pageContext.getRequest().getParameterNames())) {
      if (!param.equals(name)) {
        query.append('&').append(StringEscapeUtils.escapeHtml4(name)).append('=')
            .append(StringEscapeUtils.escapeHtml4(
                ServletRequestUtils.getStringParameter(pageContext.getRequest(), name, "")));
      }
    }
    try {
      pageContext.getOut().print(query);
    } catch (IOException e) {
      throw new JspException("Exception printing query string to JspWriter", e);
    }
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Gets the param.
   *
   * @return the param
   */
  public String getParam() {
    return param;
  }

  /**
   * Sets the param.
   *
   * @param param the new param
   */
  public void setParam(String param) {
    this.param = param;
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value the new value
   */
  public void setValue(String value) {
    this.value = value;
  }

}
