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
package com.googlecode.psiprobe.beans.stats.collectors;

import com.googlecode.psiprobe.beans.ContainerListenerBean;
import com.googlecode.psiprobe.model.Connector;
import java.util.Iterator;
import java.util.List;

public class ConnectorStatsCollectorBean extends AbstractStatsCollectorBean {

    private ContainerListenerBean listenerBean;

    public ContainerListenerBean getListenerBean() {
        return listenerBean;
    }

    public void setListenerBean(ContainerListenerBean listenerBean) {
        this.listenerBean = listenerBean;
    }

    public void collect() throws Exception {
        List connectors = listenerBean.getConnectors(false);
        for (Iterator it = connectors.iterator(); it.hasNext();) {
            Connector connector = (Connector) it.next();
            String statName = "stat.connector." + connector.getName();
            buildDeltaStats(statName + ".requests", connector.getRequestCount());
            buildDeltaStats(statName + ".errors", connector.getErrorCount());
            buildDeltaStats(statName + ".sent", connector.getBytesSent());
            buildDeltaStats(statName + ".received", connector.getBytesReceived());
            buildDeltaStats(statName + ".proc_time", connector.getProcessingTime());
        }
    }

    public void reset() throws Exception {
        List connectors = listenerBean.getConnectors(false);
        for (Iterator it = connectors.iterator(); it.hasNext();) {
            Connector connector = (Connector) it.next();
            reset(connector.getName());
        }
    }

    public void reset(String connectorName) {
        String statName = "stat.connector." + connectorName;
        resetStats(statName + ".requests");
        resetStats(statName + ".errors");
        resetStats(statName + ".sent");
        resetStats(statName + ".received");
        resetStats(statName + ".proc_time");
    }

}
