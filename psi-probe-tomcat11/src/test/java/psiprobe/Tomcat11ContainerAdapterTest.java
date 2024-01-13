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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.Valve;
import org.apache.catalina.WebResource;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.jasper.JspCompilationContext;
import org.apache.tomcat.util.descriptor.web.ApplicationParameter;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.apache.tomcat.util.descriptor.web.ContextResourceLink;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import psiprobe.model.ApplicationResource;

/**
 * The Class Tomcat11ContainerAdapterTest.
 */
@ExtendWith(MockitoExtension.class)
class Tomcat11ContainerAdapterTest {

  /** The context. */
  @Mock
  Context context;

  /**
   * Creates the valve.
   */
  @Test
  void createValve() {
    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    Valve valve = adapter.createValve();
    assertEquals("Tomcat11AgentValve[Container is null]", valve.toString());
  }

  /**
   * Can bound to null.
   */
  @Test
  void canBoundToNull() {
    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertFalse(adapter.canBoundTo(null));
  }

  /**
   * Can bound to tomcat11.
   */
  @Test
  void canBoundToTomcat11() {
    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertTrue(adapter.canBoundTo("Apache Tomcat/11.0"));
  }

  /**
   * Can bound to tomee 11.0, nsjsp 11.0, vmware tc 11.0.
   */
  // TODO Not yet supported
  @Disabled
  @ParameterizedTest
  @ValueSource(strings = {"Apache Tomcat (TomEE)/11.0", "NonStop(tm) Servlets For JavaServer Pages(tm) v11.0", "Vmware tc..../11.0"})
  void canBoundTo(String container) {
    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertTrue(adapter.canBoundTo(container));
  }

  /**
   * Can bound to other.
   */
  @Test
  void canBoundToOther() {
    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertFalse(adapter.canBoundTo("Other"));
  }

  /**
   * Filter mappings.
   */
  @Test
  void filterMappings() {
    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    FilterMap map = new FilterMap();
    map.addServletName("psi-probe");
    map.addURLPattern("/psi-probe");
    assertEquals(2, adapter.getFilterMappings(map, "dispatcherMap", "filterClass").size());
  }

  /**
   * Creates the jsp compilation context.
   */
  @Test
  void createJspCompilationContext() {
    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    JspCompilationContext jspContext = adapter.createJspCompilationContext("name", null, null, null,
        ClassLoader.getSystemClassLoader());
    assertEquals("org.apache.jsp.name", jspContext.getFQCN());
  }

  /**
   * Adds the context resource link.
   */
  @Test
  void addContextResourceLink() {
    NamingResourcesImpl namingResources = Mockito.mock(NamingResourcesImpl.class);
    Mockito.when(context.getNamingResources()).thenReturn(namingResources);
    Mockito.when(namingResources.findResourceLinks())
        .thenReturn(new ContextResourceLink[]{new ContextResourceLink()});

    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    final List<ApplicationResource> list = new ArrayList<ApplicationResource>();
    adapter.addContextResourceLink(context, list);
    assertFalse(list.isEmpty());
  }

  /**
   * Adds the context resource.
   */
  @Test
  void addContextResource() {
    NamingResourcesImpl namingResources = Mockito.mock(NamingResourcesImpl.class);
    Mockito.when(context.getNamingResources()).thenReturn(namingResources);
    Mockito.when(namingResources.findResources())
        .thenReturn(new ContextResource[]{new ContextResource()});

    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    final List<ApplicationResource> list = new ArrayList<ApplicationResource>();
    adapter.addContextResource(context, list);
    assertFalse(list.isEmpty());
  }

  /**
   * Gets the application filter maps.
   */
  @Test
  void applicationFilterMaps() {
    Mockito.when(context.findFilterMaps()).thenReturn(new FilterMap[]{new FilterMap()});

    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertEquals(0, adapter.getApplicationFilterMaps(context).size());
  }

  /**
   * Application filters.
   */
  @Test
  void applicationFilters() {
    Mockito.when(context.findFilterDefs()).thenReturn(new FilterDef[]{new FilterDef()});

    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertEquals(1, adapter.getApplicationFilters(context).size());
  }

  /**
   * Application init params.
   */
  @Test
  void applicationInitParams() {
    Mockito.when(context.findApplicationParameters())
        .thenReturn(new ApplicationParameter[]{new ApplicationParameter()});

    ServletContext servletContext = Mockito.mock(ServletContext.class);
    Mockito.when(context.getServletContext()).thenReturn(servletContext);

    List<String> initParams = new ArrayList<>();
    initParams.add("name");
    Enumeration<String> initParameterNames = Collections.enumeration(initParams);
    Mockito.when(servletContext.getInitParameterNames()).thenReturn(initParameterNames);

    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertEquals(1, adapter.getApplicationInitParams(context).size());
  }

  /**
   * Resource exists.
   */
  @Test
  void resourceExists() {
    WebResourceRoot webResourceRoot = Mockito.mock(WebResourceRoot.class);
    Mockito.when(context.getResources()).thenReturn(webResourceRoot);

    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertFalse(adapter.resourceExists("name", context));
  }

  /**
   * Resource stream.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Test
  void resourceStream() throws IOException {
    WebResourceRoot webResourceRoot = Mockito.mock(WebResourceRoot.class);
    Mockito.when(context.getResources()).thenReturn(webResourceRoot);

    WebResource webResource = Mockito.mock(WebResource.class);
    Mockito.when(webResourceRoot.getResource("name")).thenReturn(webResource);

    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertNull(adapter.getResourceStream("name", context));
  }

  /**
   * Resource attributes.
   */
  @Test
  void resourceAttributes() {
    WebResourceRoot webResourceRoot = Mockito.mock(WebResourceRoot.class);
    Mockito.when(context.getResources()).thenReturn(webResourceRoot);

    WebResource webResource = Mockito.mock(WebResource.class);
    Mockito.when(webResourceRoot.getResource("name")).thenReturn(webResource);

    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertNotNull(adapter.getResourceAttributes("name", context));
  }

  /**
   * Gets the naming token.
   */
  @Test
  void getNamingToken() {
    Mockito.when(context.getNamingToken()).thenReturn(new Object());

    final Tomcat11ContainerAdapter adapter = new Tomcat11ContainerAdapter();
    assertNotNull(adapter.getNamingToken(context));
  }

}
