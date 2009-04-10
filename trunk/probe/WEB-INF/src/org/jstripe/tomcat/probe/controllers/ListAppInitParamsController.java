/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 *  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 *  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.controllers;

import org.apache.catalina.Context;
import org.jstripe.tomcat.probe.tools.ApplicationUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Retrieves a list of context initialization parameters for a web application.
 * <p/>
 * Author: Andy Shapoval
 */
public class ListAppInitParamsController  extends ContextHandlerController {
    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        String privelegedRole = getServletContext().getInitParameter("attribute.value.role");

        ModelAndView mv = new ModelAndView(getViewName(), "appInitParams", ApplicationUtils.getApplicationInitParams(context));
        if (request.isUserInRole(privelegedRole)) {
            mv.addObject("allowedToViewValues", Boolean.TRUE);
        }

        return mv;
    }
}
