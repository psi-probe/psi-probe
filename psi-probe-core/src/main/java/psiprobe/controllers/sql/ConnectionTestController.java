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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractContextHandlerController;

/**
 * Verifies if a database connection can be established through a given datasource. Displays basic
 * information about the database.
 */
@Controller
public class ConnectionTestController extends AbstractContextHandlerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ConnectionTestController.class);

  @RequestMapping(path = "/sql/connection.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    String resourceName = ServletRequestUtils.getStringParameter(request, "resource");
    DataSource dataSource = null;

    try {
      dataSource = getContainerWrapper().getResourceResolver().lookupDataSource(context,
          resourceName, getContainerWrapper());
    } catch (NamingException e) {
      request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage(
          "probe.src.dataSourceTest.resource.lookup.failure", new Object[] {resourceName}));
      logger.trace("", e);
    }

    if (dataSource == null) {
      request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage(
          "probe.src.dataSourceTest.resource.lookup.failure", new Object[] {resourceName}));
    } else {
      try {
        // TODO: use Spring's jdbc template?
        try (Connection conn = dataSource.getConnection()) {
          DatabaseMetaData md = conn.getMetaData();

          List<Map<String, String>> dbMetaData = new ArrayList<>();

          addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.dbProdName",
              md.getDatabaseProductName());
          addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.dbProdVersion",
              md.getDatabaseProductVersion());
          addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.jdbcDriverName",
              md.getDriverName());
          addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.jdbcDriverVersion",
              md.getDriverVersion());
          addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.jdbcVersion",
              String.valueOf(md.getJDBCMajorVersion()));

          return new ModelAndView(getViewName(), "dbMetaData", dbMetaData);
        }
      } catch (SQLException e) {
        String message = getMessageSourceAccessor().getMessage(
            "probe.src.dataSourceTest.connection.failure", new Object[] {e.getMessage()});
        logger.error(message, e);
        request.setAttribute("errorMessage", message);
      }
    }

    return new ModelAndView(getViewName());
  }

  @Override
  protected boolean isContextOptional() {
    return true;
  }

  /**
   * Adds the db meta data entry.
   *
   * @param list the list
   * @param name the name
   * @param value the value
   */
  private void addDbMetaDataEntry(Collection<Map<String, String>> list, String name, String value) {
    Map<String, String> entry = new LinkedHashMap<>();
    entry.put("propertyName", getMessageSourceAccessor().getMessage(name));
    entry.put("propertyValue", value);
    list.add(entry);
  }

  @Value("ajax/sql/connection")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
