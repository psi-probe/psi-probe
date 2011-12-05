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

import com.googlecode.psiprobe.controllers.TomcatContainerController;
import com.googlecode.psiprobe.tools.ApplicationUtils;
import com.googlecode.psiprobe.tools.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Creates the list of web application installed in the same "host" as the
 * Probe.
 *
 * @author Vlad Ilyushchenko
 * @author Andy Shapoval
 */
public class ListWebappsController extends TomcatContainerController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        boolean calcSize = ServletRequestUtils.getBooleanParameter(request, "size", false)
                && SecurityUtils.hasAttributeValueRole(getServletContext(), request);

        List apps;
        try {
            apps = getContainerWrapper().getTomcatContainer().findContexts();
        } catch (NullPointerException ex) {
            throw new IllegalStateException("No container found for your server: " + getServletContext().getServerInfo(), ex);
        }
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
