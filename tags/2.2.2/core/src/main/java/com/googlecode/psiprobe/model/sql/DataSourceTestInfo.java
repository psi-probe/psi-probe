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
package com.googlecode.psiprobe.model.sql;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A class to store data source test tool related data in a session attribute.
 * 
 * @author Andy Shapoval
 */
public class DataSourceTestInfo implements Serializable {
    public static final String DS_TEST_SESS_ATTR = "dataSourceTestData";

    List results = null;
    LinkedList queryHistory = new LinkedList();
    int maxRows = 0;
    int rowsPerPage = 0;
    int historySize = 0;

    public DataSourceTestInfo() {
    }

    public void addQueryToHistory(String sql) {
        queryHistory.remove(sql);
        queryHistory.addFirst(sql);

        while(historySize >= 0 && queryHistory.size() > historySize) {
            queryHistory.removeLast();
        }
    }

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public List getQueryHistory() {
        return queryHistory;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public int getHistorySize() {
        return historySize;
    }

    public void setHistorySize(int historySize) {
        this.historySize = historySize;
    }

}
