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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

import psiprobe.TomcatContainer;
import psiprobe.beans.ContainerWrapperBean;
import psiprobe.beans.ResourceResolver;
import psiprobe.model.sql.DataSourceTestInfo;

class ExecuteSqlControllerTest {

  private ExecuteSqlController controller;

  private ContainerWrapperBean containerWrapper;

  private ResourceResolver resourceResolver;

  private TomcatContainer tomcatContainer;

  private Context context;

  @BeforeEach
  void setUp() throws Exception {
    controller = new ExecuteSqlController();
    controller.setViewName("ajax/sql/recordset");

    StaticApplicationContext applicationContext = new StaticApplicationContext();
    applicationContext.addMessage("probe.src.dataSourceTest.sql.required", Locale.getDefault(),
        "sql required");
    applicationContext.addMessage("probe.src.dataSourceTest.resource.lookup.failure",
        Locale.getDefault(), "lookup {0}");
    applicationContext.addMessage("probe.src.dataSourceTest.sql.failure", Locale.getDefault(),
        "sql failure {0}");
    applicationContext.addMessage("probe.src.dataSourceTest.sql.null", Locale.getDefault(), "NULL");
    applicationContext.refresh();
    controller.setApplicationContext(applicationContext);

    containerWrapper = mock(ContainerWrapperBean.class);
    resourceResolver = mock(ResourceResolver.class);
    tomcatContainer = mock(TomcatContainer.class);
    context = mock(Context.class);

    when(containerWrapper.getResourceResolver()).thenReturn(resourceResolver);
    when(containerWrapper.getTomcatContainer()).thenReturn(tomcatContainer);
    when(tomcatContainer.formatContextName("app")).thenReturn("/app");
    when(tomcatContainer.findContext("/app")).thenReturn(context);
    controller.setContainerWrapper(containerWrapper);
  }

  @Test
  void handleRequestReturnsEscapedResultSetAndStoresSessionState() throws Exception {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);
    ResultSetMetaData metaData = mock(ResultSetMetaData.class);

    when(resourceResolver.lookupDataSource(context, "jdbc/test", containerWrapper))
        .thenReturn(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("select * from t")).thenReturn(statement);
    when(statement.execute()).thenReturn(true);
    when(statement.getResultSet()).thenReturn(resultSet);
    when(resultSet.getMetaData()).thenReturn(metaData);
    when(metaData.getColumnCount()).thenReturn(2);
    when(metaData.getColumnLabel(1)).thenReturn("name");
    when(metaData.getColumnLabel(2)).thenReturn("name");
    when(resultSet.next()).thenReturn(true, true);
    when(resultSet.getString(1)).thenReturn("<b>unsafe</b>");
    when(resultSet.getString(2)).thenReturn((String) null);
    when(resultSet.wasNull()).thenReturn(false, true);

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/sql/recordset.ajax");
    request.addParameter("webapp", "app");
    request.addParameter("resource", "jdbc/test");
    request.addParameter("sql", "select * from t");
    request.addParameter("maxRows", "1");
    request.addParameter("rowsPerPage", "20");
    request.addParameter("historySize", "5");
    request.getSession(true);

    ModelAndView modelAndView = controller.handleRequest(request, new MockHttpServletResponse());

    assertEquals("ajax/sql/recordset", modelAndView.getViewName());
    assertEquals("1", modelAndView.getModel().get("rowsAffected"));
    assertEquals("20", modelAndView.getModel().get("rowsPerPage"));

    @SuppressWarnings("unchecked")
    List<Map<String, String>> results =
        (List<Map<String, String>>) modelAndView.getModel().get("results");
    assertEquals(1, results.size());
    assertEquals("&lt;b&gt;unsafe&lt;/b&gt;", results.get(0).get("name"));
    assertEquals("NULL", results.get(0).get("name "));

    DataSourceTestInfo sessionData = (DataSourceTestInfo) request.getSession(false)
        .getAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR);
    assertEquals(1, sessionData.getResults().size());
    assertEquals(List.of("select * from t"), sessionData.getQueryHistory());
    assertEquals(1, sessionData.getMaxRows());
    assertEquals(20, sessionData.getRowsPerPage());
    assertEquals(5, sessionData.getHistorySize());
  }

  @Test
  void handleRequestReturnsUpdateCountWhenStatementHasNoResultSet() throws Exception {
    DataSource dataSource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    PreparedStatement statement = mock(PreparedStatement.class);

    when(resourceResolver.lookupDataSource(context, "jdbc/test", containerWrapper))
        .thenReturn(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement("update t set c = 1")).thenReturn(statement);
    when(statement.execute()).thenReturn(false);
    when(statement.getUpdateCount()).thenReturn(7);

    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/sql/recordset.ajax");
    request.addParameter("webapp", "app");
    request.addParameter("resource", "jdbc/test");
    request.addParameter("sql", "update t set c = 1");
    request.getSession(true);

    ModelAndView modelAndView = controller.handleRequest(request, new MockHttpServletResponse());

    assertEquals("7", modelAndView.getModel().get("rowsAffected"));
    assertEquals("0", modelAndView.getModel().get("rowsPerPage"));
    assertTrue(modelAndView.getModel().containsKey("results"));
    assertNull(modelAndView.getModel().get("results"));
  }

  @Test
  void handleRequestSetsErrorMessagesForBlankSqlLookupFailureAndSqlException() throws Exception {
    MockHttpServletRequest blankSqlRequest =
        new MockHttpServletRequest("GET", "/sql/recordset.ajax");
    blankSqlRequest.addParameter("webapp", "app");
    blankSqlRequest.addParameter("sql", "   ");
    blankSqlRequest.getSession(true);
    ModelAndView blankSql =
        controller.handleRequest(blankSqlRequest, new MockHttpServletResponse());
    assertEquals("ajax/sql/recordset", blankSql.getViewName());
    assertEquals("sql required", blankSqlRequest.getAttribute("errorMessage"));

    MockHttpServletRequest lookupFailureRequest =
        new MockHttpServletRequest("GET", "/sql/recordset.ajax");
    lookupFailureRequest.addParameter("webapp", "app");
    lookupFailureRequest.addParameter("resource", "jdbc/missing");
    lookupFailureRequest.addParameter("sql", "select 1");
    lookupFailureRequest.getSession(true);
    when(resourceResolver.lookupDataSource(context, "jdbc/missing", containerWrapper))
        .thenThrow(new NamingException("boom"));
    controller.handleRequest(lookupFailureRequest, new MockHttpServletResponse());
    assertEquals("lookup jdbc/missing", lookupFailureRequest.getAttribute("errorMessage"));

    DataSource dataSource = mock(DataSource.class);
    when(resourceResolver.lookupDataSource(context, "jdbc/test", containerWrapper))
        .thenReturn(dataSource);
    when(dataSource.getConnection()).thenThrow(new SQLException("broken"));

    MockHttpServletRequest sqlFailureRequest =
        new MockHttpServletRequest("GET", "/sql/recordset.ajax");
    sqlFailureRequest.addParameter("webapp", "app");
    sqlFailureRequest.addParameter("resource", "jdbc/test");
    sqlFailureRequest.addParameter("sql", "select 1");
    sqlFailureRequest.getSession(true);

    ModelAndView sqlFailure =
        controller.handleRequest(sqlFailureRequest, new MockHttpServletResponse());

    assertEquals("ajax/sql/recordset", sqlFailure.getViewName());
    assertEquals("sql failure broken", sqlFailureRequest.getAttribute("errorMessage"));
  }
}
