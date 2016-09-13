/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import psiprobe.model.ApplicationSession;
import psiprobe.model.IpInfo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Valve which inserts the client's IP address into the session for Tomcat 7.0.
 */
public class Tomcat70AgentValve extends ValveBase {

  /**
   * Instantiates a new tomcat70 agent valve.
   */
  public Tomcat70AgentValve() {
    super(true);
  }

  /**
   * Gets the info.
   * 
   * @return the info
   */
  @Override
  public String getInfo() {
    return info;
  }

  /**
   * Invoke.
   * 
   * @param request the request
   * @param response the response
   * @throws java.io.IOException if the next valve throws this exception
   * @throws javax.servlet.ServletException if the next valve throws this exception
   */
  @Override
  public void invoke(Request request, Response response) throws IOException, ServletException {
    getNext().invoke(request, response);

    HttpServletRequest servletRequest = request.getRequest();
    HttpSession session = servletRequest.getSession(false);
    if (session != null) {
      String ip = IpInfo.getClientAddress(servletRequest);
      session.setAttribute(ApplicationSession.LAST_ACCESSED_BY_IP, ip);
    }
  }

}
