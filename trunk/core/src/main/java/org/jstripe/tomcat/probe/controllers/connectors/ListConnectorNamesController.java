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
package org.jstripe.tomcat.probe.controllers.connectors;

import org.jstripe.tomcat.probe.beans.ContainerListenerBean;
import org.jstripe.tomcat.probe.controllers.TomcatContainerController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListConnectorNamesController extends TomcatContainerController {
    private ContainerListenerBean containerListenerBean;

    public ContainerListenerBean getContainerListenerBean() {
        return containerListenerBean;
    }

    public void setContainerListenerBean(ContainerListenerBean containerListenerBean) {
        this.containerListenerBean = containerListenerBean;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView(getViewName(), "names", containerListenerBean.getThreadPoolNames());
    }
}
