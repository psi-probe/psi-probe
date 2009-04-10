/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.controllers;

import org.springframework.web.servlet.ModelAndView;
import org.jstripe.tomcat.probe.model.Application;
import org.jstripe.tomcat.probe.tools.ApplicationUtils;
import org.apache.catalina.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Populates Application model object with servlet statistics
 * <p/>
 * Author: Andy Shapoval
 */
public class GetAppStatsController extends ContextHandlerController {
    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        Application app = new Application();
        ApplicationUtils.collectApplicationServletStats(context, app);
        return new ModelAndView(getViewName(), "app", app);
    }
}
