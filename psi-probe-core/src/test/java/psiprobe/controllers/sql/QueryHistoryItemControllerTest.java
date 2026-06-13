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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import psiprobe.model.sql.DataSourceTestInfo;

class QueryHistoryItemControllerTest {

  @Test
  void handleRequestWritesRequestedSqlHistoryItem() throws Exception {
    QueryHistoryItemController controller = new QueryHistoryItemController();
    MockHttpServletRequest request =
        new MockHttpServletRequest("GET", "/sql/queryHistoryItem.ajax");
    request.addParameter("sqlId", "1");

    DataSourceTestInfo info = new DataSourceTestInfo();
    info.setHistorySize(-1);
    info.addQueryToHistory("select 1");
    info.addQueryToHistory("select 2");
    request.getSession(true).setAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR, info);

    MockHttpServletResponse response = new MockHttpServletResponse();

    assertNull(controller.handleRequest(request, response));
    assertEquals("UTF-8", response.getCharacterEncoding());
    assertEquals("select 1", response.getContentAsString());
  }

  @Test
  void handleRequestIgnoresMissingSessionDataAndOutOfBoundsHistoryIndex() throws Exception {
    QueryHistoryItemController controller = new QueryHistoryItemController();
    MockHttpServletRequest request =
        new MockHttpServletRequest("GET", "/sql/queryHistoryItem.ajax");
    request.addParameter("sqlId", "5");

    DataSourceTestInfo info = new DataSourceTestInfo();
    info.setHistorySize(-1);
    info.addQueryToHistory("select 1");
    request.getSession(true).setAttribute(DataSourceTestInfo.DS_TEST_SESS_ATTR, info);

    MockHttpServletResponse response = new MockHttpServletResponse();

    assertNull(controller.handleRequest(request, response));
    assertEquals("", response.getContentAsString());
  }
}
