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
package org.jstripe.tomcat.probe.controllers;

import org.apache.catalina.Context;
import org.jstripe.tomcat.probe.tools.ApplicationUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Retrieves a list of servlet context attributes for a web application.
 * <p/>
 * Author: Andy Shapoval
 */
public class ListAppAttributesController extends ContextHandlerController {
    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        List appAttrs = ApplicationUtils.getApplicationAttributes(context);
        ModelAndView mv = new ModelAndView(getViewName(), "appAttributes", appAttrs);
        String privelegedRole = getServletContext().getInitParameter("attribute.value.role");
        if (request.isUserInRole(privelegedRole)) {
            mv.addObject("displayValues", Boolean.TRUE);
        }
        return mv;
    }
}
