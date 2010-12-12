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
package com.googlecode.psiprobe;

import com.googlecode.psiprobe.model.ApplicationSession;
import com.googlecode.psiprobe.model.IPInfo;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.catalina.Valve;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.ValveContext;

public class Tomcat50AgentValve implements Valve {

    public String getInfo() {
        return "PSI Probe Agent Valve";
    }

    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        valveContext.invokeNext(request, response);
        ServletRequest servletRequest = request.getRequest();
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest hsr = (HttpServletRequest) request;
            HttpSession session = hsr.getSession(false);
            if (session != null) {
                String ip = IPInfo.getClientAddress(hsr);
                session.setAttribute(ApplicationSession.LAST_ACCESSED_BY_IP, ip);
            }
        }
    }
}
