/**
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

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import psiprobe.model.sql.DataSourceTestInfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Retrieves a history list of executed queries from a session variable.
 */
public class QueryHistoryController extends ParameterizableViewController {

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    HttpSession sess = request.getSession(false);
    List<String> queryHistory = null;

    if (sess != null) {
      DataSourceTestInfo sessData =
          (DataSourceTestInfo) sess.getAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR);

      if (sessData != null) {
        queryHistory = sessData.getQueryHistory();
      }
    }

    return new ModelAndView(getViewName(), "queryHistory", queryHistory);
  }

}
