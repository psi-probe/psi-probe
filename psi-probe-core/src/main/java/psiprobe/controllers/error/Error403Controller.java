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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * The ErrorHandlerController will show two different views depending on whether the failed request
 * was AJAX or not.
 */
@Controller
public class Error403Controller extends AbstractController {

  /** The view name. */
  private String viewName;

  /** The ajax view name. */
  private String ajaxViewName;

  /** The ajax extension. */
  private String ajaxExtension;

  /**
   * Gets the view name.
   *
   * @return the view name
   */
  public String getViewName() {
    return viewName;
  }

  /**
   * Sets the view name.
   *
   * @param viewName the new view name
   */
  @Value("errors/403")
  public void setViewName(String viewName) {
    this.viewName = viewName;
  }

  /**
   * Gets the ajax view name.
   *
   * @return the ajax view name
   */
  public String getAjaxViewName() {
    return ajaxViewName;
  }

  /**
   * Sets the ajax view name.
   *
   * @param ajaxViewName the new ajax view name
   */
  @Value("errors/403_ajax")
  public void setAjaxViewName(String ajaxViewName) {
    this.ajaxViewName = ajaxViewName;
  }

  /**
   * Gets the ajax extension.
   *
   * @return the ajax extension
   */
  public String getAjaxExtension() {
    return ajaxExtension;
  }

  /**
   * Sets the ajax extension.
   *
   * @param ajaxExtension the new ajax extension
   */
  @Value(".ajax")
  public void setAjaxExtension(String ajaxExtension) {
    this.ajaxExtension = ajaxExtension;
  }

  @RequestMapping(path = "/403.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String originalUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");
    if (originalUri != null && originalUri.endsWith(ajaxExtension)) {
      return new ModelAndView(ajaxViewName);
    }
    return new ModelAndView(viewName);
  }

}
