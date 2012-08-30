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
package com.googlecode.psiprobe.controllers.datasources;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Resets datasource if the datasource supports it.
 * 
 * @author Vlad Ilyushchenko
 */
public class ResetDataSourceController extends ContextHandlerController {

    private String replacePattern;

    public String getReplacePattern() {
        return replacePattern;
    }

    public void setReplacePattern(String replacePattern) {
        this.replacePattern = replacePattern;
    }

    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        String resourceName = ServletRequestUtils.getStringParameter(request, "resource", null);
        String referer = request.getHeader("Referer");
        String redirectURL;
        if (referer != null) {
            redirectURL = referer.replaceAll(replacePattern, "");
        } else {
            redirectURL = request.getContextPath() + getViewName();
        }

        if (resourceName != null && resourceName.length() > 0) {
            boolean reset = false;
            try {
                reset = getContainerWrapper().getResourceResolver().resetResource(context, resourceName);
            } catch (NamingException e) {
                request.setAttribute("errorMessage",
                        getMessageSourceAccessor().getMessage("probe.src.reset.datasource.notfound", new Object[]{resourceName}));
            }
            if (!reset) {
                request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage("probe.src.reset.datasource.c3p0"));
            }

        }
        logger.debug("Redirected to " + redirectURL);
        return new ModelAndView(new RedirectView(redirectURL));
    }

    protected boolean isContextOptional() {
        return ! getContainerWrapper().getResourceResolver().supportsPrivateResources();
    }
}
