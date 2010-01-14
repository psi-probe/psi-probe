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
import com.googlecode.psiprobe.tools.ApplicationUtils;
import com.googlecode.psiprobe.tools.SecurityUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the list of web application installed in the same "host" as the Probe.
 *
 * Author: Vlad Ilyushchenko, Andy Shapoval
 */
public class ListWebappsController extends TomcatContainerController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false)
                && SecurityUtils.hasAttributeValueRole(getServletContext());

        List apps = getContainerWrapper().getTomcatContainer().findContexts();
        List applications = new ArrayList(apps.size());
        boolean showResources = getContainerWrapper().getResourceResolver().supportsPrivateResources();
        for (int i = 0; i < apps.size(); i++) {
            Context appContext = (Context) apps.get(i);
            //
            // check if this is not the ROOT webapp
            //
            if (appContext.getName() != null) {
                applications.add(ApplicationUtils.getApplication(appContext, getContainerWrapper().getResourceResolver(), calcSize));
            }
        }
        if (! applications.isEmpty() && ! showResources) {
            request.setAttribute("no_resources", Boolean.TRUE);
        }
        return new ModelAndView(getViewName(), "apps", applications);
    }
}
