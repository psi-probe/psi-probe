package org.jstripe.tomcat.probe;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.jstripe.tomcat.probe.model.ApplicationSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Tomcat55AgentValve extends ValveBase {

    public String getInfo() {
        return info;
    }

    public void invoke(Request request, Response response) throws IOException, ServletException {
        getNext().invoke(request, response);

        HttpServletRequest servletRequest = request.getRequest();
        HttpSession session = servletRequest.getSession(false);
        if (session != null) {
            session.setAttribute(ApplicationSession.LAST_ACCESSED_BY_IP, servletRequest.getRemoteAddr());
        }
    }
}
