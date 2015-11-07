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

package com.googlecode.psiprobe.controllers.sessions;

import com.googlecode.psiprobe.controllers.TomcatContainerController;

import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Expires a list of sessionIDs. Accepts a list of sid_webapp parameters that are expected to be in
 * a form of "sid;webapp"
 * 
 * @author Vlad Ilyushchenko
 * @author Andy Shapoval
 * @author Mark Lewis
 */
public class ExpireSessionsController extends TomcatContainerController {

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String[] sidWebApps = ServletRequestUtils.getStringParameters(request, "sid_webapp");
    for (String sidWebApp : sidWebApps) {
      if (sidWebApp != null) {
        String[] ss = sidWebApp.split(";");
        if (ss.length == 2) {
          String sessionId = ss[0];
          String appName = ss[1];
          Context context = getContainerWrapper().getTomcatContainer().findContext(appName);
          if (context != null) {
            Manager manager = context.getManager();
            Session session = manager.findSession(sessionId);
            if (session != null && session.isValid()) {
              session.expire();
            }
          } else {
            return new ModelAndView("errors/paramerror");
          }
        } else {
          return new ModelAndView("errors/paramerror");
        }
      }
    }
    return new ModelAndView(new InternalResourceView(getViewName()));
  }

}
