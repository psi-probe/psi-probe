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
import org.jstripe.tomcat.probe.model.ApplicationResource;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Creates a list of all configured datasources for all web applications within the container.
 *
 * Author: Vlad Ilyushchenko
 */
public class ListAllJdbcResourcesController extends TomcatContainerController{

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        if (getContainerWrapper().getResourceResolver().supportsPrivateResources()) {
            List apps = getContainerWrapper().getTomcatContainer().findContexts();

            List resources = new ArrayList();
            for (int i = 0; i < apps.size(); i++) {

                List appResources = getContainerWrapper().getResourceResolver().getApplicationResources((Context) apps.get(i));
                //
                // add only those resources that have data source info
                //
                for (Iterator it = appResources.iterator(); it.hasNext(); ) {
                    ApplicationResource res = (ApplicationResource) it.next();
                    if (res.getDataSourceInfo() != null) {
                        resources.add(res);
                    }
                }
            }

            return new ModelAndView(getViewName(), "resources", resources);
        } else {
            request.setAttribute("global_resources", Boolean.TRUE);
            return new ModelAndView(getViewName(), "resources", getContainerWrapper().getResourceResolver().getApplicationResources());
        }
    }
}
