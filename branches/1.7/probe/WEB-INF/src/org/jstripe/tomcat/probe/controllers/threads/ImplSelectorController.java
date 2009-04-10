/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 *  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 *  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.tomcat.probe.controllers.threads;

import org.jstripe.tomcat.probe.Utils;
import org.springframework.web.bind.RequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImplSelectorController extends AbstractController {
    private String impl1Controller;
    private String impl2Controller;

    public String getImpl1Controller() {
        return impl1Controller;
    }

    public void setImpl1Controller(String impl1Controller) {
        this.impl1Controller = impl1Controller;
    }

    public String getImpl2Controller() {
        return impl2Controller;
    }

    public void setImpl2Controller(String impl2Controller) {
        this.impl2Controller = impl2Controller;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {
        boolean forceOld = RequestUtils.getBooleanParameter(request, "forceold", false);
        if (!forceOld && Utils.isThreadingEnabled()) {
            return new ModelAndView(impl2Controller);
        } else {
            return new ModelAndView(impl1Controller);
        }
    }
}
