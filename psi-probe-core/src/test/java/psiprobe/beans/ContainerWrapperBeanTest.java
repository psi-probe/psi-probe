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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codebox.bean.JavaBeanTester;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.catalina.Context;
import org.junit.jupiter.api.Test;

import psiprobe.TomcatContainer;
import psiprobe.model.ApplicationResource;
import psiprobe.model.DataSourceInfo;

/**
 * The Class ContainerWrapperBeanTest.
 */
class ContainerWrapperBeanTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(ContainerWrapperBean.class).loadData().test();
  }

  @Test
  void getResourceResolverSelectsDefaultAndJbossResolvers() {
    ResourceResolver defaultResolver = mock(ResourceResolver.class);
    ResourceResolver jbossResolver = mock(ResourceResolver.class);

    ContainerWrapperBean defaultBean = new ContainerWrapperBean();
    defaultBean.setResourceResolvers(Map.of("default", defaultResolver, "jboss", jbossResolver));
    assertSame(defaultResolver, defaultBean.getResourceResolver());
    assertSame(defaultResolver, defaultBean.getResourceResolver());

    String previous = System.getProperty("jboss.server.name");
    try {
      System.setProperty("jboss.server.name", "node1");
      ContainerWrapperBean jbossBean = new ContainerWrapperBean();
      jbossBean.setResourceResolvers(Map.of("default", defaultResolver, "jboss", jbossResolver));
      assertSame(jbossResolver, jbossBean.getResourceResolver());
    } finally {
      if (previous == null) {
        System.clearProperty("jboss.server.name");
      } else {
        System.setProperty("jboss.server.name", previous);
      }
    }
  }

  @Test
  void getDataSourcesAggregatesPrivateAndGlobalDataSources() throws Exception {
    ContainerWrapperBean bean = new ContainerWrapperBean();
    ResourceResolver resolver = mock(ResourceResolver.class);
    Context context = mock(Context.class);
    TomcatContainer tomcatContainer = mock(TomcatContainer.class);

    bean.setResourceResolvers(Map.of("default", resolver));
    setTomcatContainer(bean, tomcatContainer);

    when(resolver.supportsPrivateResources()).thenReturn(true);
    when(resolver.supportsGlobalResources()).thenReturn(true);
    when(tomcatContainer.findContexts()).thenReturn(List.of(context));

    ApplicationResource privateResource = resourceWithDataSource("jdbc/private");
    ApplicationResource privateNonDatasource = new ApplicationResource();
    when(resolver.getApplicationResources(context, bean))
        .thenReturn(List.of(privateResource, privateNonDatasource));

    ApplicationResource globalResource = resourceWithDataSource("jdbc/global");
    when(resolver.getApplicationResources()).thenReturn(List.of(globalResource));

    List<ApplicationResource> dataSources = bean.getDataSources();

    assertEquals(2, dataSources.size());
    assertTrue(dataSources.stream().anyMatch(resource -> "jdbc/private".equals(resource.getName())));
    assertTrue(dataSources.stream().anyMatch(resource -> "jdbc/global".equals(resource.getName())));
  }

  private static ApplicationResource resourceWithDataSource(String name) {
    ApplicationResource resource = new ApplicationResource();
    resource.setName(name);
    resource.setDataSourceInfo(new DataSourceInfo());
    return resource;
  }

  private static void setTomcatContainer(ContainerWrapperBean bean, TomcatContainer container)
      throws NoSuchFieldException, IllegalAccessException {
    Field field = ContainerWrapperBean.class.getDeclaredField("tomcatContainer");
    field.setAccessible(true);
    field.set(bean, container);
  }

}
