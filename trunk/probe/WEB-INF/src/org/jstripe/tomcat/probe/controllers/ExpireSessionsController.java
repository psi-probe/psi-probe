/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.controllers;

import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Expires a list of sessionIDs.
 *
 * Author: Vlad Ilyushchenko
 */
public class ExpireSessionsController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equalsIgnoreCase("post")) {
            Manager manager = context.getManager();
            for (Enumeration enumeration = request.getParameterNames(); enumeration.hasMoreElements();) {
                String sessionID = (String) enumeration.nextElement();
                if ("on".equals(request.getParameter(sessionID))) {
                    Session session = manager.findSession(sessionID);
                    if (session != null && session.isValid()) {
                        session.expire();
                    }
                }
            }
        } else {
            String sessionID= ServletRequestUtils.getStringParameter(request, "sid");
            Session session = context.getManager().findSession(sessionID);
            if (session != null) session.expire();
        }
        return new ModelAndView(new InternalResourceView(getViewName()));
    }
}
