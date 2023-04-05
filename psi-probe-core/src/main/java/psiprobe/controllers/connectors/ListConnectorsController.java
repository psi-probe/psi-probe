/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.controllers.connectors;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.beans.ContainerListenerBean;
import psiprobe.controllers.AbstractTomcatContainerController;
import psiprobe.model.Connector;
import psiprobe.model.RequestProcessor;
import psiprobe.tools.TimeExpression;

/**
 * The Class ListConnectorsController.
 */
@Controller
public class ListConnectorsController extends AbstractTomcatContainerController {

  /** The container listener bean. */
  @Inject
  private ContainerListenerBean containerListenerBean;

  /** The include request processors. */
  private boolean includeRequestProcessors;

  /** The collection period. */
  private long collectionPeriod;

  /**
   * Gets the container listener bean.
   *
   * @return the container listener bean
   */
  public ContainerListenerBean getContainerListenerBean() {
    return containerListenerBean;
  }

  /**
   * Sets the container listener bean.
   *
   * @param containerListenerBean the new container listener bean
   */
  public void setContainerListenerBean(ContainerListenerBean containerListenerBean) {
    this.containerListenerBean = containerListenerBean;
  }

  /**
   * Gets the collection period.
   *
   * @return the collection period
   */
  public long getCollectionPeriod() {
    return collectionPeriod;
  }

  /**
   * Sets the collection period.
   *
   * @param collectionPeriod the new collection period
   */
  public void setCollectionPeriod(long collectionPeriod) {
    this.collectionPeriod = collectionPeriod;
  }

  /**
   * Sets the collection period using expression.
   *
   * @param collectionPeriod the new collection period using expression
   */
  @Value("${psiprobe.beans.stats.collectors.connector.period}")
  public void setCollectionPeriod(String collectionPeriod) {
    this.collectionPeriod = TimeExpression.inSeconds(collectionPeriod);
  }

  /**
   * Checks if is include request processors.
   *
   * @return true, if is include request processors
   */
  public boolean isIncludeRequestProcessors() {
    return includeRequestProcessors;
  }

  /**
   * Sets the include request processors.
   *
   * @param includeRequestProcessors the new include request processors
   */
  @Value("true")
  public void setIncludeRequestProcessors(boolean includeRequestProcessors) {
    this.includeRequestProcessors = includeRequestProcessors;
  }

  @RequestMapping(path = "/connectors.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    boolean workerThreadNameSupported = false;
    List<Connector> connectors = containerListenerBean.getConnectors(includeRequestProcessors);

    if (!connectors.isEmpty()) {
      List<RequestProcessor> reqProcs = connectors.get(0).getRequestProcessors();
      if (!reqProcs.isEmpty()) {
        RequestProcessor reqProc = reqProcs.get(0);
        workerThreadNameSupported = reqProc.isWorkerThreadNameSupported();
      }
    }

    return new ModelAndView(getViewName()).addObject("connectors", connectors)
        .addObject("workerThreadNameSupported", workerThreadNameSupported)
        .addObject("collectionPeriod", getCollectionPeriod());
  }

  @Value("connectors")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
