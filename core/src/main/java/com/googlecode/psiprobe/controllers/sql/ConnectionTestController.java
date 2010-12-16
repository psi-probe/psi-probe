/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.controllers.sql;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Verifies if a database connection can be established through a given
 * datasource. Displays basic information about the database.
 * 
 * @author Andy Shapoval
 */
public class ConnectionTestController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName, Context context, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resourceName = ServletRequestUtils.getStringParameter(request, "resource");
        DataSource dataSource = null;

        try {
            dataSource = getContainerWrapper().getResourceResolver().lookupDataSource(context, resourceName);
        } catch (NamingException e) {
            request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage("probe.src.dataSourceTest.resource.lookup.failure", new Object[]{resourceName}));
        }

        if (dataSource == null) {
            request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage("probe.src.dataSourceTest.resource.lookup.failure", new Object[]{resourceName}));
        } else {
            try {
                // TODO: use Spring's jdbc template?
                Connection conn = dataSource.getConnection();
                try {
                    DatabaseMetaData md = conn.getMetaData();

                    List dbMetaData = new ArrayList();

                    addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.dbProdName", md.getDatabaseProductName());
                    addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.dbProdVersion", md.getDatabaseProductVersion());
                    addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.jdbcDriverName", md.getDriverName());
                    addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.jdbcDriverVersion", md.getDriverVersion());
//                    addDbMetaDataEntry(dbMetaData, "probe.jsp.dataSourceTest.dbMetaData.jdbcVersion", String.valueOf(md.getJDBCMajorVersion()));

                    return new ModelAndView(getViewName(), "dbMetaData", dbMetaData);
                } finally {
                    conn.close();
                }
            } catch (SQLException e) {
                String message = getMessageSourceAccessor().getMessage("probe.src.dataSourceTest.connection.failure", new Object[] { e.getMessage() });
                logger.error(message, e);
                request.setAttribute("errorMessage", message);
            }
        }

        return new ModelAndView(getViewName());
    }

    protected boolean isContextOptional() {
        return true;
    }

    private void addDbMetaDataEntry(List list, String name, String value) {
        Map entry = new LinkedHashMap();
        entry.put("propertyName", getMessageSourceAccessor().getMessage(name));
        entry.put("propertyValue", value);
        list.add(entry);
    }

}
