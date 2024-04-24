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
package psiprobe.controllers.apps;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.tools.TimeExpression;

/**
 * The Class GetApplicationSummaryController.
 */
@Controller
public class GetApplicationSummaryController extends BaseGetApplicationController {

  @RequestMapping(path = "/appsummary.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Value("appsummary")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

  /**
   * Sets the collection period by expression.
   *
   * @param collectionPeriod the new collection period by expression
   */
  @Value("${psiprobe.beans.stats.collectors.app.period}")
  public void setCollectionPeriod(String collectionPeriod) {
    super.setCollectionPeriod(TimeExpression.inSeconds(collectionPeriod));
  }

}
