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

import com.googlecode.psiprobe.model.sql.DataSourceTestInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * Displays a view that allows for a database connectivity testing. Supplies
 * default values to input fields of the view.
 * 
 * @author Andy Shapoval
 */
public class DataSourceTestController extends ParameterizableViewController {
    private int maxRows;
    private int rowsPerPage;
    private int historySize;
    private String replacePattern;
    private long collectionPeriod;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession sess = request.getSession(false);

        DataSourceTestInfo sessData = null;

        if (sess != null) {
            sessData = (DataSourceTestInfo) sess.getAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR);
        }

        String referer = request.getHeader("Referer");
        String backURL;
        if (referer != null) {
            backURL = referer.replaceAll(replacePattern, "");
        } else {
            backURL = null;
        }

        return new ModelAndView(getViewName())
                .addObject("maxRows", String.valueOf(sessData == null ? getMaxRows() : sessData.getMaxRows()))
                .addObject("rowsPerPage", String.valueOf(sessData == null ? getRowsPerPage() : sessData.getRowsPerPage()))
                .addObject("historySize", String.valueOf(sessData == null ? getHistorySize() : sessData.getHistorySize()))
                .addObject("backURL", backURL)
                .addObject("collectionPeriod", new Long(getCollectionPeriod()));
    }

    public long getCollectionPeriod() {
        return collectionPeriod;
    }

    public void setCollectionPeriod(long collectionPeriod) {
        this.collectionPeriod = collectionPeriod;
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

    public String getReplacePattern() {
        return replacePattern;
    }

    public void setReplacePattern(String replacePattern) {
        this.replacePattern = replacePattern;
    }
}
