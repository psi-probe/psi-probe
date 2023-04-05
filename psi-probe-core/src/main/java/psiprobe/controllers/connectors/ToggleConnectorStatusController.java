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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

import psiprobe.beans.ContainerListenerBean;
import psiprobe.beans.stats.collectors.ConnectorStatsCollectorBean;

/**
 * The Class to use toggle connector status, like STATED, STOPPED.
 */
@Controller
public class ToggleConnectorStatusController extends ParameterizableViewController {

  /** The static logger. */
  private static final Logger logger =
      LoggerFactory.getLogger(ToggleConnectorStatusController.class);

  /** The collector bean. */
  @Inject
  private ConnectorStatsCollectorBean collectorBean;

  /** The container listener. */
  @Inject
  private ContainerListenerBean containerListener;

  /**
   * Gets the collector bean.
   *
   * @return the collector bean
   */
  public ConnectorStatsCollectorBean getCollectorBean() {
    return collectorBean;
  }

  /**
   * Sets the collector bean.
   *
   * @param collectorBean the new collector bean
   */
  public void setCollectorBean(ConnectorStatsCollectorBean collectorBean) {
    this.collectorBean = collectorBean;
  }

  @RequestMapping(path = "/app/connectorStatus.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String connectorName = ServletRequestUtils.getRequiredStringParameter(request, "cn");

    String port = ServletRequestUtils.getRequiredStringParameter(request, "port");

    String operation = ServletRequestUtils.getRequiredStringParameter(request, "operation");

    containerListener.toggleConnectorStatus(operation, port);

    logger.info("Connector status toggled for {}", connectorName);
    return new ModelAndView(new RedirectView(request.getContextPath() + getViewName()));
  }

  @Value("/connectors.htm")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
