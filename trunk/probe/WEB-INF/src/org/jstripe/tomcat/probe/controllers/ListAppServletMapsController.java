/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 *  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 *  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.tomcat.probe.controllers;

import org.apache.catalina.Context;
import org.jstripe.tomcat.probe.tools.ApplicationUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Retrieves a list of web application servlet mappings
 * <p/>
 * Author: Andy Shapoval
 */
public class ListAppServletMapsController extends ContextHandlerController {
    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        List ctxs;
        if (context == null) {
            ctxs = getContainerWrapper().getTomcatContainer().findContexts();
        } else {
            ctxs = new ArrayList();
            ctxs.add(context);
        }

        List servletMaps = new ArrayList();
        for (Iterator i = ctxs.iterator(); i.hasNext();) {
            Context ctx = (Context) i.next();
            servletMaps.addAll(ApplicationUtils.getApplicationServletMaps(ctx));
        }

        return new ModelAndView(getViewName(), "servletMaps", servletMaps);
    }

    protected boolean isContextOptional() {
        return true;
    }
}
