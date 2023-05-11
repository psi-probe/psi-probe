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
package psiprobe.model.sql;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class to store data source test tool related data in a session attribute.
 */
public class DataSourceTestInfo implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant DS_TEST_SESS_ATTR. */
  public static final String DS_TEST_SESS_ATTR = "dataSourceTestData";

  /** The results. */
  private List<Map<String, String>> results;

  /** The query history. */
  private final LinkedList<String> queryHistory;

  /** The max rows. */
  private int maxRows;

  /** The rows per page. */
  private int rowsPerPage;

  /** The history size. */
  private int historySize;

  /**
   * DataSourceTestInfo Constructor.
   */
  public DataSourceTestInfo() {
    queryHistory = new LinkedList<>();
  }

  /**
   * Adds the query to history.
   *
   * @param sql the sql
   */
  public void addQueryToHistory(String sql) {
    queryHistory.remove(sql);
    queryHistory.addFirst(sql);

    while (historySize >= 0 && queryHistory.size() > historySize) {
      queryHistory.removeLast();
    }
  }

  /**
   * Gets the results.
   *
   * @return the results
   */
  public List<Map<String, String>> getResults() {
    return results;
  }

  /**
   * Sets the results.
   *
   * @param results the results
   */
  public void setResults(List<Map<String, String>> results) {
    this.results = results;
  }

  /**
   * Gets the query history.
   *
   * @return the query history
   */
  public List<String> getQueryHistory() {
    return queryHistory;
  }

  /**
   * Gets the max rows.
   *
   * @return the max rows
   */
  public int getMaxRows() {
    return maxRows;
  }

  /**
   * Sets the max rows.
   *
   * @param maxRows the new max rows
   */
  public void setMaxRows(int maxRows) {
    this.maxRows = maxRows;
  }

  /**
   * Gets the rows per page.
   *
   * @return the rows per page
   */
  public int getRowsPerPage() {
    return rowsPerPage;
  }

  /**
   * Sets the rows per page.
   *
   * @param rowsPerPage the new rows per page
   */
  public void setRowsPerPage(int rowsPerPage) {
    this.rowsPerPage = rowsPerPage;
  }

  /**
   * Gets the history size.
   *
   * @return the history size
   */
  public int getHistorySize() {
    return historySize;
  }

  /**
   * Sets the history size.
   *
   * @param historySize the new history size
   */
  public void setHistorySize(int historySize) {
    this.historySize = historySize;
  }

}
