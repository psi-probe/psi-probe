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

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import psiprobe.model.sql.DataSourceTestInfo;

/**
 * Retrieves a single query from a history list.
 */
@Controller
public class QueryHistoryItemController extends AbstractController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(QueryHistoryItemController.class);

  @RequestMapping(path = "/sql/queryHistoryItem.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    int sqlId = ServletRequestUtils.getIntParameter(request, "sqlId", -1);

    HttpSession sess = request.getSession(false);

    if (sess != null) {
      DataSourceTestInfo sessData =
          (DataSourceTestInfo) sess.getAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR);

      if (sessData != null) {
        List<String> queryHistory = sessData.getQueryHistory();

        if (queryHistory != null) {
          try {
            String sql = queryHistory.get(sqlId);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().print(sql);
          } catch (IndexOutOfBoundsException e) {
            logger.error("Cannot find a query history entry for history item id = {}", sqlId);
            logger.trace("", e);
          }
        }
      }
    }

    return null;
  }

}
