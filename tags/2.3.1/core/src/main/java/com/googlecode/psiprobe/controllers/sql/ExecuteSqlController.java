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
import com.googlecode.psiprobe.model.sql.DataSourceTestInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

/**
 * Executes an SQL query through a given datasource to test database
 * connectivity. Displays results returned by the query.
 * 
 * @author Andy Shapoval
 */
public class ExecuteSqlController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName, Context context, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resourceName = ServletRequestUtils.getStringParameter(request, "resource");
        String sql = ServletRequestUtils.getStringParameter(request, "sql", null);

        if (sql == null || sql.equals("") || sql.trim().equals("")) {
            request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage("probe.src.dataSourceTest.sql.required"));

            return new ModelAndView(getViewName());
        }

        int maxRows = ServletRequestUtils.getIntParameter(request, "maxRows", 0);
        int rowsPerPage = ServletRequestUtils.getIntParameter(request, "rowsPerPage", 0);
        int historySize = ServletRequestUtils.getIntParameter(request, "historySize", 0);

        // store current option values and query history in a session attribute

        HttpSession sess = request.getSession();
        DataSourceTestInfo sessData = (DataSourceTestInfo) sess.getAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR);

        synchronized(sess) {
            if (sessData == null) {
                sessData = new DataSourceTestInfo();
                sess.setAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR, sessData);
            }

            sessData.setMaxRows(maxRows);
            sessData.setRowsPerPage(rowsPerPage);
            sessData.setHistorySize(historySize);
            sessData.addQueryToHistory(sql);
        }

        DataSource dataSource = null;

        try {
            dataSource = getContainerWrapper().getResourceResolver().lookupDataSource(context, resourceName);
        } catch (NamingException e) {
            request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage("probe.src.dataSourceTest.resource.lookup.failure", new Object[]{resourceName}));
        }

        if (dataSource == null) {
            request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage("probe.src.dataSourceTest.resource.lookup.failure", new Object[]{resourceName}));
        } else {
            List results = null;
            int rowsAffected = 0;

            try {
                // TODO: use Spring's jdbc template?
                Connection conn = dataSource.getConnection();

                try {
                    conn.setAutoCommit(true);
                    PreparedStatement stmt = conn.prepareStatement(sql);

                    try {
                        boolean hasResultSet = stmt.execute();

                        if (! hasResultSet) {
                            rowsAffected = stmt.getUpdateCount();
                        } else {
                            results = new ArrayList();
                            ResultSet rs = stmt.getResultSet();

                            try {
                                ResultSetMetaData metaData = rs.getMetaData();

                                while(rs.next() && (maxRows < 0 || results.size() < maxRows)) {
                                    Map record = new LinkedHashMap();

                                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                                        String value = rs.getString(i);

                                        if (rs.wasNull()) {
                                            value = getMessageSourceAccessor().getMessage("probe.src.dataSourceTest.sql.null");
                                        } else {
                                            value = HtmlUtils.htmlEscape(value);
                                        }

                                        // a work around for IE browsers bug of not displaying
                                        // a border around an empty table column

                                        if (value.equals("")) {
                                            value = "&nbsp;";
                                        }

                                        // Pad the keys of columns with existing labels so they are distinct
                                        String key = metaData.getColumnLabel(i);
                                        while (record.containsKey(key)) {
                                            key += " ";
                                        }
                                        record.put(HtmlUtils.htmlEscape(key), value);
                                    }

                                    results.add(record);
                                }
                            } finally {
                                rs.close();
                            }

                            rowsAffected = results.size();
                        }
                    } finally {
                        stmt.close();
                    }
                } finally {
                    conn.close();
                }

                // store the query results in the session attribute in order
                // to support a result set pagination feature without re-executing the query

                synchronized(sess) {
                    sessData.setResults(results);
                }

                ModelAndView mv = new ModelAndView(getViewName(), "results", results);
                mv.addObject("rowsAffected", String.valueOf(rowsAffected));
                mv.addObject("rowsPerPage", String.valueOf(rowsPerPage));

                return mv;
            } catch (SQLException e) {
                String message = getMessageSourceAccessor().getMessage("probe.src.dataSourceTest.sql.failure", new Object[] { e.getMessage() });
                logger.error(message, e);
                request.setAttribute("errorMessage", message);
            }
        }

        return new ModelAndView(getViewName());
    }

    protected boolean isContextOptional() {
        return true;
    }

}
