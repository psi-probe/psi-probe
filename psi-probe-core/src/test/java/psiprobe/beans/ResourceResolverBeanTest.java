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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.catalina.Context;
import org.apache.catalina.core.StandardServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import psiprobe.TomcatContainer;
import psiprobe.beans.accessors.DatasourceAccessor;
import psiprobe.model.ApplicationResource;
import psiprobe.model.DataSourceInfo;

class ResourceResolverBeanTest {

  private TestableResourceResolverBean bean;

  @BeforeEach
  void setUp() {
    bean = new TestableResourceResolverBean();
    bean.setDatasourceMappers(List.of(TestDatasourceAccessor.class.getName()));
    TestDatasourceAccessor.infoToReturn = null;
    TestDatasourceAccessor.resetToReturn = false;
    TestDatasourceAccessor.lastResource = null;
  }

  @Test
  void getApplicationResourcesMapsJmxAttributesAndInvokesLookup() throws Exception {
    MBeanServer server = mock(MBeanServer.class);
    ObjectName objectName = new ObjectName(
        "Catalina:type=Resource,resourcetype=Global,class=javax.sql.DataSource,name=jdbc/test");

    when(server.queryNames(new ObjectName("Catalina:type=Resource,resourcetype=Global,*"), null))
        .thenReturn(Set.of(objectName));
    when(server.getAttribute(objectName, "name")).thenReturn("jdbc/test");
    when(server.getAttribute(objectName, "type")).thenReturn("javax.sql.DataSource");
    when(server.getAttribute(objectName, "scope")).thenReturn("Shareable");
    when(server.getAttribute(objectName, "auth")).thenReturn("Container");
    when(server.getAttribute(objectName, "description")).thenReturn("Primary DS");

    bean.mbeanServer = server;
    bean.lookupInfo = new DataSourceInfo();

    List<ApplicationResource> resources = bean.getApplicationResources();

    assertEquals(1, resources.size());
    ApplicationResource resource = resources.get(0);
    assertEquals("jdbc/test", resource.getName());
    assertEquals("javax.sql.DataSource", resource.getType());
    assertEquals("Shareable", resource.getScope());
    assertEquals("Container", resource.getAuth());
    assertEquals("Primary DS", resource.getDescription());
    assertSame(bean.lookupInfo, resource.getDataSourceInfo());
    assertEquals(List.of("jdbc/test:true:true"), bean.lookupCalls);
  }

  @Test
  void getApplicationResourcesForContextBindsLooksUpAndUnbinds() throws Exception {
    ContainerWrapperBean containerWrapper = mock(ContainerWrapperBean.class);
    TomcatContainer tomcatContainer = mock(TomcatContainer.class);
    Context context = mock(Context.class);
    ApplicationResource resource = new ApplicationResource();
    resource.setName("jdbc/private");

    when(containerWrapper.getTomcatContainer()).thenReturn(tomcatContainer);
    when(tomcatContainer.getAvailable(context)).thenReturn(true);
    when(context.getName()).thenReturn("/app");

    bean.resourcesToAdd = List.of(resource);
    List<ApplicationResource> resources = bean.getApplicationResources(context, containerWrapper);

    assertEquals(1, resources.size());
    assertEquals(List.of("jdbc/private:true:false"), bean.lookupCalls);
    verify(tomcatContainer).bindToContext(context);
    verify(tomcatContainer).addContextResource(context, resources);
    verify(tomcatContainer).addContextResourceLink(context, resources);
    verify(tomcatContainer).unbindFromContext(context);
  }

  @Test
  void lookupResourceMarksResourceUnresolvedWhenContextIsNotBound() {
    ResourceResolverBean realBean = new ResourceResolverBean();
    realBean.setDatasourceMappers(List.of(TestDatasourceAccessor.class.getName()));
    ApplicationResource resource = new ApplicationResource();
    resource.setName("jdbc/test");
    resource.setLookedUp(true);

    realBean.lookupResource(resource, false, false);

    assertFalse(resource.isLookedUp());
  }

