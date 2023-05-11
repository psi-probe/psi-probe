package com.googlecode.psiprobe.beans.stats.collectors;

import com.googlecode.psiprobe.beans.ContainerWrapperBean;
import com.googlecode.psiprobe.model.ApplicationResource;
import com.googlecode.psiprobe.model.DataSourceInfo;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Mark Lewis
 */
public class DatasourceStatsCollectorBean extends AbstractStatsCollectorBean {

    private static final String PREFIX_ESTABLISHED = "ds.est.";
    private static final String PREFIX_BUSY = "ds.busy.";

    private Log logger = LogFactory.getLog(DatasourceStatsCollectorBean.class);
    private ContainerWrapperBean containerWrapper;

    public ContainerWrapperBean getContainerWrapper() {
        return containerWrapper;
    }

    public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
        this.containerWrapper = containerWrapper;
    }

    public void collect() throws Exception {
        long currentTime = System.currentTimeMillis();
        if (containerWrapper == null) {
            logger.error("Cannot collect data source stats. Container wrapper is not set.");
        } else if (containerWrapper.getTomcatContainer() != null) {
            // if the TomcatContainer is null, fetching the datasources will fail with an NPE
            List dataSources = getContainerWrapper().getDataSources();
            for (int i = 0; i < dataSources.size(); i++) {
                ApplicationResource ds = (ApplicationResource) dataSources.get(i);
                String name = ds.getName();
                DataSourceInfo dsi = ds.getDataSourceInfo();
                int numEstablished = dsi.getEstablishedConnections();
                int numBusy = dsi.getBusyConnections();
                logger.trace("Collecting stats for datasource: " + name);
                buildAbsoluteStats(PREFIX_ESTABLISHED + name, numEstablished, currentTime);
                buildAbsoluteStats(PREFIX_BUSY + name, numBusy, currentTime);
            }
            logger.debug("datasource stats collected in " + (System.currentTimeMillis() - currentTime) + "ms");
        }
    }

    public void reset() throws Exception {
        if (containerWrapper == null) {
            logger.error("Cannot reset application stats. Container wrapper is not set.");
        } else if (containerWrapper.getTomcatContainer() != null) {
            // if the TomcatContainer is null, fetching the datasources will fail with an NPE
            List dataSources = getContainerWrapper().getDataSources();
            for (int i = 0; i < dataSources.size(); i++) {
                ApplicationResource ds = (ApplicationResource) dataSources.get(i);
                reset(ds.getName());
            }
        }
    }

    public void reset(String name) throws Exception {
        resetStats(PREFIX_ESTABLISHED + name);
        resetStats(PREFIX_BUSY + name);
    }

}
