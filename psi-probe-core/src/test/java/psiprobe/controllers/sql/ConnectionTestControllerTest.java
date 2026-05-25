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
package psiprobe.controllers.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.beans.ContainerWrapperBean;
import psiprobe.beans.ResourceResolver;
import psiprobe.TomcatContainer;

class ConnectionTestControllerTest {

  private ConnectionTestController controller;
  private ContainerWrapperBean containerWrapper;
  private ResourceResolver resourceResolver;
  private TomcatContainer tomcatContainer;

  @BeforeEach
  void setUp() throws Exception {
    controller = new ConnectionTestController();
    controller.setViewName("ajax/sql/connection");

    StaticApplicationContext applicationContext = new StaticApplicationContext();
    applicationContext.addMessage("probe.src.dataSourceTest.resource.lookup.failure",
        Locale.getDefault(), "lookup {0}");
    applicationContext.addMessage("probe.src.dataSourceTest.connection.failure", Locale.getDefault(),
        "connection {0}");
    applicationContext.addMessage("probe.jsp.dataSourceTest.dbMetaData.dbProdName",
        Locale.getDefault(), "db product");
    applicationContext.addMessage("probe.jsp.dataSourceTest.dbMetaData.dbProdVersion",
        Locale.getDefault(), "db version");
    applicationContext.addMessage("probe.jsp.dataSourceTest.dbMetaData.jdbcDriverName",
        Locale.getDefault(), "driver name");
    applicationContext.addMessage("probe.jsp.dataSourceTest.dbMetaData.jdbcDriverVersion",
        Locale.getDefault(), "driver version");
    applicationContext.addMessage("probe.jsp.dataSourceTest.dbMetaData.jdbcVersion",
        Locale.getDefault(), "jdbc version");
    controller.setApplicationContext(applicationContext);

    containerWrapper = mock(ContainerWrapperBean.class);
    resourceResolver = mock(ResourceResolver.class);
    tomcatContainer = mock(TomcatContainer.class);

    when(containerWrapper.getResourceResolver()).thenReturn(resourceResolver);
    when(containerWrapper.getTomcatContainer()).thenReturn(tomcatContainer);
    when(tomcatContainer.formatContextName("app")).thenReturn("/app");

    controller.setContainerWrapper(containerWrapper);
  }

  @Test
  void handleRequestReturnsDatabaseMetadataWhenLookupSucceeds() throws Exception {
    Context context = mock(Context.class);
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    DatabaseMetaData metaData = mock(DatabaseMetaData.class);

    when(tomcatContainer.findContext("/app")).thenReturn(context);
    when(resourceResolver.lookupDataSource(context, "jdbc/test", containerWrapper))
        .thenReturn(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.getMetaData()).thenReturn(metaData);
    when(metaData.getDatabaseProductName()).thenReturn("PostgreSQL");
    when(metaData.getDatabaseProductVersion()).thenReturn("16");
    when(metaData.getDriverName()).thenReturn("Driver");
    when(metaData.getDriverVersion()).thenReturn("1.0");
    when(metaData.getJDBCMajorVersion()).thenReturn(4);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter("webapp", "app");
    request.addParameter("resource", "jdbc/test");

    ModelAndView modelAndView = controller.handleRequest(request, new MockHttpServletResponse());

    assertEquals("ajax/sql/connection", modelAndView.getViewName());
    @SuppressWarnings("unchecked")
    List<Map<String, String>> dbMetaData =
        (List<Map<String, String>>) modelAndView.getModel().get("dbMetaData");
    assertEquals(5, dbMetaData.size());
    assertEquals("db product", dbMetaData.getFirst().get("propertyName"));
    assertEquals("PostgreSQL", dbMetaData.getFirst().get("propertyValue"));
  }

  @Test
  void handleRequestSetsLookupErrorWhenDatasourceLookupFails() throws Exception {
    Context context = mock(Context.class);
    when(tomcatContainer.findContext("/app")).thenReturn(context);
    when(resourceResolver.lookupDataSource(context, "jdbc/missing", containerWrapper))
        .thenThrow(new NamingException("boom"));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter("webapp", "app");
    request.addParameter("resource", "jdbc/missing");

    ModelAndView modelAndView = controller.handleRequest(request, new MockHttpServletResponse());

    assertEquals("ajax/sql/connection", modelAndView.getViewName());
    assertEquals("lookup jdbc/missing", request.getAttribute("errorMessage"));
  }

  @Test
  void handleRequestSetsConnectionErrorWhenConnectionFails() throws Exception {
    Context context = mock(Context.class);
    DataSource dataSource = mock(DataSource.class);
    when(tomcatContainer.findContext("/app")).thenReturn(context);
    when(resourceResolver.lookupDataSource(context, "jdbc/test", containerWrapper))
        .thenReturn(dataSource);
    when(dataSource.getConnection()).thenThrow(new SQLException("down"));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addParameter("webapp", "app");
    request.addParameter("resource", "jdbc/test");

    ModelAndView modelAndView = controller.handleRequest(request, new MockHttpServletResponse());

    assertEquals("ajax/sql/connection", modelAndView.getViewName());
    assertEquals("connection down", request.getAttribute("errorMessage"));
    assertTrue(modelAndView.getModel().isEmpty());
  }
}
