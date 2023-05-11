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
package com.googlecode.psiprobe.controllers.connectors;

import com.googlecode.psiprobe.beans.ContainerListenerBean;
import com.googlecode.psiprobe.controllers.TomcatContainerController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class ListConnectorNamesController extends TomcatContainerController {
    private ContainerListenerBean containerListenerBean;
    private long collectionPeriod;

    public ContainerListenerBean getContainerListenerBean() {
        return containerListenerBean;
    }

    public void setContainerListenerBean(ContainerListenerBean containerListenerBean) {
        this.containerListenerBean = containerListenerBean;
    }

    public long getCollectionPeriod() {
        return collectionPeriod;
    }

    public void setCollectionPeriod(long collectionPeriod) {
        this.collectionPeriod = collectionPeriod;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView(getViewName())
                .addObject("names", containerListenerBean.getThreadPoolNames())
                .addObject("collectionPeriod", new Long(getCollectionPeriod()));
    }
}
