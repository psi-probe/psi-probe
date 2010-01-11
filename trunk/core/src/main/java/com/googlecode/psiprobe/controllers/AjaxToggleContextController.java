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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Stops a web application.
 *
 * Author: Vlad Ilyushchenko
 */
public class AjaxToggleContextController extends ContextHandlerController {
    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!request.getContextPath().equals(contextName) && context != null) {
            try {
                if (context.getAvailable()) {
                    logger.info(request.getRemoteAddr() + " requested STOP of "+contextName);
                    getContainerWrapper().getTomcatContainer().stop(contextName);
                } else {
                    logger.info(request.getRemoteAddr() + " requested START of "+contextName);
                    getContainerWrapper().getTomcatContainer().start(contextName);
                }
            } catch (Throwable e) {
                logger.error(e);
            }
        }
        return new ModelAndView(getViewName(), "available",
                Boolean.valueOf(context == null ? false : context.getAvailable()));
    }
}
