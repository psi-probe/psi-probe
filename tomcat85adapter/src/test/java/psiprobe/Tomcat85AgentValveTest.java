package psiprobe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import psiprobe.Tomcat85ContainerAdapter;

/**
 * The Class Tomcat85AgentValveTest.
 */
public class Tomcat85AgentValveTest {

  /**
   * Can bound to null.
   */
  @Test
  public void canBoundToNull() {
    final Tomcat85ContainerAdapter valve = new Tomcat85ContainerAdapter();
    assertFalse(valve.canBoundTo(null));
  }

  /**
   * Can bound to tomcat85.
   */
  @Test
  public void canBoundToTomcat85() {
    final Tomcat85ContainerAdapter valve = new Tomcat85ContainerAdapter();
    assertTrue(valve.canBoundTo("Apache Tomcat/8.5"));
  }

  /**
   * Can bound to tom ee.
   */
  @Test
  public void canBoundToTomEE() {
    final Tomcat85ContainerAdapter valve = new Tomcat85ContainerAdapter();
    assertTrue(valve.canBoundTo("Apache Tomcat (TomEE)/8.5"));
  }

  /**
   * Can bound to pivotal8.
   */
  @Test
  public void canBoundToPivotal8() {
    final Tomcat85ContainerAdapter valve = new Tomcat85ContainerAdapter();
    assertFalse(valve.canBoundTo("Pivotal tc..../8.0"));
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
    final Tomcat85ContainerAdapter valve = new Tomcat85ContainerAdapter();
    assertFalse(valve.canBoundTo("Other"));
  }

}
