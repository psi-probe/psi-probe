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

package psiprobe.controllers.deploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.TomcatContainerController;

/**
 * Precharges the list of contexts in the deploy page.
 * 
 * @author Sandra Pascual
 */
public class DeployController extends TomcatContainerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(DeployController.class);

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    List<Context> apps;
    try {
      apps = getContainerWrapper().getTomcatContainer().findContexts();
    } catch (NullPointerException ex) {
      throw new IllegalStateException("No container found for your server: "
          + getServletContext().getServerInfo(), ex);
    }

    List applications = new ArrayList();
    for (Context appContext : apps) {
      // check if this is not the ROOT webapp
      if (appContext.getName() != null && appContext.getName().trim().length() > 0) {
        Map<String, String> app = new HashMap<String, String>();
        app.put("value", appContext.getName());
        app.put("label", appContext.getName());
        applications.add(app);
      }
    }
    request.setAttribute("apps", applications);
    return new ModelAndView(getViewName());
  }
}
