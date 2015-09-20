/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.controllers.connectors;

import com.googlecode.psiprobe.beans.ContainerListenerBean;
import com.googlecode.psiprobe.controllers.TomcatContainerController;
import com.googlecode.psiprobe.model.Connector;
import com.googlecode.psiprobe.model.RequestProcessor;

import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class ListConnectorsController extends TomcatContainerController {

  private ContainerListenerBean containerListenerBean;
  private boolean includeRequestProcessors;
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

  public boolean isIncludeRequestProcessors() {
    return includeRequestProcessors;
  }

  public void setIncludeRequestProcessors(boolean includeRequestProcessors) {
    this.includeRequestProcessors = includeRequestProcessors;
  }

  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    boolean workerThreadNameSupported = false;
    List<Connector> connectors = containerListenerBean.getConnectors(includeRequestProcessors);

    if (connectors.size() > 0) {
      List<RequestProcessor> reqProcs = connectors.get(0).getRequestProcessors();
      if (reqProcs.size() > 0) {
        RequestProcessor reqProc = reqProcs.get(0);
        workerThreadNameSupported = reqProc.isWorkerThreadNameSupported();
      }
    }

    return new ModelAndView(getViewName()).addObject("connectors", connectors)
        .addObject("workerThreadNameSupported", workerThreadNameSupported)
        .addObject("collectionPeriod", getCollectionPeriod());
  }

}
