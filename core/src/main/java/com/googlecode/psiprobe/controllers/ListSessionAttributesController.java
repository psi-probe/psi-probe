/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.controllers;

import org.apache.catalina.Context;
import com.googlecode.psiprobe.model.ApplicationSession;
import com.googlecode.psiprobe.tools.ApplicationUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Retrieves the list of attributes for given session.
 *
 * Author: Vlad Ilyushchenko
 */
public class ListSessionAttributesController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName, Context context, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String privelegedRole = getServletContext().getInitParameter("attribute.value.role");
        boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false) && request.isUserInRole(privelegedRole);
        String sid = ServletRequestUtils.getStringParameter(request, "sid");

        ApplicationSession appSession = ApplicationUtils.getApplicationSession(
                context.getManager().findSession(sid), calcSize, true);

        if (appSession != null) {
            appSession.setAllowedToViewValues(request.isUserInRole(privelegedRole));
            return new ModelAndView(getViewName(), "session", appSession);
        } else {
            return new ModelAndView(getViewName());
        }
    }
}
