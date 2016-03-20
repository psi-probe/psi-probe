package psiprobe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import psiprobe.Tomcat80ContainerAdapter;

/**
 * The Class Tomcat80AgentValveTest.
 */
public class Tomcat80AgentValveTest {

  /**
   * Can bound to null.
   */
  @Test
  public void canBoundToNull() {
    final Tomcat80ContainerAdapter valve = new Tomcat80ContainerAdapter();
    assertFalse(valve.canBoundTo(null));
  }

  /**
   * Can bound to tomcat7.
   */
  @Test
  public void canBoundToTomcat7() {
    final Tomcat80ContainerAdapter valve = new Tomcat80ContainerAdapter();
    assertTrue(valve.canBoundTo("Apache Tomcat/8.0"));
  }

  /**
   * Can bound to tom ee.
   */
  @Test
  public void canBoundToTomEE() {
    final Tomcat80ContainerAdapter valve = new Tomcat80ContainerAdapter();
    assertTrue(valve.canBoundTo("Apache Tomcat (TomEE)/8.0"));
  }

  /**
   * Can bound to pivotal7.
   */
  @Test
  public void canBoundToPivotal7() {
    final Tomcat80ContainerAdapter valve = new Tomcat80ContainerAdapter();
    assertFalse(valve.canBoundTo("Pivotal tc..../7.0"));
  }

  /**
   * Can bound to pivotal8.
   */
  @Test
  public void canBoundToPivotal8() {
    final Tomcat80ContainerAdapter valve = new Tomcat80ContainerAdapter();
    assertTrue(valve.canBoundTo("Pivotal tc..../8.0"));
  }

  /**
   * Can bound to other.
   */
  @Test
  public void canBoundToOther() {
    final Tomcat80ContainerAdapter valve = new Tomcat80ContainerAdapter();
    assertFalse(valve.canBoundTo("Other"));
  }

}
