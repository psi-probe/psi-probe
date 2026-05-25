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
package psiprobe.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.naming.NamingException;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardWrapper;
import org.junit.jupiter.api.Test;

import psiprobe.beans.ContainerWrapperBean;
import psiprobe.beans.ResourceResolver;
import psiprobe.model.Application;
import psiprobe.model.ApplicationResource;
import psiprobe.model.ApplicationSession;
import psiprobe.model.DataSourceInfo;
import psiprobe.model.ServletInfo;
import psiprobe.model.ServletMapping;

/**
 * Tests for {@link ApplicationUtils}.
 */
class ApplicationUtilsTest {

  @Test
  void getApplicationSessionReturnsNullForInvalidSession() {
    Session session = mock(Session.class);
    when(session.isValid()).thenReturn(false);

    assertNull(ApplicationUtils.getApplicationSession(session, false, false));
  }

  @Test
  void getApplicationSessionCollectsAttributesAndMetadata() {
    Session session = mock(Session.class);
    Manager manager = mock(Manager.class);
    HttpSession httpSession = mock(HttpSession.class);

    when(session.isValid()).thenReturn(true);
    when(session.getId()).thenReturn("s-1");
    when(session.getCreationTime()).thenReturn(1_000L);
    when(session.getLastAccessedTime()).thenReturn(2_000L);
    when(session.getMaxInactiveInterval()).thenReturn(60);
    when(session.getManager()).thenReturn(manager);
    when(session.getSession()).thenReturn(httpSession);

    when(httpSession.getAttributeNames()).thenReturn(Collections.enumeration(List.of("name",
        ApplicationSession.LAST_ACCESSED_BY_IP, ApplicationSession.LAST_ACCESSED_LOCALE)));
    when(httpSession.getAttribute("name")).thenReturn("value");
    when(httpSession.getAttribute(ApplicationSession.LAST_ACCESSED_BY_IP)).thenReturn("127.0.0.1");
    when(httpSession.getAttribute(ApplicationSession.LAST_ACCESSED_LOCALE)).thenReturn(Locale.US);

    ApplicationSession result = ApplicationUtils.getApplicationSession(session, false, true);

    assertNotNull(result);
    assertEquals("s-1", result.getId());
    assertEquals(3, result.getObjectCount());
    assertTrue(result.isSerializable());
    assertEquals("127.0.0.1", result.getLastAccessedIp());
    assertEquals(Locale.US, result.getLastAccessedIpLocale());
    assertEquals(3, result.getAttributes().size());
  }

  @Test
  void getApplicationSessionHandlesInvalidatedHttpSessionGracefully() {
    Session session = mock(Session.class);
    Manager manager = mock(Manager.class);
    HttpSession httpSession = mock(HttpSession.class);

    when(session.isValid()).thenReturn(true);
    when(session.getId()).thenReturn("s-2");
    when(session.getCreationTime()).thenReturn(1_000L);
    when(session.getLastAccessedTime()).thenReturn(2_000L);
    when(session.getMaxInactiveInterval()).thenReturn(60);
    when(session.getManager()).thenReturn(manager);
    when(session.getSession()).thenReturn(httpSession);

    when(httpSession.getAttributeNames()).thenThrow(new IllegalStateException("invalidated"));

    ApplicationSession result = ApplicationUtils.getApplicationSession(session, false, true);

    assertNotNull(result);
    assertEquals(0, result.getObjectCount());
    assertEquals(0, result.getAttributes().size());
  }

  @Test
  void collectApplicationServletStatsAggregatesStandardWrappers() {
    Context context = mock(Context.class);
    StandardWrapper sw1 = mock(StandardWrapper.class);
    StandardWrapper sw2 = mock(StandardWrapper.class);
    Container other = mock(Container.class);

    when(context.findChildren()).thenReturn(new Container[] {sw1, other, sw2});

    when(sw1.getRequestCount()).thenReturn(5);
    when(sw1.getErrorCount()).thenReturn(1);
    when(sw1.getProcessingTime()).thenReturn(100L);
    when(sw1.getMinTime()).thenReturn(10L);
    when(sw1.getMaxTime()).thenReturn(50L);

    when(sw2.getRequestCount()).thenReturn(2);
    when(sw2.getErrorCountLong()).thenReturn(0L);
    when(sw2.getProcessingTime()).thenReturn(200L);
    when(sw2.getMinTime()).thenReturn(5L);
    when(sw2.getMaxTime()).thenReturn(80L);

    Application app = new Application();
    ApplicationUtils.collectApplicationServletStats(context, app);

    assertEquals(2, app.getServletCount());
    assertEquals(7L, app.getRequestCount());
    assertEquals(1L, app.getErrorCount());
    assertEquals(300L, app.getProcessingTime());
    assertEquals(5L, app.getMinTime());
    assertEquals(80L, app.getMaxTime());
  }