  @Test
  void resetResourceUsesDatasourceAccessorForGlobalResource() throws Exception {
    javax.naming.Context namingContext = mock(javax.naming.Context.class);
    TestDatasourceAccessor.resetToReturn = true;
    when(namingContext.lookup("jdbc/test")).thenReturn("resetResource");

    try (MockedStatic<ResourceResolverBean> mocked = org.mockito.Mockito
        .mockStatic(ResourceResolverBean.class, org.mockito.Mockito.CALLS_REAL_METHODS)) {
      mocked.when(ResourceResolverBean::getGlobalNamingContext).thenReturn(namingContext);
      assertTrue(bean.resetResource(null, "jdbc/test", null));
    }

    assertEquals("resetResource", TestDatasourceAccessor.lastResource);
  }

  @Test
  void getGlobalNamingContextReturnsContextFromManagedStandardServer() throws Exception {
    MBeanServer server = mock(MBeanServer.class);
    javax.naming.Context globalContext = mock(javax.naming.Context.class);
    StandardServer standardServer = new StandardServer();
    standardServer.setGlobalNamingContext(globalContext);
    ObjectName objectName = new ObjectName("Catalina:type=Server");

    when(server.getDomains()).thenReturn(new String[] {"Catalina"});
    when(server.getAttribute(objectName, "managedResource")).thenReturn(standardServer);

    try (MockedStatic<ManagementFactory> mocked =
        org.mockito.Mockito.mockStatic(ManagementFactory.class)) {
      mocked.when(ManagementFactory::getPlatformMBeanServer).thenReturn(server);
      assertSame(globalContext, ResourceResolverBean.getGlobalNamingContext());
    }
  }

  @Test
  void lookupDataSourceReturnsNullWhenResolvedObjectIsNotDataSource() throws Exception {
    javax.naming.Context namingContext = mock(javax.naming.Context.class);
    when(namingContext.lookup("jdbc/notds")).thenReturn("value");

    try (MockedStatic<ResourceResolverBean> mocked = org.mockito.Mockito
        .mockStatic(ResourceResolverBean.class, org.mockito.Mockito.CALLS_REAL_METHODS)) {
      mocked.when(ResourceResolverBean::getGlobalNamingContext).thenReturn(namingContext);
      assertNull(bean.lookupDataSource(null, "jdbc/notds", null));
    }
  }

  public static class TestDatasourceAccessor implements DatasourceAccessor {

    private static DataSourceInfo infoToReturn;

    private static Object lastResource;

    private static boolean resetToReturn;

    @Override
    public DataSourceInfo getInfo(Object resource) throws SQLException {
      lastResource = resource;
      return infoToReturn;
    }

    @Override
    public boolean reset(Object resource) throws SQLException {
      lastResource = resource;
      return resetToReturn;
    }

    @Override
    public boolean canMap(Object resource) {
      return true;
    }
  }

  private static class TestableResourceResolverBean extends ResourceResolverBean {

    private MBeanServer mbeanServer;

    private DataSourceInfo lookupInfo;

    private List<ApplicationResource> resourcesToAdd = List.of();

    private final List<String> lookupCalls = new java.util.ArrayList<>();

    @Override
    public MBeanServer getMBeanServer() {
      return mbeanServer;
    }

    @Override
    public void lookupResource(ApplicationResource resource, boolean contextBound, boolean global) {
      lookupCalls.add(resource.getName() + ":" + contextBound + ":" + global);
      if (lookupInfo != null) {
        resource.setLookedUp(true);
        resource.setDataSourceInfo(lookupInfo);
      }
    }

    @Override
    public synchronized List<ApplicationResource> getApplicationResources(Context context,
        ContainerWrapperBean containerWrapper) throws javax.naming.NamingException {
      return super.getApplicationResources(context, new ContainerWrapperBean() {
        @Override
        public TomcatContainer getTomcatContainer() {
          return new DelegatingTomcatContainer(containerWrapper.getTomcatContainer(),
              resourcesToAdd);
        }
      });
    }
  }

  private static class DelegatingTomcatContainer implements TomcatContainer {

    private final TomcatContainer delegate;

    private final List<ApplicationResource> resourcesToAdd;

