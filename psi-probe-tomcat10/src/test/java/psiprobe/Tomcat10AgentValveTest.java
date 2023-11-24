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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The Class Tomcat10AgentValveTest.
 */
@ExtendWith(MockitoExtension.class)
class Tomcat10AgentValveTest {

  /** The valve. */
  Tomcat10AgentValve valve;

  /** The request. */
  @Mock
  Request request;

  /** The response. */
  @Mock
  Response response;

  /** The http session. */
  @Mock
  HttpSession session;

  /** The servlet request. */
  @Mock
  HttpServletRequest servletRequest;

  /** The valve mock. */
  @Mock
  Valve valveMock;

  /**
   * Invoke.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ServletException the servlet exception
   */
  @Test
  void invoke() throws IOException, ServletException {
    Mockito.when(request.getSession(Mockito.anyBoolean())).thenReturn(session);
    Mockito.when(request.getRequest()).thenReturn(servletRequest);

    valve = new Tomcat10AgentValve();
    valve.setNext(valveMock);
    valve.invoke(request, response);
    Mockito.verify(session, Mockito.times(2)).setAttribute(Mockito.anyString(), Mockito.any());
  }

}
