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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import mockit.Expectations;
import mockit.Mocked;

import org.apache.catalina.Context;
import org.apache.catalina.Valve;
import org.apache.jasper.JspCompilationContext;
import org.apache.tomcat.util.descriptor.web.ApplicationParameter;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import psiprobe.model.ApplicationResource;

/**
 * The Class Tomcat10ContainerAdapterTest.
 */
class Tomcat10ContainerAdapterTest {

  /** The context. */
  @Mocked
  Context context;

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
   * Can bound to tomcat10.
   */
  @Test
  void canBoundToTomcat10() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertTrue(adapter.canBoundTo("Apache Tomcat/10.1"));
  }

  /**
   * Can bound to nsjsp10.
   */
  @Test
  void canBoundToNsJsp10() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertTrue(adapter.canBoundTo("NonStop(tm) Servlets For JavaServer Pages(tm) v10.1"));
  }

  /**
   * Can bound to tomEE10.
   */
  @Test
  void canBoundToTomEE10() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertTrue(adapter.canBoundTo("Apache Tomcat (TomEE)/10.0"));
  }

  /**
   * Can bound to vmware10.
   */
  @Test
  void canBoundToVmware10() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertTrue(adapter.canBoundTo("Vmware tc..../10.1"));
  }

  /**
   * Can bound to other.
   */
  @Test
  void canBoundToOther() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertFalse(adapter.canBoundTo("Other"));
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
    JspCompilationContext context = adapter.createJspCompilationContext("name", null, null, null,
        ClassLoader.getSystemClassLoader());
    assertEquals("org.apache.jsp.name", context.getFQCN());
  }

  /**
   * Adds the context resource link.
   */
  @Test
  void addContextResourceLink() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    adapter.addContextResourceLink(context, new ArrayList<ApplicationResource>());
  }

  /**
   * Adds the context resource.
   */
  @Test
  void addContextResource() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    adapter.addContextResource(context, new ArrayList<ApplicationResource>());
  }

  /**
   * Gets the application filter maps.
   */
  @Test
  void applicationFilterMaps() {
    Assertions.assertNotNull(new Expectations() {
      {
        context.findFilterMaps();
        result = new FilterMap();
      }
    });

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(0, adapter.getApplicationFilterMaps(context).size());
  }

  /**
   * Application filters.
   */
  @Test
  void applicationFilters() {
    Assertions.assertNotNull(new Expectations() {
      {
        context.findFilterDefs();
        result = new FilterDef();
      }
    });

    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(1, adapter.getApplicationFilters(context).size());
  }

  /**
   * Application init params.
   */
  @Test
  void applicationInitParams() {
    Assertions.assertNotNull(new Expectations() {
      {
        context.findApplicationParameters();
        result = new ApplicationParameter();
      }
    });
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertEquals(0, adapter.getApplicationInitParams(context).size());
  }

  /**
   * Resource exists.
   */
  @Test
  void resourceExists() {
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
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    adapter.getResourceStream("name", context);
  }

  /**
   * Resource attributes.
   */
  @Test
  void resourceAttributes() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    adapter.getResourceAttributes("name", context);
  }

  /**
   * Gets the naming token.
   */
  @Test
  void getNamingToken() {
    final Tomcat10ContainerAdapter adapter = new Tomcat10ContainerAdapter();
    assertNull(adapter.getNamingToken(context));
  }

}
