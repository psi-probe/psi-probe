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
package psiprobe.controllers.system;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.tools.TimeExpression;

/**
 * Creates an instance of OsInfoController.
 */
@Controller
public class OsInfoController extends BaseSysInfoController {

  @RequestMapping(path = "/adm/osinfo.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Value("osinfo")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

  /**
   * Sets the collection period by expression.
   *
   * @param collectionPeriod the new collection period by expression
   */
  @Value("${psiprobe.beans.stats.collectors.runtime.period}")
  public void setCollectionPeriod(String collectionPeriod) {
    super.setCollectionPeriod(TimeExpression.inSeconds(collectionPeriod));
  }

}
