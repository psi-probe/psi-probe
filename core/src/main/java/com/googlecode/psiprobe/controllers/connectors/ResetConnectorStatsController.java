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

import com.googlecode.psiprobe.beans.stats.collectors.ConnectorStatsCollectorBean;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class ResetConnectorStatsController.
 *
 * @author Mark Lewis
 */
public class ResetConnectorStatsController extends ParameterizableViewController {

  /** The collector bean. */
  ConnectorStatsCollectorBean collectorBean;

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

  /* (non-Javadoc)
   * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String connectorName = ServletRequestUtils.getRequiredStringParameter(request, "cn");
    collectorBean.reset(connectorName);
    return new ModelAndView(new RedirectView(request.getContextPath() + getViewName()));
  }

}
