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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class DisplayJspController extends ContextHandlerController {

    public static final String SUMMARY_ATTRIBUTE = "jsp.summary";

    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean compile = ServletRequestUtils.getBooleanParameter(request, "compile", false);

        HttpSession session = request.getSession(true);
        Summary summary = (Summary) session.getAttribute(SUMMARY_ATTRIBUTE);
        if (summary == null || ! contextName.equals(summary.getName())) {
            summary = new Summary();
            summary.setName(contextName);
        }
        getContainerWrapper().getTomcatContainer().listContextJsps(context, summary, compile);

        session.setAttribute(SUMMARY_ATTRIBUTE, summary);

        if (compile) {
            return new ModelAndView(new RedirectView(request.getRequestURI()+"?webapp="+(contextName.length() == 0 ? "/" : contextName)));
        } else {
            return new ModelAndView(getViewName(), "summary", summary);
        }
    }
}
