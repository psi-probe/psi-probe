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
package psiprobe.beans;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.catalina.Context;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import psiprobe.model.ApplicationResource;

/** Tests for {@link JBossResourceResolverBean}. */
class JBossResourceResolverBeanTest {

  @Test
  void supportsFlagsMatchJbossCapabilities() {
    JBossResourceResolverBean bean = new JBossResourceResolverBean();

    assertFalse(bean.supportsPrivateResources());
    assertTrue(bean.supportsGlobalResources());
    assertFalse(bean.supportsDataSourceLookup());
  }

  @Test
  void getApplicationResourcesMapsJmxDataToProbeModel() throws Exception {
    MBeanServer server = mock(MBeanServer.class);
    ObjectName poolObjectName =
        new ObjectName("jboss.jca:service=ManagedConnectionPool,name=ExamplePool");
    ObjectName factoryObjectName =
        new ObjectName("jboss.jca:service=ManagedConnectionFactory,name=ExamplePool");

    when(server.queryNames(new ObjectName("jboss.jca:service=ManagedConnectionPool,*"), null))
        .thenReturn(Set.of(poolObjectName));

    when(server.getAttribute(poolObjectName, "Criteria")).thenReturn("ByContainerAndApplication");
    when(server.getAttribute(poolObjectName, "MaxSize")).thenReturn(32);
    when(server.getAttribute(poolObjectName, "ConnectionCount")).thenReturn(12);
    when(server.getAttribute(poolObjectName, "InUseConnectionCount")).thenReturn(4L);
    when(server.getAttribute(factoryObjectName, "ManagedConnectionFactoryProperties"))
        .thenReturn(parseProperties(
            "<props>" + "<property name='ConnectionURL'>jdbc:postgresql://localhost/db</property>"
                + "<property name='UserName'>app-user</property>"
                + "<property name='JmsProviderAdapterJNDI'>jms/connectionFactory</property>"
                + "</props>"));

    JBossResourceResolverBean bean = new TestableJBossResourceResolverBean(server);

    List<ApplicationResource> resources = bean.getApplicationResources();

    assertEquals(1, resources.size());
    ApplicationResource resource = resources.get(0);
    assertEquals("ExamplePool", resource.getName());
    assertEquals("Both", resource.getAuth());
    assertEquals("jms", resource.getType());
    assertNotNull(resource.getDataSourceInfo());
    assertEquals(32, resource.getDataSourceInfo().getMaxConnections());
    assertEquals(12, resource.getDataSourceInfo().getEstablishedConnections());
    assertEquals(4, resource.getDataSourceInfo().getBusyConnections());
    assertEquals("app-user", resource.getDataSourceInfo().getUsername());
    assertEquals("jms/connectionFactory", resource.getDataSourceInfo().getJdbcUrl());
    assertTrue(resource.getDataSourceInfo().isResettable());
  }

  @Test
  void getApplicationResourcesReturnsEmptyWhenNoMBeanServerPresent() throws Exception {
    JBossResourceResolverBean bean = new TestableJBossResourceResolverBean(null);

    assertEquals(List.of(), bean.getApplicationResources());
  }

  @Test
  void getApplicationResourcesReturnsEmptyWhenServerThrows() throws Exception {
    MBeanServer server = mock(MBeanServer.class);
    when(server.queryNames(ArgumentMatchers.any(), isNull()))
        .thenThrow(new RuntimeException("boom"));

    JBossResourceResolverBean bean = new TestableJBossResourceResolverBean(server);

    assertEquals(List.of(), bean.getApplicationResources());
  }

  @Test
  void getApplicationResourcesContextMethodReturnsEmptyList() throws Exception {
    JBossResourceResolverBean bean = new JBossResourceResolverBean();

    assertTrue(bean.getApplicationResources(mock(Context.class)).isEmpty());
  }

  @Test
  void getApplicationResourcesContextWithWrapperIsUnsupported() {
    JBossResourceResolverBean bean = new JBossResourceResolverBean();

    assertThrows(UnsupportedOperationException.class,
        () -> bean.getApplicationResources(mock(Context.class), mock(ContainerWrapperBean.class)));
  }

  @Test
  void resetResourceStopsAndStartsConnectionPool() throws Exception {
    MBeanServer server = mock(MBeanServer.class);
    JBossResourceResolverBean bean = new TestableJBossResourceResolverBean(server);

    boolean reset = bean.resetResource(null, "ExamplePool", null);

    assertTrue(reset);
    ObjectName expected =
        new ObjectName("jboss.jca:service=ManagedConnectionPool,name=ExamplePool");
    verify(server).invoke(expected, "stop", null, null);
    verify(server).invoke(expected, "start", null, null);
  }

  @Test
  void resetResourceReturnsFalseWhenServerMissing() throws Exception {
    JBossResourceResolverBean bean = new TestableJBossResourceResolverBean(null);

    assertFalse(bean.resetResource(null, "ExamplePool", null));
  }

  @Test
  void resetResourceReturnsFalseWhenInvokeFails() throws Exception {
    MBeanServer server = mock(MBeanServer.class);
    doThrow(new RuntimeException("failed")).when(server).invoke(any(), eq("stop"), isNull(),
        isNull());

    JBossResourceResolverBean bean = new TestableJBossResourceResolverBean(server);

    assertFalse(bean.resetResource(null, "ExamplePool", null));
  }

  @Test
  void resetResourceWithMalformedNameThrowsNamingException() {
    JBossResourceResolverBean bean = new JBossResourceResolverBean();

    NamingException exception =
        assertThrows(NamingException.class, () -> bean.resetResource(null, "bad:name", null));

    assertTrue(exception.getMessage().contains("malformed ObjectName"));
  }

  @Test
  void lookupDataSourceIsUnsupported() {
    JBossResourceResolverBean bean = new JBossResourceResolverBean();

    assertThrows(UnsupportedOperationException.class,
        () -> bean.lookupDataSource(null, "resource", null));
  }

  private static Element parseProperties(String xml) throws Exception {
    return DocumentBuilderFactory.newInstance().newDocumentBuilder()
        .parse(new InputSource(new StringReader(xml))).getDocumentElement();
  }

  private static class TestableJBossResourceResolverBean extends JBossResourceResolverBean {

    private final MBeanServer server;

    private TestableJBossResourceResolverBean(MBeanServer server) {
      this.server = server;
    }

    @Override
    public MBeanServer getMBeanServer() {
      return server;
    }
  }
}
