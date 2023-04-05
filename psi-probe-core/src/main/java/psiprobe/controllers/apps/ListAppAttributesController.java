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
package psiprobe.controllers.apps;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractContextHandlerController;
import psiprobe.model.Attribute;
import psiprobe.tools.ApplicationUtils;
import psiprobe.tools.SecurityUtils;

/**
 * Retrieves a list of servlet context attributes for a web application.
 */
@Controller
public class ListAppAttributesController extends AbstractContextHandlerController {

  @RequestMapping(path = "/appattributes.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    List<Attribute> appAttrs = ApplicationUtils.getApplicationAttributes(context);
    ModelAndView mv = new ModelAndView(getViewName(), "appAttributes", appAttrs);

    if (SecurityUtils.hasAttributeValueRole(getServletContext())) {
      mv.addObject("displayValues", Boolean.TRUE);
    }
    return mv;
  }

  @Value("appattributes")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
