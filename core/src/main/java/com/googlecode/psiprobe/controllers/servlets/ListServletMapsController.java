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

package com.googlecode.psiprobe.controllers.servlets;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import com.googlecode.psiprobe.model.ServletMapping;
import com.googlecode.psiprobe.tools.ApplicationUtils;

import org.apache.catalina.Context;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Retrieves a list of servlet mappings for a particular web application or all web applications if
 * an application name is not passed in a query string.
 * 
 * @author Andy Shapoval
 */
public class ListServletMapsController extends ContextHandlerController {

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    List<Context> ctxs;
    if (context == null) {
      ctxs = getContainerWrapper().getTomcatContainer().findContexts();
    } else {
      ctxs = new ArrayList<Context>();
      ctxs.add(context);
    }

    List<ServletMapping> servletMaps = new ArrayList<ServletMapping>();
    for (Context ctx : ctxs) {
      servletMaps.addAll(ApplicationUtils.getApplicationServletMaps(ctx));
    }

    return new ModelAndView(getViewName(), "servletMaps", servletMaps);
  }

  @Override
  protected boolean isContextOptional() {
    return true;
  }

}
