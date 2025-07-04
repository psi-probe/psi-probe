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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.JspCompilationContext;
import org.apache.naming.ContextAccessController;
import org.apache.tomcat.util.descriptor.web.ApplicationParameter;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.apache.tomcat.util.descriptor.web.ContextResourceLink;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import psiprobe.model.ApplicationResource;

/**
 * The Class Tomcat10ContainerAdapterTest.
 */
@ExtendWith(MockitoExtension.class)
class Tomcat10ContainerAdapterTest {

  /** The context. */
  @Mock
  Context context;

  /** The embedded servlet options. */
  @Mock
  EmbeddedServletOptions options;

  /**
   * Creates the valve.
   */
  @Test
  void createValve() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    Valve valve = adapter.createValve();
    assertEquals("Tomcat10AgentValve[Container is null]", valve.toString());
  }

  /**
   * Can bound to null.
   */
  @Test
  void canBoundToNull() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertFalse(adapter.canBoundTo(null));
  }

  /**
   * Can bound to tomcat 10.1, tomee 10.0, nsjsp 10.1, vmware tc 10.1.
   *
   * @param container the container
   */
  @ParameterizedTest
  @ValueSource(strings = {"Apache Tomcat/10.1", "Apache Tomcat (TomEE)/10.0",
      "NonStop(tm) Servlets For JavaServer Pages(tm) v10.1", "Vmware tc..../10.1"})
  void canBoundTo(String container) {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertTrue(adapter.canBoundTo(container));
  }

  /**
   * Can not bound to other containers.
   *
   * @param container the container
   */
  @ParameterizedTest
  @ValueSource(strings = {"Vmware tc", "Other"})
  void cannotBoundToOthers(String container) {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertFalse(adapter.canBoundTo(container));
  }

  /**
   * Filter mappings.
   */
  @Test
  void filterMappings() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
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
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    Mockito.when(this.options.getGeneratedJspPackageName()).thenReturn("org.apache.jsp");
    JspCompilationContext jspContext = adapter.createJspCompilationContext("name", this.options,
        null, null, ClassLoader.getSystemClassLoader());
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
        .thenReturn(new ContextResourceLink[] {new ContextResourceLink()});

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
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
        .thenReturn(new ContextResource[] {new ContextResource()});

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    final List<ApplicationResource> list = new ArrayList<ApplicationResource>();
    adapter.addContextResource(context, list);
    assertFalse(list.isEmpty());
  }

  /**
   * Gets the application filter maps.
   */
  @Test
  void applicationFilterMaps() {
    Mockito.when(context.findFilterMaps()).thenReturn(new FilterMap[] {new FilterMap()});

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(0, adapter.getApplicationFilterMaps(context).size());
  }

  /**
   * Gets the application filter maps async.
   *
   * @param dispatcher the dispatcher
   */
  @ParameterizedTest
  @ValueSource(strings = {"ASYNC", "ERROR", "FORWARD", "INCLUDE", "NONE"})
  void applicationFilterMapsTypes(String dispatcher) {
    FilterMap filterMap = new FilterMap();
    filterMap.setDispatcher(dispatcher);
    Mockito.when(context.findFilterMaps()).thenReturn(new FilterMap[] {filterMap});

    Mockito.when(context.findFilterDef(Mockito.any())).thenReturn(new FilterDef());

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(0, adapter.getApplicationFilterMaps(context).size());
  }

  /**
   * Gets the application filter maps throws on unknown dispatcher mapping.
   */
  @Test
  void applicationFilterMapsThrowsOnUnknownDispatcherMapping() {
    Context mockContext = Mockito.mock(Context.class);
    FilterMap filterMap = Mockito.mock(FilterMap.class);

    // Return an invalid dispatcher mapping value
    Mockito.when(filterMap.getDispatcherMapping()).thenReturn(-1);
    Mockito.when(mockContext.findFilterMaps()).thenReturn(new FilterMap[] {filterMap});

    Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();

    assertThrows(NullPointerException.class, () -> adapter.getApplicationFilterMaps(mockContext));
  }

  /**
   * Application filters.
   */
  @Test
  void applicationFilters() {
    Mockito.when(context.findFilterDefs()).thenReturn(new FilterDef[] {new FilterDef()});

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(1, adapter.getApplicationFilters(context).size());
  }

  /**
   * Application init params.
   */
  @Test
  void applicationInitParams() {
    Mockito.when(context.findApplicationParameters())
        .thenReturn(new ApplicationParameter[] {new ApplicationParameter()});

    ServletContext servletContext = Mockito.mock(ServletContext.class);
    Mockito.when(context.getServletContext()).thenReturn(servletContext);

    List<String> initParams = new ArrayList<>();
    initParams.add("name");
    Enumeration<String> initParameterNames = Collections.enumeration(initParams);
    Mockito.when(servletContext.getInitParameterNames()).thenReturn(initParameterNames);

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(1, adapter.getApplicationInitParams(context).size());
  }

  /**
   * Application init params none.
   */
  @Test
  void applicationInitParamsNone() {
    Mockito.when(context.findApplicationParameters())
        .thenReturn(new ApplicationParameter[] {(ApplicationParameter) null});

    ServletContext servletContext = Mockito.mock(ServletContext.class);
    Mockito.when(context.getServletContext()).thenReturn(servletContext);

    List<String> initParams = new ArrayList<>();
    initParams.add("name");
    Enumeration<String> initParameterNames = Collections.enumeration(initParams);
    Mockito.when(servletContext.getInitParameterNames()).thenReturn(initParameterNames);

    Mockito.when(context.findParameter(Mockito.any())).thenReturn(null);

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(1, adapter.getApplicationInitParams(context).size());
  }

  /**
   * Application init params not override attempt.
   */
  @Test
  void applicationInitParamsNotOverrideAttempt() {
    ApplicationParameter appParam = new ApplicationParameter();
    appParam.setName("noOverride");
    appParam.setOverride(false);
    Mockito.when(context.findApplicationParameters())
        .thenReturn(new ApplicationParameter[] {appParam});

    ServletContext servletContext = Mockito.mock(ServletContext.class);
    Mockito.when(context.getServletContext()).thenReturn(servletContext);

    List<String> initParams = new ArrayList<>();
    initParams.add("name");
    Enumeration<String> initParameterNames = Collections.enumeration(initParams);
    Mockito.when(servletContext.getInitParameterNames()).thenReturn(initParameterNames);

    Mockito.when(context.findParameter(Mockito.any())).thenReturn("name");

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(1, adapter.getApplicationInitParams(context).size());
  }

  /**
   * Application init params not override attempt.
   */
  @Test
  void applicationInitParamsOverrideAttempt() {
    ApplicationParameter appParam = new ApplicationParameter();
    appParam.setName("override");
    appParam.setOverride(false);
    Mockito.when(context.findApplicationParameters())
        .thenReturn(new ApplicationParameter[] {appParam});

    ServletContext servletContext = Mockito.mock(ServletContext.class);
    Mockito.when(context.getServletContext()).thenReturn(servletContext);

    List<String> initParams = new ArrayList<>();
    initParams.add("override");
    Enumeration<String> initParameterNames = Collections.enumeration(initParams);
    Mockito.when(servletContext.getInitParameterNames()).thenReturn(initParameterNames);

    Mockito.when(context.findParameter(Mockito.any())).thenReturn("override");

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(1, adapter.getApplicationInitParams(context).size());
  }

  /**
   * Resource exists.
   */
  @Test
  void resourceExists() {
    WebResourceRoot webResourceRoot = Mockito.mock(WebResourceRoot.class);
    Mockito.when(context.getResources()).thenReturn(webResourceRoot);

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertFalse(adapter.resourceExists("name", context));
  }

  /**
   * Resource exists when true.
   */
  @Test
  void resourceExistsWhenTrue() {
    WebResourceRoot webResourceRoot = Mockito.mock(WebResourceRoot.class);
    Mockito.when(context.getResources()).thenReturn(webResourceRoot);

    WebResource webResource = Mockito.mock(WebResource.class);
    Mockito.when(webResourceRoot.getResource("name")).thenReturn(webResource);

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertTrue(adapter.resourceExists("name", context));
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

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
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

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertNotNull(adapter.getResourceAttributes("name", context));
  }

  /**
   * Gets the naming token.
   */
  @Test
  void namingToken() {
    Mockito.when(context.getNamingToken()).thenReturn(new Object());

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertNotNull(adapter.getNamingToken(context));
  }

  /**
   * Gets the naming token with security token check false.
   */
  @Test
  void namingTokenWithSecurityTokenCheckFalse() {
    Mockito.when(context.getNamingToken()).thenReturn(new Object());

    try (MockedStatic<ContextAccessController> mocked =
        Mockito.mockStatic(ContextAccessController.class)) {
      mocked.when(() -> ContextAccessController.checkSecurityToken(Mockito.any(), Mockito.any()))
          .thenReturn(false);

      final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
      assertNotNull(adapter.getNamingToken(context));
    }
  }

  /**
   * Application filters when none.
   */
  @Test
  void applicationFiltersWhenNone() {
    Mockito.when(context.findFilterDefs()).thenReturn(new FilterDef[] {(FilterDef) null});

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertTrue(adapter.getApplicationFilters(context).isEmpty());
  }

  /**
   * Application filter maps when none.
   */
  @Test
  void applicationFilterMapsWhenNone() {
    Mockito.when(context.findFilterMaps()).thenReturn(new FilterMap[] {(FilterMap) null});

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertTrue(adapter.getApplicationFilterMaps(context).isEmpty());
  }

}
