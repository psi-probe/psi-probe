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
package com.googlecode.psiprobe.controllers.logs;

import com.googlecode.psiprobe.tools.logging.LogDestination;
import com.googlecode.psiprobe.tools.logging.jdk.Jdk14HandlerAccessor;
import com.googlecode.psiprobe.tools.logging.log4j.Log4JAppenderAccessor;
import com.googlecode.psiprobe.tools.logging.logback.LogbackAppenderAccessor;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Mark Lewis
 */
public class ChangeLogLevelController extends LogHandlerController {

    protected ModelAndView handleLogFile(HttpServletRequest request, HttpServletResponse response, LogDestination logDest) throws Exception {
        String level = ServletRequestUtils.getRequiredStringParameter(request, "level");
        if (logDest.getValidLevels() != null && Arrays.asList(logDest.getValidLevels()).contains(level)) {
            if (logDest instanceof Log4JAppenderAccessor) {
                Log4JAppenderAccessor accessor = (Log4JAppenderAccessor) logDest;
                accessor.getLoggerAccessor().setLevel(level);
            } else if (logDest instanceof Jdk14HandlerAccessor) {
                Jdk14HandlerAccessor accessor = (Jdk14HandlerAccessor) logDest;
                accessor.getLoggerAccessor().setLevel(level);
            } else if (logDest instanceof LogbackAppenderAccessor) {
                LogbackAppenderAccessor accessor = (LogbackAppenderAccessor) logDest;
                accessor.getLoggerAccessor().setLevel(level);
            }
        }
        return null;
    }

}
