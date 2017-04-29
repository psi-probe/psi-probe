/**
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.catalina.Valve;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.junit.Test;

import psiprobe.Tomcat85ContainerAdapter;

/**
 * The Class Tomcat85ContainerAdapterTest.
 */
public class Tomcat85ContainerAdapterTest {

  /**
   * Creates the valve.
   */
  @Test
  public void createValve() {
    final Tomcat85ContainerAdapter adapter = new Tomcat85ContainerAdapter();
    Valve valve = adapter.createValve();
    assertEquals("psiprobe.Tomcat85AgentValve[Container is null]", valve.toString());
  }

  /**
   * Can bound to null.
   */
  @Test
  public void canBoundToNull() {
    final Tomcat85ContainerAdapter adapter = new Tomcat85ContainerAdapter();
    assertFalse(adapter.canBoundTo(null));
  }

  /**
   * Can bound to tomcat85.
   */
  @Test
  public void canBoundToTomcat85() {
    final Tomcat85ContainerAdapter adapter = new Tomcat85ContainerAdapter();
    assertTrue(adapter.canBoundTo("Apache Tomcat/8.5"));
  }

  /**
   * Can bound to tom ee.
   */
  @Test
  public void canBoundToTomEE() {
    final Tomcat85ContainerAdapter adapter = new Tomcat85ContainerAdapter();
    assertTrue(adapter.canBoundTo("Apache Tomcat (TomEE)/8.5"));
  }

  /**
   * Can bound to pivotal8.
   */
  @Test
  public void canBoundToPivotal8() {
    final Tomcat85ContainerAdapter adapter = new Tomcat85ContainerAdapter();
    assertFalse(adapter.canBoundTo("Pivotal tc..../8.0"));
  }

  /**
   * Can bound to pivotal85.
   */
  @Test
  public void canBoundToPivotal85() {
    final Tomcat85ContainerAdapter valve = new Tomcat85ContainerAdapter();
    assertTrue(valve.canBoundTo("Pivotal tc..../8.5"));
  }

  /**
   * Can bound to other.
   */
  @Test
  public void canBoundToOther() {
    final Tomcat85ContainerAdapter adapter = new Tomcat85ContainerAdapter();
    assertFalse(adapter.canBoundTo("Other"));
  }

  /**
   * Filter mappings.
   */
  @Test
  public void FilterMappings() {
    final Tomcat85ContainerAdapter adapter = new Tomcat85ContainerAdapter();
    FilterMap map = new FilterMap();
    map.addServletName("psi-probe");
    map.addURLPattern("/psi-probe");
    assertEquals(2, adapter.getFilterMappings(map, "dispatcherMap", "filterClass").size());
  }

}
