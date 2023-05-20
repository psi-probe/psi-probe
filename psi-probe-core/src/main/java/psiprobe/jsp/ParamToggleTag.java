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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.bind.ServletRequestUtils;

/**
 * The Class ParamToggleTag.
 */
public class ParamToggleTag extends TagSupport {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The param. */
  private String param = "size";

  @Override
  public int doStartTag() throws JspException {
    boolean getSize =
        ServletRequestUtils.getBooleanParameter(pageContext.getRequest(), param, false);
    StringBuilder query = new StringBuilder();
    query.append(param).append("=").append(!getSize);
    String encoding = pageContext.getResponse().getCharacterEncoding();
    for (String name : Collections.list(pageContext.getRequest().getParameterNames())) {
      if (!param.equals(name)) {
        try {
          String value = ServletRequestUtils.getStringParameter(pageContext.getRequest(), name, "");
          String encodedValue = URLEncoder.encode(value, encoding);
          query.append("&").append(name).append("=").append(encodedValue);
        } catch (UnsupportedEncodingException e) {
          throw new JspException(e);
        }
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

}
