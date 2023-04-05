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
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.model.sql.DataSourceTestInfo;
import psiprobe.tools.TimeExpression;

/**
 * Displays a view that allows for a database connectivity testing. Supplies default values to input
 * fields of the view.
 */
@Controller
public class DataSourceTestController extends ParameterizableViewController {

  /** The max rows. */
  private int maxRows;

  /** The rows per page. */
  private int rowsPerPage;

  /** The history size. */
  private int historySize;

  /** The replace pattern. */
  private String replacePattern;

  /** The collection period. */
  private long collectionPeriod;

  @RequestMapping(path = "/sql/datasourcetest.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    HttpSession sess = request.getSession(false);

    DataSourceTestInfo sessData = null;

    if (sess != null) {
      sessData = (DataSourceTestInfo) sess.getAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR);
    }

    String referer = request.getHeader("Referer");
    String backUrl;
    if (referer != null) {
      backUrl = referer.replaceAll(replacePattern, "");
    } else {
      backUrl = null;
    }

    return new ModelAndView(getViewName())
        .addObject("maxRows",
            String.valueOf(sessData == null ? getMaxRows() : sessData.getMaxRows()))
        .addObject("rowsPerPage",
            String.valueOf(sessData == null ? getRowsPerPage() : sessData.getRowsPerPage()))
        .addObject("historySize",
            String.valueOf(sessData == null ? getHistorySize() : sessData.getHistorySize()))
        .addObject("backURL", backUrl).addObject("collectionPeriod", getCollectionPeriod());
  }

  /**
   * Gets the collection period.
   *
   * @return the collection period
   */
  public long getCollectionPeriod() {
    return collectionPeriod;
  }

  /**
   * Sets the collection period.
   *
   * @param collectionPeriod the new collection period
   */
  public void setCollectionPeriod(long collectionPeriod) {
    this.collectionPeriod = collectionPeriod;
  }

  /**
   * Sets the collection period using expression.
   *
   * @param collectionPeriod the new collection period using expression
   */
  @Value("${psiprobe.beans.stats.collectors.connector.period}")
  public void setCollectionPeriod(String collectionPeriod) {
    this.collectionPeriod = TimeExpression.inSeconds(collectionPeriod);
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
  @Value("1000")
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
  @Value("50")
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
  @Value("30")
  public void setHistorySize(int historySize) {
    this.historySize = historySize;
  }

  /**
   * Gets the replace pattern.
   *
   * @return the replace pattern
   */
  public String getReplacePattern() {
    return replacePattern;
  }

  /**
   * Sets the replace pattern.
   *
   * @param replacePattern the new replace pattern
   */
  @Value("^http(s)?://[a-zA-Z\\-\\.0-9]+(:[0-9]+)?")
  public void setReplacePattern(String replacePattern) {
    this.replacePattern = replacePattern;
  }

  @Value("datasourcetest")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
