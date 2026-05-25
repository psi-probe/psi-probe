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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletContext;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Service;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.junit.jupiter.api.Test;

import psiprobe.model.ApplicationParam;
import psiprobe.model.ApplicationResource;
import psiprobe.model.FilterInfo;
import psiprobe.model.FilterMapping;
import psiprobe.model.jsp.Summary;

/**
 * Tests for {@link AbstractTomcatContainer}.
 */
class AbstractTomcatContainerTest {

  private static class TestTomcatContainer extends AbstractTomcatContainer {

    private final Valve valve = mock(Valve.class);

    @Override
    protected Object getNamingToken(Context context) {
      return "token";
    }

    @Override
    protected JspCompilationContext createJspCompilationContext(String name, Options opt,
        ServletContext sctx, JspRuntimeContext jrctx, ClassLoader classLoader) {
      return null;
    }

    @Override
    protected Valve createValve() {
      return valve;
    }

    @Override
    public boolean canBoundTo(String binding) {
      return true;
    }

    @Override
    public List<FilterMapping> getApplicationFilterMaps(Context context) {
      return List.of();
    }

    @Override
    public void addContextResource(Context context, List<ApplicationResource> resourceList) {
      // no-op for tests
    }

    @Override
    public void addContextResourceLink(Context context, List<ApplicationResource> resourceList) {
      // no-op for tests
    }

    @Override
    public List<FilterInfo> getApplicationFilters(Context context) {
      return List.of();
    }

    @Override
    public List<ApplicationParam> getApplicationInitParams(Context context) {
      return List.of();
    }

    @Override
    public boolean resourceExists(String name, Context context) {
      return false;
    }

    @Override
    public InputStream getResourceStream(String name, Context context) {
      return null;
    }

    @Override
    public Long[] getResourceAttributes(String name, Context context) {
      return new Long[] {0L, 0L};
    }
  }

