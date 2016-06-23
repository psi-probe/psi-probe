/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package psiprobe.controllers.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.model.sql.DataSourceTestInfo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Displays a result set cached in an attribute of HttpSession object to support result set
 * pagination feature without re-executing a query that created the result set.
 * 
 * @author Andy Shapoval
 */
public class CachedRecordSetController extends ParameterizableViewController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(CachedRecordSetController.class);

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    int rowsPerPage = ServletRequestUtils.getIntParameter(request, "rowsPerPage", 0);
    List<Map<String, String>> results = null;
    int rowsAffected = 0;
    HttpSession sess = request.getSession(false);

    if (sess == null) {
      request
          .setAttribute(
              "errorMessage",
              getMessageSourceAccessor().getMessage(
                  "probe.src.dataSourceTest.cachedResultSet.failure"));
      logger.error("Cannot retrieve a cached result set. Http session is NULL.");
    } else {
      DataSourceTestInfo sessData =
          (DataSourceTestInfo) sess.getAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR);

      if (sessData == null) {
        request.setAttribute(
            "errorMessage",
            getMessageSourceAccessor().getMessage(
                "probe.src.dataSourceTest.cachedResultSet.failure"));
        logger.error("Cannot retrieve a cached result set. {} session attribute is NULL.",
            DataSourceTestInfo.DS_TEST_SESS_ATTR);
      } else {
        synchronized (sess) {
          sessData.setRowsPerPage(rowsPerPage);
        }

        results = sessData.getResults();

        if (results == null) {
          request.setAttribute(
              "errorMessage",
              getMessageSourceAccessor().getMessage(
                  "probe.src.dataSourceTest.cachedResultSet.failure"));
          logger.error("Cached results set is NULL.");
        } else {
          rowsAffected = results.size();
        }
      }
    }

    ModelAndView mv = new ModelAndView(getViewName(), "results", results);
    mv.addObject("rowsAffected", String.valueOf(rowsAffected));
    mv.addObject("rowsPerPage", String.valueOf(rowsPerPage));

    return mv;
  }

}
