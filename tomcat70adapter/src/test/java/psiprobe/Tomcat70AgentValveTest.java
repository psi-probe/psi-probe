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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import psiprobe.Tomcat70ContainerAdapter;

/**
 * The Class Tomcat70AgentValveTest.
 */
public class Tomcat70AgentValveTest {

  /**
   * Can bound to null.
   */
  @Test
  public void canBoundToNull() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertFalse(valve.canBoundTo(null));
  }

  /**
   * Can bound to tomcat7.
   */
  @Test
  public void canBoundToTomcat7() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertTrue(valve.canBoundTo("Apache Tomcat/7.0"));
  }

  /**
   * Can bound to tom ee.
   */
  @Test
  public void canBoundToTomEE() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertTrue(valve.canBoundTo("Apache Tomcat (TomEE)/7.0"));
  }

  /**
   * Can bound to j boss3.
   */
  @Test
  public void canBoundToJBoss3() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertTrue(valve.canBoundTo("JBoss Web/3.0"));
  }

  /**
   * Can bound to j boss7.
   */
  @Test
  public void canBoundToJBoss7() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertTrue(valve.canBoundTo("JBoss Web/7.0"));
  }

  /**
   * Can bound to ns jsp7.
   */
  @Test
  public void canBoundToNsJsp7() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertTrue(valve.canBoundTo("NonStop(tm) Servlets For JavaServer Pages(tm) v7.0"));
  }

  /**
   * Can bound to spring tc6.
   */
  @Test
  public void canBoundToSpringTc6() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertFalse(valve.canBoundTo("SpringSource tc..../6.0"));
  }

  /**
   * Can bound to spring tc7.
   */
  @Test
  public void canBoundToSpringTc7() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertTrue(valve.canBoundTo("SpringSource tc..../7.0"));
  }

  /**
   * Can bound to vm ware6.
   */
  @Test
  public void canBoundToVmWare6() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertFalse(valve.canBoundTo("VMware vFabric tc..../6.0"));
  }

  /**
   * Can bound to vm ware7.
   */
  @Test
  public void canBoundToVmWare7() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertTrue(valve.canBoundTo("VMware vFabric tc..../7.0"));
  }

  /**
   * Can bound to pivotal6.
   */
  @Test
  public void canBoundToPivotal6() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertFalse(valve.canBoundTo("Pivotal tc..../6.0"));
  }

  /**
   * Can bound to pivotal7.
   */
  @Test
  public void canBoundToPivotal7() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertTrue(valve.canBoundTo("Pivotal tc..../7.0"));
  }

  /**
   * Can bound to other.
   */
  @Test
  public void canBoundToOther() {
    final Tomcat70ContainerAdapter valve = new Tomcat70ContainerAdapter();
    assertFalse(valve.canBoundTo("Other"));
  }

}
