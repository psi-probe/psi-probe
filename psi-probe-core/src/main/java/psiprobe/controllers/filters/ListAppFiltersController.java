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
package psiprobe.controllers.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractContextHandlerController;
import psiprobe.model.FilterInfo;
import psiprobe.tools.ApplicationUtils;

/**
 * Retrieves a list of filter mappings or filter definitions of a web application.
 */
@Controller
public class ListAppFiltersController extends AbstractContextHandlerController {

  @RequestMapping(path = "/appfilters.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    List<FilterInfo> appFilters =
        ApplicationUtils.getApplicationFilters(context, getContainerWrapper());

    return new ModelAndView(getViewName(), "appFilters", appFilters);
  }

  @Value("appfilters")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
