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
import com.googlecode.psiprobe.model.ThreadPool;
import com.googlecode.psiprobe.model.RequestProcessor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Iterator;

/**
 * Creates the list of http connection thread pools.
 *
 * Author: Vlad Ilyushchenko
 */
public class StatusController extends TomcatContainerController {

    private ContainerListenerBean containerListenerBean;
    private boolean includeRequestProcessors = true;

    public ContainerListenerBean getContainerListenerBean() {
        return containerListenerBean;
    }

    public void setContainerListenerBean(ContainerListenerBean containerListenerBean) {
        this.containerListenerBean = containerListenerBean;
    }

    public boolean isIncludeRequestProcessors() {
        return includeRequestProcessors;
    }

    public void setIncludeRequestProcessors(boolean includeRequestProcessors) {
        this.includeRequestProcessors = includeRequestProcessors;
    }

    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean workerThreadNameSupported = false;
        List pools = containerListenerBean.getThreadPools(includeRequestProcessors);

        if (pools.size() > 0 && ((ThreadPool)pools.get(0)).getRequestProcessors().size() > 0) {
            workerThreadNameSupported = ((RequestProcessor)((ThreadPool)pools.get(0)).getRequestProcessors().get(0)).isWorkerThreadNameSupported();
        }
        
        return new ModelAndView(getViewName(), "pools", pools).addObject("workerThreadNameSupported", Boolean.valueOf(workerThreadNameSupported));
    }
}
