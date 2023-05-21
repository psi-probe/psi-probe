/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe;

import jakarta.servlet.ServletException;

import java.io.IOException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import psiprobe.model.ApplicationSession;
import psiprobe.model.IpInfo;

/**
 * Valve which inserts the client's IP address into the session for Tomcat 10.0.
 */
public class Tomcat10AgentValve extends ValveBase {

  /**
   * Instantiates a new tomcat10 agent valve.
   */
  public Tomcat10AgentValve() {
    super(true);
  }

  @Override
  public void invoke(Request request, Response response) throws IOException, ServletException {
    getNext().invoke(request, response);

    if (request.getSession(false) != null) {
      String ip = IpInfo.getClientAddress(request.getRequest());
      // Explicit calls to ensure result not lost
      request.getSession(false).setAttribute(ApplicationSession.LAST_ACCESSED_BY_IP, ip);
      request.getSession(false).setAttribute(ApplicationSession.LAST_ACCESSED_LOCALE,
          request.getLocale());
    }
  }

}
