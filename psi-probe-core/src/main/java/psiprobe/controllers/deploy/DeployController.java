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
package psiprobe.controllers.deploy;

import com.google.common.base.Strings;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractTomcatContainerController;

/**
 * Precharges the list of contexts in the deploy page.
 */
@Controller
public class DeployController extends AbstractTomcatContainerController {

  @RequestMapping(path = "/adm/deploy.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    List<Context> apps;
    try {
      apps = getContainerWrapper().getTomcatContainer().findContexts();
    } catch (NullPointerException ex) {
      throw new IllegalStateException(
          "No container found for your server: " + getServletContext().getServerInfo(), ex);
    }

    List<Map<String, String>> applications = new ArrayList<>();
    for (Context appContext : apps) {
      // check if this is not the ROOT webapp
      if (!Strings.isNullOrEmpty(appContext.getName())) {
        Map<String, String> app = new HashMap<>();
        app.put("value", appContext.getName());
        app.put("label", appContext.getName());
        applications.add(app);
      }
    }
    request.setAttribute("apps", applications);
    return new ModelAndView(getViewName());
  }

  @Value("deploy")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
