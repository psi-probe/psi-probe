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

package psiprobe.controllers.connectors;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.beans.ContainerListenerBean;
import psiprobe.controllers.TomcatContainerController;
import psiprobe.model.Connector;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class GetConnectorController.
 *
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class GetConnectorController extends TomcatContainerController {

  /** The container listener bean. */
  private ContainerListenerBean containerListenerBean;

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

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String connectorName = ServletRequestUtils.getStringParameter(request, "cn", null);
    Connector connector = null;

    if (connectorName != null) {
      List<Connector> connectors = containerListenerBean.getConnectors(false);
      for (Connector conn : connectors) {
        if (connectorName.equals(conn.getProtocolHandler())) {
          connector = conn;
          break;
        }
      }
    }

    return new ModelAndView(getViewName(), "connector", connector);
  }

}
