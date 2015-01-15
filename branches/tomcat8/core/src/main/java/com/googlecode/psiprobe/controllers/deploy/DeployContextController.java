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
package com.googlecode.psiprobe.controllers.deploy;

import com.googlecode.psiprobe.controllers.TomcatContainerController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * Forces Tomcat to install a pre-configured context name.
 *
 * @author Vlad Ilyushchenko
 */
public class DeployContextController extends TomcatContainerController {
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String contextName = ServletRequestUtils.getStringParameter(request, "context", null);

        if (contextName != null) {
            try {
                if (getContainerWrapper().getTomcatContainer().installContext(contextName)) {
                    request.setAttribute("successMessage", getMessageSourceAccessor().getMessage("probe.src.deploy.context.success", new Object[]{contextName}));
                } else {
                    request.setAttribute("errorMessage", getMessageSourceAccessor().getMessage("probe.src.deploy.context.failure", new Object[]{contextName}));
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }

        return new ModelAndView(new InternalResourceView(getViewName()));
    }
}
