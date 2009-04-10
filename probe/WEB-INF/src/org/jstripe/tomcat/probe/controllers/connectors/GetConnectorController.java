/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.controllers.connectors;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.RequestUtils;
import org.jstripe.tomcat.probe.beans.ContainerListenerBean;
import org.jstripe.tomcat.probe.model.ThreadPool;
import org.jstripe.tomcat.probe.controllers.TomcatContainerController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GetConnectorController extends TomcatContainerController {
    private ContainerListenerBean containerListenerBean;

    public ContainerListenerBean getContainerListenerBean() {
        return containerListenerBean;
    }

    public void setContainerListenerBean(ContainerListenerBean containerListenerBean) {
        this.containerListenerBean = containerListenerBean;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {
        String connectorName = RequestUtils.getStringParameter(request, "cn", null);
        ThreadPool connector = null;

        if (connectorName != null) {
            List pools = containerListenerBean.getThreadPools(false);
            for (int i = 0; i < pools.size(); i++) {
                ThreadPool p = (ThreadPool) pools.get(i);
                if (connectorName.equals(p.getName())) {
                    connector = p;
                    break;
                }
            }
        }

        return new ModelAndView(getViewName(), "pool", connector);
    }
}
