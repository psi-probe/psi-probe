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
package com.googlecode.psiprobe.controllers.servlets;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import com.googlecode.psiprobe.model.ServletInfo;
import com.googlecode.psiprobe.tools.ApplicationUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.springframework.web.servlet.ModelAndView;

/**
 * Retrieves a list of servlets for a particular web application or for all
 * applications if an application name is not passed in a query string.
 * 
 * @author Andy Shapoval
 */
public class ListServletsController extends ContextHandlerController {
    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        List ctxs;
        if (context == null) {
            ctxs = getContainerWrapper().getTomcatContainer().findContexts();
        } else {
            ctxs = new ArrayList();
            ctxs.add(context);
        }

        List servlets = new ArrayList();
        for (Iterator i = ctxs.iterator(); i.hasNext();) {
            Context ctx = (Context) i.next();
            if (ctx != null) {
                List appServlets = ApplicationUtils.getApplicationServlets(ctx);
                for (Iterator j = appServlets.iterator(); j.hasNext();) {
                    ServletInfo svlt = (ServletInfo) j.next();
                    Collections.sort(svlt.getMappings());
                }
                servlets.addAll(appServlets);
            }
        }

        return new ModelAndView(getViewName(), "servlets", servlets);
    }

    protected boolean isContextOptional() {
        return true;
    }
}
