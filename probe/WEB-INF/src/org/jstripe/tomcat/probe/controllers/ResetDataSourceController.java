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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Resets datasource if the datasource supports it.
 * <p/>
 * Author: Vlad Ilyushchenko
 */
public class ResetDataSourceController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        String resourceName = ServletRequestUtils.getStringParameter(request, "resource", null);
        String viewName = ServletRequestUtils.getStringParameter(request, "view", getViewName());

        if (resourceName != null && resourceName.length() > 0) {
            boolean reset = false;
            try {
                reset = getContainerWrapper().getResourceResolver().resetResource(context, resourceName);
            } catch (NamingException e) {
                request.setAttribute("errorMessage",
                        getMessageSourceAccessor().getMessage("probe.src.reset.datasource.notfound", new Object[]{resourceName}));
            }
            if (!reset)
                request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage("probe.src.reset.datasource.c3p0"));

        }
        return new ModelAndView(viewName);
    }

    protected boolean isContextOptional() {
        return ! getContainerWrapper().getResourceResolver().supportsPrivateResources();
    }
}
