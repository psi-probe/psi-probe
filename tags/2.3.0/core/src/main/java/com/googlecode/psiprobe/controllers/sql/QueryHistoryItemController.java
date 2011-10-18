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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Retrieves a single query from a history list.
 * 
 * @author Andy Shapoval
 */
public class QueryHistoryItemController extends AbstractController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int sqlId = ServletRequestUtils.getIntParameter(request, "sqlId", -1);

        HttpSession sess = request.getSession(false);

        if (sess != null) {
            DataSourceTestInfo sessData = (DataSourceTestInfo) sess.getAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR);

            if (sessData != null) {
                List queryHistory = sessData.getQueryHistory();

                if (queryHistory != null) {
                    try {
                        String sql = (String) queryHistory.get(sqlId);
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().print(sql);
                    } catch (IndexOutOfBoundsException e) {
                        logger.error("Cannot find a query history entry for history item id = " + sqlId);
                    }
                }
            }
        }

        return null;
    }
}
