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
package com.googlecode.psiprobe.controllers.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.tanukisoftware.wrapper.WrapperManager;

public class StopJvmController extends ParameterizableViewController {
    private int stopExitCode = 1;

    public int getStopExitCode() {
        return stopExitCode;
    }

    public void setStopExitCode(int stopExitCode) {
        this.stopExitCode = stopExitCode;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean done = false;
        try {
            Class.forName("org.tanukisoftware.wrapper.WrapperManager");
            logger.info("JVM is STOPPED by "+request.getRemoteAddr());
            WrapperManager.stop(stopExitCode);
            done = true;
        } catch (ClassNotFoundException e) {
            logger.info("WrapperManager not found. Do you have wrapper.jar in the classpath?");
        }
        return new ModelAndView(getViewName(), "done", Boolean.valueOf(done));
    }
}
