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
package com.googlecode.psiprobe.controllers.sessions;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.Session;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class RemoveSessAttributeController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName,
                                         Context context,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {

        String sid = ServletRequestUtils.getStringParameter(request, "sid");
        String attrName = ServletRequestUtils.getStringParameter(request, "attr");
        Session session = context.getManager().findSession(sid);
        if (session != null) {
            session.getSession().removeAttribute(attrName);
        }

        return new ModelAndView(new RedirectView(request.getContextPath() + getViewName() + "?"+request.getQueryString()));
    }
}
