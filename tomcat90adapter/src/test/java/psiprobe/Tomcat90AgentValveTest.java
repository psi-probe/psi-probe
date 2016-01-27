package psiprobe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import psiprobe.Tomcat90ContainerAdapter;

/**
 * The Class Tomcat90AgentValveTest.
 */
@Ignore
public class Tomcat90AgentValveTest {

  /**
   * Can bound to null.
   */
  @Test
  public void canBoundToNull() {
    final Tomcat90ContainerAdapter valve = new Tomcat90ContainerAdapter();
    assertFalse(valve.canBoundTo(null));
  }

  /**
   * Can bound to tomcat7.
   */
  @Test
  public void canBoundToTomcat7() {
    final Tomcat90ContainerAdapter valve = new Tomcat90ContainerAdapter();
    assertTrue(valve.canBoundTo("Apache Tomcat/9.0"));
  }

  /**
   * Can bound to tom ee.
   */
  @Test
  public void canBoundToTomEE() {
    final Tomcat90ContainerAdapter valve = new Tomcat90ContainerAdapter();
    assertTrue(valve.canBoundTo("Apache Tomcat (TomEE)/9.0"));
  }

  /**
   * Can bound to pivotal6.
   */
  @Test
  public void canBoundToPivotal6() {
    final Tomcat90ContainerAdapter valve = new Tomcat90ContainerAdapter();
    assertFalse(valve.canBoundTo("Pivotal tc..../8.0"));
  }

  /**
   * Can bound to pivotal7.
   */
  @Test
  public void canBoundToPivotal7() {
    final Tomcat90ContainerAdapter valve = new Tomcat90ContainerAdapter();
    assertTrue(valve.canBoundTo("Pivotal tc..../9.0"));
  }

  /**
   * Can bound to other.
   */
  @Test
  public void canBoundToOther() {
    final Tomcat90ContainerAdapter valve = new Tomcat90ContainerAdapter();
    assertFalse(valve.canBoundTo("Other"));
  }

}
