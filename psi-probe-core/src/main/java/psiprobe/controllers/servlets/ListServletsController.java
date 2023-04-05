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
package psiprobe.controllers.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractContextHandlerController;
import psiprobe.model.ServletInfo;
import psiprobe.tools.ApplicationUtils;

/**
 * Retrieves a list of servlets for a particular web application or for all applications if an
 * application name is not passed in a query string.
 */
@Controller
public class ListServletsController extends AbstractContextHandlerController {

  @RequestMapping(path = "/servlets.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    List<Context> ctxs;
    if (context == null) {
      ctxs = getContainerWrapper().getTomcatContainer().findContexts();
    } else {
      ctxs = new ArrayList<>();
      ctxs.add(context);
    }

    List<ServletInfo> servlets = new ArrayList<>();
    for (Context ctx : ctxs) {
      if (ctx != null) {
        List<ServletInfo> appServlets = ApplicationUtils.getApplicationServlets(ctx);
        for (ServletInfo svlt : appServlets) {
          Collections.sort(svlt.getMappings());
        }
        servlets.addAll(appServlets);
      }
    }

    return new ModelAndView(getViewName(), "servlets", servlets);
  }

  @Override
  protected boolean isContextOptional() {
    return true;
  }

  @Value("ajax/servlets")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
