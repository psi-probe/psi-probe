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
package psiprobe.beans.stats.collectors;

import jakarta.inject.Inject;

import org.springframework.beans.factory.annotation.Value;

import psiprobe.beans.ContainerListenerBean;
import psiprobe.model.Connector;
import psiprobe.tools.TimeExpression;

/**
 * The Class ConnectorStatsCollectorBean.
 */
public class ConnectorStatsCollectorBean extends AbstractStatsCollectorBean {

  /** The listener bean. */
  @Inject
  private ContainerListenerBean listenerBean;

  /**
   * Gets the listener bean.
   *
   * @return the listener bean
   */
  public ContainerListenerBean getListenerBean() {
    return listenerBean;
  }

  /**
   * Sets the listener bean.
   *
   * @param listenerBean the new listener bean
   */
  public void setListenerBean(ContainerListenerBean listenerBean) {
    this.listenerBean = listenerBean;
  }

  @Override
  public void collect() throws Exception {
    for (Connector connector : listenerBean.getConnectors(false)) {
      String statName = "stat.connector." + connector.getProtocolHandler();
      buildDeltaStats(statName + ".requests", connector.getRequestCount());
      buildDeltaStats(statName + ".errors", connector.getErrorCount());
      buildDeltaStats(statName + ".sent", connector.getBytesSent());
      buildDeltaStats(statName + ".received", connector.getBytesReceived());
      buildDeltaStats(statName + ".proc_time", connector.getProcessingTime());
    }
  }

  /**
   * Reset.
   *
   * @throws Exception the exception
   */
  public void reset() throws Exception {
    for (Connector connector : listenerBean.getConnectors(false)) {
      reset(connector.getProtocolHandler());
    }
  }

  /**
   * Reset.
   *
   * @param connectorName the connector name
   */
  public void reset(String connectorName) {
    String statName = "stat.connector." + connectorName;
    resetStats(statName + ".requests");
    resetStats(statName + ".errors");
    resetStats(statName + ".sent");
    resetStats(statName + ".received");
    resetStats(statName + ".proc_time");
  }

  /**
   * Sets the max series expression.
   *
   * @param period the period
   * @param span the span
   */
  public void setMaxSeries(
      @Value("${psiprobe.beans.stats.collectors.connector.period}") long period,
      @Value("${psiprobe.beans.stats.collectors.connector.span}") long span) {
    super.setMaxSeries((int) TimeExpression.dataPoints(period, span));
  }

}
