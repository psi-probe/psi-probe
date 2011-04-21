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
package com.googlecode.psiprobe.controllers.jsp;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import com.googlecode.psiprobe.model.jsp.Summary;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class RecompileJspController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName, Context context, HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        Summary summary = session != null ? (Summary) session.getAttribute(DisplayJspController.SUMMARY_ATTRIBUTE) : null;

        if (request.getMethod().equalsIgnoreCase("post") && summary != null ) {
            List names = new ArrayList();
            for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
                String name = (String) e.nextElement();
                if ("on".equals(request.getParameter(name))) {
                    names.add(name);
                }
            }
            getContainerWrapper().getTomcatContainer().recompileJsps(context, summary, names);
            session.setAttribute(DisplayJspController.SUMMARY_ATTRIBUTE, summary);
        } else if ( summary != null && contextName.equals(summary.getName())) {
            String name = ServletRequestUtils.getStringParameter(request, "source", null);
            if (name != null) {
                List names = new ArrayList();
                names.add(name);
                getContainerWrapper().getTomcatContainer().recompileJsps(context, summary, names);
                session.setAttribute(DisplayJspController.SUMMARY_ATTRIBUTE, summary);
            } else {
                logger.error("source is not passed, nothing to do");
            }
        }
        return new ModelAndView(new RedirectView(request.getContextPath() + ServletRequestUtils.getStringParameter(request, "view", getViewName()) + "?" + request.getQueryString()));
    }
}