  @Test
  void setWrapperInitializesContainerStateAndRemovesValveOnUnset() {
    TestTomcatContainer container = new TestTomcatContainer();
    Wrapper wrapper = mock(Wrapper.class);
    Container parent = mock(Container.class);
    Host host = mock(Host.class);
    Engine engine = mock(Engine.class);
    Service service = mock(Service.class);
    Pipeline pipeline = mock(Pipeline.class);

    when(wrapper.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(host);
    when(host.getParent()).thenReturn(engine);
    when(host.getPipeline()).thenReturn(pipeline);
    when(host.getName()).thenReturn("localhost");
    when(host.getAppBase()).thenReturn("webapps");
    when(engine.getService()).thenReturn(service);
    when(engine.getName()).thenReturn("Catalina");
    when(service.findConnectors()).thenReturn(new Connector[] {mock(Connector.class)});

    container.setWrapper(wrapper);
    assertEquals(1, container.findConnectors().size());

    container.setWrapper(null);
    verify(pipeline, times(1)).addValve(any());
    verify(pipeline, times(1)).removeValve(any());
  }

  @Test
  void formatContextNameAndFilenameHandleRootAndNestedPaths() {
    TestTomcatContainer container = new TestTomcatContainer();

    assertNull(container.formatContextName(null));
    assertEquals("", container.formatContextName("ROOT"));
    assertEquals("", container.formatContextName("/ROOT"));
    assertEquals("/app", container.formatContextName("app"));
    assertEquals("##v1", container.formatContextName("/ROOT##v1"));

    assertNull(container.formatContextFilename(null));
    assertEquals("ROOT", container.formatContextFilename("/"));
    assertEquals("ROOT##v2", container.formatContextFilename("/##v2"));
    assertEquals("a#b", container.formatContextFilename("/a/b"));
  }

  @Test
  void findContextFallsBackToSlashForRoot() {
    TestTomcatContainer container = new TestTomcatContainer();
    Host host = mock(Host.class);
    Context rootContext = mock(Context.class);

    container.host = host;
    when(host.findChild("")).thenReturn(null);
    when(host.findChild("/")).thenReturn(rootContext);

    assertEquals(rootContext, container.findContext("/"));
  }

  @Test
  void getAppBaseSupportsRelativeAndAbsolutePaths() {
    TestTomcatContainer container = new TestTomcatContainer();
    Host host = mock(Host.class);

    container.host = host;

    String oldBase = System.getProperty("catalina.base");
    try {
      System.setProperty("catalina.base", "/tmp/catalina-base");

      when(host.getAppBase()).thenReturn("webapps");
      assertEquals(Path.of("/tmp/catalina-base", "webapps").toString(),
          container.getAppBase().toPath().toString());

      when(host.getAppBase()).thenReturn("/opt/webapps");
      assertEquals(Path.of("/opt/webapps").toString(), container.getAppBase().toPath().toString());
    } finally {
      if (oldBase != null) {
        System.setProperty("catalina.base", oldBase);
      }
    }
  }

  @Test
  void findContextsReturnsOnlyContextChildren() {
    TestTomcatContainer container = new TestTomcatContainer();
    Host host = mock(Host.class);
    Context context = mock(Context.class);
    Container other = mock(Container.class);

    container.host = host;
    when(host.findChildren()).thenReturn(new Container[] {context, other});

    List<Context> contexts = container.findContexts();
    assertEquals(1, contexts.size());
    assertEquals(context, contexts.get(0));
  }

  @Test
  void stopAndStartOperateWhenContextExists() throws Exception {
    TestTomcatContainer container = new TestTomcatContainer();
    Host host = mock(Host.class);
    Context context = mock(Context.class);

    container.host = host;
    when(host.findChild("/app")).thenReturn(context);

    container.stop("/app");
    container.start("/app");

    verify(context, times(1)).stop();
    verify(context, times(1)).start();
  }

  @Test
  void installWarInvokesMbeanChangesFlow() throws Exception {
    TestTomcatContainer container = new TestTomcatContainer();
    MBeanServer mBeanServer = mock(MBeanServer.class);

    container.mbeanServer = mBeanServer;
    container.objectNameDeployer = new ObjectName("Catalina:type=Deployer,host=localhost");

    when(mBeanServer.invoke(any(), eq("tryAddServiced"), any(), any())).thenReturn(Boolean.TRUE);
    when(mBeanServer.invoke(any(), eq("check"), any(), any())).thenReturn(null);
    when(mBeanServer.invoke(any(), eq("removeServiced"), any(), any())).thenReturn(null);

    container.installWar("/app");

    verify(mBeanServer, times(1)).invoke(any(), eq("check"), any(), any());
    verify(mBeanServer, times(1)).invoke(any(), eq("removeServiced"), any(), any());
  }

  @Test
  void getConfigFileReturnsOnlyFileScheme() throws Exception {
    TestTomcatContainer container = new TestTomcatContainer();
    Context context = mock(Context.class);

    Path temp = Files.createTempFile("ctx", ".xml");
    temp.toFile().deleteOnExit();
    when(context.getConfigFile()).thenReturn(temp.toUri().toURL());
    assertNotNull(container.getConfigFile(context));

    when(context.getConfigFile()).thenReturn(new URL("https://example.com/context.xml"));
    assertNull(container.getConfigFile(context));
  }

  @Test
  void addFilterMappingSupportsUrlAndServletMappings() {
    TestTomcatContainer container = new TestTomcatContainer();
    List<FilterMapping> mappings = new ArrayList<>();

    container.addFilterMapping("f1", "REQUEST", "FilterClass", new String[] {"/a", "/b"}, mappings,
        AbstractTomcatContainer.FilterMapType.URL);
    container.addFilterMapping("f2", "REQUEST", "FilterClass2", new String[] {"s1"}, mappings,
        AbstractTomcatContainer.FilterMapType.SERVLET_NAME);

    assertEquals(3, mappings.size());
    assertEquals("/a", mappings.get(0).getUrl());
    assertEquals("s1", mappings.get(2).getServletName());
  }

  @Test
  void getAvailableReflectsLifecycleState() {
    TestTomcatContainer container = new TestTomcatContainer();
    Context context = mock(Context.class);

    when(context.getState()).thenReturn(LifecycleState.STARTED);
    assertTrue(container.getAvailable(context));
  }

  @Test
  void getNameAndHostNameUseHostHierarchy() {
    TestTomcatContainer container = new TestTomcatContainer();
    Host host = mock(Host.class);
    Engine engine = mock(Engine.class);

    container.host = host;
    when(host.getName()).thenReturn("localhost");
    when(host.getParent()).thenReturn(engine);
    when(engine.getName()).thenReturn("Catalina");

    assertEquals("localhost", container.getHostName());
    assertEquals("Catalina", container.getName());
  }

  @Test
  void listContextJspsHandlesMissingJspServletWithoutThrowing() {
    TestTomcatContainer container = new TestTomcatContainer();
    Context context = mock(Context.class);
    Summary summary = new Summary();

    when(context.findChild("jsp")).thenReturn(null);
    when(context.getName()).thenReturn("/app");

    container.listContextJsps(context, summary, false);
    assertNotNull(summary.getItems());
    assertTrue(summary.getItems().isEmpty());
  }
}
