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

import com.googlecode.psiprobe.tools.ApplicationUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.util.ServerInfo;
import org.springframework.web.servlet.ModelAndView;

/**
 * Retrieves a list of web application filter mappings.
 * 
 * @author Andy Shapoval
 */
public class ListAppFilterMapsController extends ContextHandlerController {
    protected ModelAndView handleContext(String contextName, Context context,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Tomcat's class representing filter mapping was changed starting with Tomcat 6,
        // therefore ApplicationUtils.getApplicationFilterMaps() throws an exception on Tomcat 6
        // TODO Add support of Tomcat 6
        String serverVersion = ServerInfo.getServerInfo();
        if (serverVersion.startsWith("Apache Tomcat/5.")) {
            List filterMaps = ApplicationUtils.getApplicationFilterMaps(context);
            return new ModelAndView(getViewName(), "filterMaps", filterMaps);
        } else {
            return new ModelAndView("errors/serverversion", "serverVersion", serverVersion);
        }
    }
}
