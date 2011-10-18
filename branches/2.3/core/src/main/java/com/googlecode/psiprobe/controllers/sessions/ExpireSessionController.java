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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.bind.ServletRequestUtils;
import org.apache.catalina.Context;
import org.apache.catalina.Session;

/**
 * Expires a single session of a particular web application.
 *
 * @author Vlad Ilyushchenko
 * @author Andy Shapoval
 */
public class ExpireSessionController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sessionID= ServletRequestUtils.getStringParameter(request, "sid");
        Session session = context.getManager().findSession(sessionID);
        if (session != null) {
            session.expire();
        }
        return new ModelAndView(new InternalResourceView(getViewName()));
    }
}
