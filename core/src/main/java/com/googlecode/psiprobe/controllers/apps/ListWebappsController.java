/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.controllers.apps;

import com.googlecode.psiprobe.controllers.TomcatContainerController;
import com.googlecode.psiprobe.model.Application;
import com.googlecode.psiprobe.tools.ApplicationUtils;
import com.googlecode.psiprobe.tools.SecurityUtils;

import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Creates the list of web application installed in the same "host" as the Probe.
 * 
 * @author Vlad Ilyushchenko
 * @author Andy Shapoval
 * @author Mark Lewis
 */
public class ListWebappsController extends TomcatContainerController {

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    boolean calcSize =
        ServletRequestUtils.getBooleanParameter(request, "size", false)
            && SecurityUtils.hasAttributeValueRole(getServletContext(), request);

    List<Context> apps;
    try {
      apps = getContainerWrapper().getTomcatContainer().findContexts();
    } catch (NullPointerException ex) {
      throw new IllegalStateException("No container found for your server: "
          + getServletContext().getServerInfo(), ex);
    }
    List<Application> applications = new ArrayList<Application>(apps.size());
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

}
