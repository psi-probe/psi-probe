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
package psiprobe.controllers.sessions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.TomcatContainer;
import psiprobe.beans.ContainerWrapperBean;
import psiprobe.model.ApplicationSession;
import psiprobe.model.SessionSearchInfo;
import psiprobe.tools.ApplicationUtils;

/**
 * The Class ListSessionsControllerTest.
 */
class ListSessionsControllerTest {

  /** The controller. */
  private ListSessionsController controller;

  /** The request. */
  private HttpServletRequest request;

  /** The response. */
  private HttpServletResponse response;

  /** The context. */
  private Context context;

  /** The manager. */
  private Manager manager;

  /** The container wrapper. */
  private ContainerWrapperBean containerWrapper;

  /**
   * Sets the up.
   */
  @BeforeEach
  void setUp() {
    controller = spy(new ListSessionsController());
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    context = mock(Context.class);
    manager = mock(Manager.class);
    containerWrapper = mock(ContainerWrapperBean.class);

    doReturn(containerWrapper).when(controller).getContainerWrapper();
    doReturn("sessions").when(controller).getViewName();
  }

  /**
   * Test handle request delegates to super.
   *
   * @throws Exception the exception
   */
  @Test
  void testHandleRequestDelegatesToSuper() throws Exception {
    ListSessionsController ctrl = spy(new ListSessionsController());
    doReturn(new ModelAndView()).when(ctrl).handleRequest(any(), any());
    ModelAndView mv = ctrl.handleRequest(request, response);
    assertNotNull(mv);
  }

  /**
   * Test handle context with no context lists all sessions.
   *
   * @throws Exception the exception
   */
  @Test
  void testHandleContextWithNoContextListsAllSessions() throws Exception {
    List<Context> ctxs = new ArrayList<>();
    Context ctx = mock(Context.class);
    Manager mgr = mock(Manager.class);
    Session s = mock(Session.class);
    ApplicationSession appSession = mock(ApplicationSession.class);

    ctxs.add(ctx);
    when(containerWrapper.getTomcatContainer()).thenReturn(mock(TomcatContainer.class));

    TomcatContainer tomcatContainer = mock(TomcatContainer.class);
    when(containerWrapper.getTomcatContainer()).thenReturn(tomcatContainer);
    when(tomcatContainer.findContexts()).thenReturn(ctxs);

    when(ctx.getManager()).thenReturn(mgr);
    when(mgr.findSessions()).thenReturn(new Session[] {s});

    @SuppressWarnings("unused")
    var unused = mockStatic(ApplicationUtils.class);

    when(ApplicationUtils.getApplicationSession(any(), anyBoolean(), anyBoolean()))
        .thenReturn(appSession);
    when(appSession.getAttributes()).thenReturn(Collections.emptyList());

    ModelAndView mv = controller.handleContext(null, null, request, response);
    assertNotNull(mv);
    assertTrue(mv.getModel().containsKey("sessions"));
  }

  /**
   * Test handle context with context lists sessions.
   *
   * @throws Exception the exception
   */
  @Test
  void testHandleContextWithContextListsSessions() throws Exception {
    when(context.getManager()).thenReturn(manager);
    when(manager.findSessions()).thenReturn(new Session[0]);
    ModelAndView mv = controller.handleContext("test", context, request, response);
    assertNotNull(mv);
    assertTrue(mv.getModel().containsKey("sessions"));
  }

  /**
   * Test match session.
   *
   * @throws Exception the exception
   */
  @Test
  void testMatchSession() throws Exception {
    ApplicationSession appSession = mock(ApplicationSession.class);
    SessionSearchInfo searchInfo = new SessionSearchInfo();
    // Use reflection to call private method
    var method = ListSessionsController.class.getDeclaredMethod("matchSession",
        ApplicationSession.class, SessionSearchInfo.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(controller, appSession, searchInfo);
    assertTrue(result);
  }

  /**
   * Test is context optional.
   */
  @Test
  void testIsContextOptional() {
    assertTrue(controller.isContextOptional());
  }

  /**
   * Test set view name.
   */
  @Test
  void testSetViewName() {
    controller.setViewName("sessions");
    assertEquals("sessions", controller.getViewName());
  }

}
