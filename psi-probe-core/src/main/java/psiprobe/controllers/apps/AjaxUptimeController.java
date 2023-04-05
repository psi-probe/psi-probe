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

import java.lang.management.ManagementFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * The Class AjaxUptimeController.
 */
@Controller
public class AjaxUptimeController extends ParameterizableViewController {

  @GetMapping(path = "/uptime.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    long uptimeStartValue = ManagementFactory.getRuntimeMXBean().getStartTime();
    long uptime = System.currentTimeMillis() - uptimeStartValue;
    long uptimeDays = uptime / (1000 * 60 * 60 * 24);

    uptime = uptime % (1000 * 60 * 60 * 24);
    long uptimeHours = uptime / (1000 * 60 * 60);

    uptime = uptime % (1000 * 60 * 60);
    long uptimeMins = uptime / (1000 * 60);

    request.setAttribute("uptime_days", uptimeDays);
    request.setAttribute("uptime_hours", uptimeHours);
    request.setAttribute("uptime_mins", uptimeMins);

    return new ModelAndView(getViewName());
  }

  @Value("ajax/uptime")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
