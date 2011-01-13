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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class SetupFollowController extends LogHandlerController {

    protected ModelAndView handleLogFile(HttpServletRequest request, HttpServletResponse response, LogDestination logDest) throws Exception {
        ModelAndView mv = new ModelAndView(getViewName());
        mv.addObject("validLogLevels", logDest.getValidLevels());
        mv.addObject("logLevel", logDest.getLevel());
        return mv;
    }
}