    private DelegatingTomcatContainer(TomcatContainer delegate,
        List<ApplicationResource> resourcesToAdd) {
      this.delegate = delegate;
      this.resourcesToAdd = resourcesToAdd;
    }

    @Override
    public void addContextResource(Context context, List<ApplicationResource> resourceList) {
      resourceList.addAll(resourcesToAdd);
      delegate.addContextResource(context, resourceList);
    }

    @Override
    public void addContextResourceLink(Context context, List<ApplicationResource> resourceList) {
      delegate.addContextResourceLink(context, resourceList);
    }

    @Override
    public boolean getAvailable(Context context) {
      return delegate.getAvailable(context);
    }

    @Override
    public void bindToContext(Context context) throws javax.naming.NamingException {
      delegate.bindToContext(context);
    }

    @Override
    public void unbindFromContext(Context context) throws javax.naming.NamingException {
      delegate.unbindFromContext(context);
    }

    @Override
    public Context findContext(String name) {
      return delegate.findContext(name);
    }

    @Override
    public String formatContextName(String name) {
      return delegate.formatContextName(name);
    }

    @Override
    public String formatContextFilename(String contextName) {
      return delegate.formatContextFilename(contextName);
    }

    @Override
    public List<Context> findContexts() {
      return delegate.findContexts();
    }

    @Override
    public List<org.apache.catalina.connector.Connector> findConnectors() {
      return delegate.findConnectors();
    }

    @Override
    public void stop(String name) throws Exception {
      delegate.stop(name);
    }

    @Override
    public void start(String name) throws Exception {
      delegate.start(name);
    }

    @Override
    public void remove(String name) throws Exception {
      delegate.remove(name);
    }

    @Override
    public void installWar(String name) throws Exception {
      delegate.installWar(name);
    }

    @Override
    public java.io.File getAppBase() {
      return delegate.getAppBase();
    }

    @Override
    public java.io.File getConfigFile(Context context) {
      return delegate.getConfigFile(context);
    }

    @Override
    public String getConfigBase() {
      return delegate.getConfigBase();
    }

    @Override
    public void setWrapper(org.apache.catalina.Wrapper wrapper) {
      delegate.setWrapper(wrapper);
    }

    @Override
    public boolean canBoundTo(String binding) {
      return delegate.canBoundTo(binding);
    }

    @Override
    public boolean installContext(String contextName) throws Exception {
      return delegate.installContext(contextName);
    }

    @Override
    public void listContextJsps(Context context, psiprobe.model.jsp.Summary summary,
        boolean compile) {
      delegate.listContextJsps(context, summary, compile);
    }

    @Override
    public void recompileJsps(Context context, psiprobe.model.jsp.Summary summary,
        List<String> names) {
      delegate.recompileJsps(context, summary, names);
    }

    @Override
    public void discardWorkDir(Context context) {
      delegate.discardWorkDir(context);
    }

    @Override
    public String getHostName() {
      return delegate.getHostName();
    }

    @Override
    public String getName() {
      return delegate.getName();
    }

    @Override
    public String getServletFileNameForJsp(Context context, String jspName) {
      return delegate.getServletFileNameForJsp(context, jspName);
    }

    @Override
    public List<psiprobe.model.FilterMapping> getApplicationFilterMaps(Context context) {
      return delegate.getApplicationFilterMaps(context);
    }

    @Override
    public List<psiprobe.model.FilterInfo> getApplicationFilters(Context context) {
      return delegate.getApplicationFilters(context);
    }

    @Override
    public List<psiprobe.model.ApplicationParam> getApplicationInitParams(Context context) {
      return delegate.getApplicationInitParams(context);
    }

    @Override
    public boolean resourceExists(String name, Context context) {
      return delegate.resourceExists(name, context);
    }

    @Override
    public java.io.InputStream getResourceStream(String name, Context context)
        throws java.io.IOException {
      return delegate.getResourceStream(name, context);
    }

    @Override
    public Long[] getResourceAttributes(String name, Context context) {
      return delegate.getResourceAttributes(name, context);
    }
  }
}
