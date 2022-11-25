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
package psiprobe.controllers.logs;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.tools.logging.LogDestination;
import psiprobe.tools.logging.jdk.Jdk14HandlerAccessor;
import psiprobe.tools.logging.log4j.Log4JAppenderAccessor;
import psiprobe.tools.logging.log4j2.Log4J2AppenderAccessor;
import psiprobe.tools.logging.logback.LogbackAppenderAccessor;
import psiprobe.tools.logging.logback13.Logback13AppenderAccessor;
import psiprobe.tools.logging.slf4jlogback.TomcatSlf4jLogbackAppenderAccessor;
import psiprobe.tools.logging.slf4jlogback13.TomcatSlf4jLogback13AppenderAccessor;

/**
 * The Class ChangeLogLevelController.
 */
@Controller
public class ChangeLogLevelController extends AbstractLogHandlerController {

  @RequestMapping(path = "/adm/changeloglevel.ajax")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleLogFile(HttpServletRequest request, HttpServletResponse response,
      LogDestination logDest) throws Exception {

    String level = ServletRequestUtils.getRequiredStringParameter(request, "level");
    if (logDest.getValidLevels() != null
        && Arrays.asList(logDest.getValidLevels()).contains(level)) {

      if (logDest instanceof Log4JAppenderAccessor) {
        Log4JAppenderAccessor accessor = (Log4JAppenderAccessor) logDest;
        accessor.getLoggerAccessor().setLevel(level);
      } else if (logDest instanceof Log4J2AppenderAccessor) {
        Log4J2AppenderAccessor accessor = (Log4J2AppenderAccessor) logDest;
        accessor.getLoggerAccessor().setLevel(level);
      } else if (logDest instanceof Jdk14HandlerAccessor) {
        Jdk14HandlerAccessor accessor = (Jdk14HandlerAccessor) logDest;
        accessor.getLoggerAccessor().setLevel(level);
      } else if (logDest instanceof LogbackAppenderAccessor) {
        LogbackAppenderAccessor accessor = (LogbackAppenderAccessor) logDest;
        accessor.getLoggerAccessor().setLevel(level);
      } else if (logDest instanceof Logback13AppenderAccessor) {
        Logback13AppenderAccessor accessor = (Logback13AppenderAccessor) logDest;
        accessor.getLoggerAccessor().setLevel(level);
      } else if (logDest instanceof TomcatSlf4jLogbackAppenderAccessor) {
        TomcatSlf4jLogbackAppenderAccessor accessor = (TomcatSlf4jLogbackAppenderAccessor) logDest;
        accessor.getLoggerAccessor().setLevel(level);
      } else if (logDest instanceof TomcatSlf4jLogback13AppenderAccessor) {
        TomcatSlf4jLogback13AppenderAccessor accessor = (TomcatSlf4jLogback13AppenderAccessor) logDest;
        accessor.getLoggerAccessor().setLevel(level);
      }
    }
    return null;
  }

  @Value("")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