  @Test
  void getApplicationDataSourceUsageScoresUsesMaximumAcrossResources() throws NamingException {
    Context context = mock(Context.class);
    ResourceResolver resolver = mock(ResourceResolver.class);
    ContainerWrapperBean containerWrapper = mock(ContainerWrapperBean.class);

    ApplicationResource r1 = new ApplicationResource();
    DataSourceInfo ds1 = new DataSourceInfo();
    ds1.setMaxConnections(100);
    ds1.setBusyConnections(40);
    ds1.setEstablishedConnections(50);
    r1.setDataSourceInfo(ds1);

    ApplicationResource r2 = new ApplicationResource();
    DataSourceInfo ds2 = new DataSourceInfo();
    ds2.setMaxConnections(200);
    ds2.setBusyConnections(160);
    ds2.setEstablishedConnections(100);
    r2.setDataSourceInfo(ds2);

    ApplicationResource r3 = new ApplicationResource();

    when(resolver.getApplicationResources(context, containerWrapper))
        .thenReturn(List.of(r1, r2, r3));

    int[] scores =
        ApplicationUtils.getApplicationDataSourceUsageScores(context, resolver, containerWrapper);

    assertEquals(Math.max(ds1.getBusyScore(), ds2.getBusyScore()), scores[0]);
    assertEquals(Math.max(ds1.getEstablishedScore(), ds2.getEstablishedScore()), scores[1]);
  }

  @Test
  void getApplicationDataSourceUsageScoresWrapsNamingException() throws NamingException {
    Context context = mock(Context.class);
    ResourceResolver resolver = mock(ResourceResolver.class);
    ContainerWrapperBean containerWrapper = mock(ContainerWrapperBean.class);

    when(resolver.getApplicationResources(context, containerWrapper))
        .thenThrow(new NamingException("boom"));

    assertThrows(RuntimeException.class, () -> ApplicationUtils
        .getApplicationDataSourceUsageScores(context, resolver, containerWrapper));
  }

  @Test
  void getApplicationAttributesCollectsServletContextAttributes() {
    Context context = mock(Context.class);
    ServletContext servletContext = mock(ServletContext.class);

    when(context.getServletContext()).thenReturn(servletContext);
    when(servletContext.getAttributeNames())
        .thenReturn(Collections.enumeration(List.of("a1", "a2")));
    when(servletContext.getAttribute("a1")).thenReturn("v1");
    when(servletContext.getAttribute("a2")).thenReturn(42);

    assertEquals(2, ApplicationUtils.getApplicationAttributes(context).size());
  }

  @Test
  void getApplicationServletReturnsNullWhenChildIsNotWrapper() {
    Context context = mock(Context.class);
    Container container = mock(Container.class);

    when(context.findChild("x")).thenReturn(container);

    assertNull(ApplicationUtils.getApplicationServlet(context, "x"));
  }

  @Test
  void getApplicationServletAndServletsBuildServletInfoFromWrappers() {
    Context context = mock(Context.class);
    Wrapper wrapper = mock(Wrapper.class);
    Container other = mock(Container.class);

    when(context.getName()).thenReturn("/app");
    when(context.findChild("w")).thenReturn(wrapper);
    when(wrapper.getName()).thenReturn("w");
    when(wrapper.getServletClass()).thenReturn("com.example.Servlet");
    when(wrapper.isUnavailable()).thenReturn(false);
    when(wrapper.getLoadOnStartup()).thenReturn(1);
    when(wrapper.getRunAs()).thenReturn("runAs");
    when(wrapper.findMappings()).thenReturn(new String[] {"/w"});

    ServletInfo servletInfo = ApplicationUtils.getApplicationServlet(context, "w");

    assertNotNull(servletInfo);
    assertEquals("w", servletInfo.getServletName());

    when(context.findChildren()).thenReturn(new Container[] {wrapper, other});
    assertEquals(1, ApplicationUtils.getApplicationServlets(context).size());
  }

  @Test
  void getApplicationServletMapsSkipsNullMappingsAndAddsWrapperMetadata() {
    Context context = mock(Context.class);
    Wrapper wrapper = mock(Wrapper.class);

    when(context.getName()).thenReturn("/app");
    when(context.findServletMappings()).thenReturn(new String[] {"/x", null, "/y"});
    when(context.findServletMapping("/x")).thenReturn("s1");
    when(context.findServletMapping("/y")).thenReturn(null);
    when(context.findChild("s1")).thenReturn(wrapper);
    when(wrapper.getServletClass()).thenReturn("com.example.S1");
    when(wrapper.isUnavailable()).thenReturn(false);

    List<ServletMapping> mappings = ApplicationUtils.getApplicationServletMaps(context);

    assertEquals(1, mappings.size());
    assertEquals("/x", mappings.get(0).getUrl());
    assertEquals("s1", mappings.get(0).getServletName());
    assertEquals("com.example.S1", mappings.get(0).getServletClass());
  }
}
