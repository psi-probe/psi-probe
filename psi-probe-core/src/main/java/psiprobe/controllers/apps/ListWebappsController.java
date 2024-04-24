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

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractTomcatContainerController;
import psiprobe.model.Application;
import psiprobe.tools.ApplicationUtils;
import psiprobe.tools.SecurityUtils;

/**
 * Creates the list of web application installed in the same "host" as the Probe.
 */
@Controller
public class ListWebappsController extends AbstractTomcatContainerController {

  @RequestMapping(path = "/index.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false)
        && SecurityUtils.hasAttributeValueRole(getServletContext());

    List<Context> apps;
    try {
      apps = getContainerWrapper().getTomcatContainer().findContexts();
    } catch (NullPointerException ex) {
      throw new IllegalStateException(
          "No container found for your server: " + getServletContext().getServerInfo(), ex);
    }
    List<Application> applications = new ArrayList<>(apps.size());
    boolean showResources = getContainerWrapper().getResourceResolver().supportsPrivateResources();
    for (Context appContext : apps) {
      // check if this is not the ROOT webapp
      if (appContext.getName() != null) {
        applications.add(ApplicationUtils.getApplication(appContext,
            getContainerWrapper().getResourceResolver(), calcSize, getContainerWrapper()));
      }
    }
    if (!applications.isEmpty() && !showResources) {
      request.setAttribute("no_resources", Boolean.TRUE);
    }
    return new ModelAndView(getViewName(), "apps", applications);
  }

  @Value("applications")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }
}
