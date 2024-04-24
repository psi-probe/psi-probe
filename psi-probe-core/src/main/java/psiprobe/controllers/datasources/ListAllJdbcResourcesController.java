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
package psiprobe.controllers.datasources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractTomcatContainerController;
import psiprobe.model.ApplicationResource;

/**
 * Creates a list of all configured datasources for all web applications within the container.
 */
@Controller
public class ListAllJdbcResourcesController extends AbstractTomcatContainerController {

  @RequestMapping(path = "/datasources.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse httpServletResponse) throws Exception {

    boolean supportsGlobal = getContainerWrapper().getResourceResolver().supportsGlobalResources();
    boolean supportsPrivate =
        getContainerWrapper().getResourceResolver().supportsPrivateResources();
    boolean supportsDataSourceLookup =
        getContainerWrapper().getResourceResolver().supportsDataSourceLookup();
    List<ApplicationResource> privateResources = getContainerWrapper().getPrivateDataSources();
    List<ApplicationResource> globalResources = getContainerWrapper().getGlobalDataSources();
    return new ModelAndView(getViewName()).addObject("supportsGlobal", supportsGlobal)
        .addObject("supportsPrivate", supportsPrivate)
        .addObject("supportsDSLookup", supportsDataSourceLookup)
        .addObject("privateResources", privateResources)
        .addObject("globalResources", globalResources);
  }

  @Value("datasources")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
