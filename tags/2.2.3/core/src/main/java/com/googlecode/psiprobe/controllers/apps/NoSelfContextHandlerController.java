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
package com.googlecode.psiprobe.controllers.apps;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Base class preventing "destructive" actions to be executed on the Probe's
 * context.
 *
 * @author Vlad Ilyushchenko
 */
public abstract class NoSelfContextHandlerController extends ContextHandlerController {
    private boolean passQueryString = false;

    public boolean isPassQueryString() {
        return passQueryString;
    }

    public void setPassQueryString(boolean passQueryString) {
        this.passQueryString = passQueryString;
    }

    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            if (request.getContextPath().equals(contextName)) {
                throw new IllegalStateException(getMessageSourceAccessor().getMessage("probe.src.contextAction.cannotActOnSelf"));
            }

            executeAction(contextName);
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            logger.error(e);
            return new ModelAndView(new InternalResourceView(getViewName()));
        }
        return new ModelAndView(new RedirectView(request.getContextPath() + getViewName() + (isPassQueryString() ? "?" + request.getQueryString() : "")));
    }


    protected abstract void executeAction(String contextName) throws Exception;
}
