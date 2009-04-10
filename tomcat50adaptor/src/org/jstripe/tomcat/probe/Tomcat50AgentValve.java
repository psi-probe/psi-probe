package org.jstripe.tomcat.probe;

import org.apache.catalina.Valve;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.ValveContext;
import org.jstripe.tomcat.probe.model.ApplicationSession;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Tomcat50AgentValve implements Valve {

    public String getInfo() {
        return "LambdaProbe Agent Valve";
    }

    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        valveContext.invokeNext(request, response);
        ServletRequest servletRequest = request.getRequest();
        if (servletRequest instanceof HttpServletRequest) {
            HttpSession session = ((HttpServletRequest)request).getSession(false);
            if (session != null) {
                session.setAttribute(ApplicationSession.LAST_ACCESSED_BY_IP, servletRequest.getRemoteAddr());
            }
        }
    }
}
