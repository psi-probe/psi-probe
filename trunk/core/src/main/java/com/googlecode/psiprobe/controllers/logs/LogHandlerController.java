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
package com.googlecode.psiprobe.controllers.logs;

import com.googlecode.psiprobe.beans.LogResolverBean;
import com.googlecode.psiprobe.tools.logging.LogDestination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class LogHandlerController extends ParameterizableViewController {

    private LogResolverBean logResolver;

    public LogResolverBean getLogResolver() {
        return logResolver;
    }

    public void setLogResolver(LogResolverBean logResolver) {
        this.logResolver = logResolver;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        boolean logFound = false;

        ModelAndView modelAndView = null;

        String logType = ServletRequestUtils.getStringParameter(request, "logType");
        String webapp = ServletRequestUtils.getStringParameter(request, "webapp");
        boolean context = ServletRequestUtils.getBooleanParameter(request, "context", false);
        boolean root = ServletRequestUtils.getBooleanParameter(request, "root", false);
        String logName = ServletRequestUtils.getStringParameter(request, "logName");
        String logIndex = ServletRequestUtils.getStringParameter(request, "logIndex");

        LogDestination dest = logResolver.getLogDestination(logType, webapp, context, root, logName, logIndex);

        if (dest != null) {
            if (dest.getFile() != null && dest.getFile().exists()) {
                modelAndView = handleLogFile(request, response, dest);
                logFound = true;
            } else {
                logger.error(dest.getFile() + ": file not found");
            }
        } else {
            logger.error(logType + (root ? " root" : "") + " log" + (root ? "" : " \"" + logName + "\"") + " not found");
        }
        if (!logFound) {
            response.sendError(404);
        }
        return modelAndView;
    }

    protected ModelAndView handleLogFile(HttpServletRequest request, HttpServletResponse response, LogDestination logDest)
            throws Exception {

        return new ModelAndView(getViewName())
                .addObject("log", logDest);
    }
}
