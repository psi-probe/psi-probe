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
package psiprobe;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import psiprobe.beans.stats.collectors.AppStatsCollectorBean;
import psiprobe.beans.stats.collectors.ClusterStatsCollectorBean;
import psiprobe.beans.stats.collectors.ConnectorStatsCollectorBean;
import psiprobe.beans.stats.collectors.DatasourceStatsCollectorBean;
import psiprobe.beans.stats.collectors.JvmMemoryStatsCollectorBean;
import psiprobe.beans.stats.collectors.RuntimeStatsCollectorBean;
import psiprobe.beans.stats.listeners.MemoryPoolMailingListener;
import psiprobe.beans.stats.listeners.StatsCollectionListener;
import psiprobe.beans.stats.providers.ConnectorSeriesProvider;
import psiprobe.beans.stats.providers.MultipleSeriesProvider;
import psiprobe.beans.stats.providers.StandardSeriesProvider;
import psiprobe.model.stats.StatsCollection;

/**
 * The Class ProbeConfigStats.
 */
@Configuration
public class ProbeConfigStats {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ProbeConfigStats.class);

  /**
   * Gets the stats collection.
   *
   * @return the stats collection
   */
  @Bean(name = "statsCollection")
  public StatsCollection getStatsCollection() {
    logger.debug("Instantiated statsCollection");
    return new StatsCollection();
  }

  /**
   * Gets the connector stats collector bean.
   *
   * @return the connector stats collector bean
   */
  @Bean(name = "connectorStatsCollector")
  public ConnectorStatsCollectorBean getConnectorStatsCollectorBean() {
    logger.debug("Instantiated connectorStatsCollector");
    return new ConnectorStatsCollectorBean();
  }

  /**
   * Gets the cluster stats collector bean.
   *
   * @return the cluster stats collector bean
   */
  @Bean(name = "clusterStatsCollector")
  public ClusterStatsCollectorBean getClusterStatsCollectorBean() {
    logger.debug("Instantiated clusterStatsCollector");
    return new ClusterStatsCollectorBean();
  }

  /**
   * Gets the runtime stats collector bean.
   *
   * @return the runtime stats collector bean
   */
  @Bean(name = "runtimeStatsCollector")
  public RuntimeStatsCollectorBean getRuntimeStatsCollectorBean() {
    logger.debug("Instantiated runtimeStatsCollector");
    return new RuntimeStatsCollectorBean();
  }

  /**
   * Gets the app stats collector bean.
   *
   * @return the app stats collector bean
   */
  @Bean(name = "appStatsCollector")
  public AppStatsCollectorBean getAppStatsCollectorBean() {
    logger.debug("Instantiated appStatsCollector");
    return new AppStatsCollectorBean();
  }

  /**
   * Gets the jvm memory stats collector bean.
   *
   * @return the jvm memory stats collector bean
   */
  @Bean(name = "memoryStatsCollector")
  public JvmMemoryStatsCollectorBean getJvmMemoryStatsCollectorBean() {
    logger.debug("Instantiated memoryStatsCollector");
    return new JvmMemoryStatsCollectorBean();
  }

  /**
   * Gets the datasource stats collector bean.
   *
   * @return the datasource stats collector bean
   */
  @Bean(name = "datasourceStatsCollector")
  public DatasourceStatsCollectorBean getDatasourceStatsCollectorBean() {
    logger.debug("Instantiated datasourceStatsCollector");
    return new DatasourceStatsCollectorBean();
  }

  /**
   * Gets the memory pool mailing listener.
   *
   * @return the memory pool mailing listener
   */
  @Bean(name = "listeners")
  public List<StatsCollectionListener> getMemoryPoolMailingListener() {
    logger.debug("Instantiated listeners");
    List<StatsCollectionListener> list = new ArrayList<>();
    list.add(new MemoryPoolMailingListener());
    return list;
  }

  /**
   * Gets the connector series provider.
   *
   * @return the connector series provider
   */
  @Bean(name = "rcn")
  public ConnectorSeriesProvider getConnectorSeriesProvider() {
    logger.debug("Instantiated rcn");
    return new ConnectorSeriesProvider();
  }

  /**
   * Gets the cl traffic.
   *
   * @return the cl traffic
   */
  @Bean(name = "cl_traffic")
  public StandardSeriesProvider getClTraffic() {
    logger.debug("Instantiated cl_traffic");
    List<String> list = new ArrayList<>();
    list.add("cluster.sent");
    list.add("cluster.received");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the cl request.
   *
   * @return the cl request
   */
  @Bean(name = "cl_request")
  public StandardSeriesProvider getClRequest() {
    logger.debug("Instantiated cl_request");
    List<String> list = new ArrayList<>();
    list.add("cluster.req.sent");
    list.add("cluster.req.received");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the connector.
   *
   * @return the connector
   */
  @Bean(name = "connector")
  public StandardSeriesProvider getConnector() {
    logger.debug("Instantiated connector");
    List<String> list = new ArrayList<>();
    list.add("stat.connector.{0}.requests");
    list.add("stat.connector.{0}.errors");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the traffic.
   *
   * @return the traffic
   */
  @Bean(name = "traffic")
  public StandardSeriesProvider getTraffic() {
    logger.debug("Instantiated traffic");
    List<String> list = new ArrayList<>();
    list.add("stat.connector.{0}.sent");
    list.add("stat.connector.{0}.received");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the connector proc time.
   *
   * @return the connector proc time
   */
  @Bean(name = "connector_proc_time")
  public StandardSeriesProvider getConnectorProcTime() {
    logger.debug("Instantiated connector_proc_time");
    List<String> list = new ArrayList<>();
    list.add("stat.connector.{0}.proc_time");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the memory usage.
   *
   * @return the memory usage
   */
  @Bean(name = "memory_usage")
  public StandardSeriesProvider getMemoryUsage() {
    logger.debug("Instantiated memory_usage");
    List<String> list = new ArrayList<>();
    list.add("memory.pool.{0}");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the os memory.
   *
   * @return the os memory
   */
  @Bean(name = "os_memory")
  public StandardSeriesProvider getOsMemory() {
    logger.debug("Instantiated os_memory");
    List<String> list = new ArrayList<>();
    list.add("os.memory.physical");
    list.add("os.memory.committed");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the swap usage.
   *
   * @return the swap usage
   */
  @Bean(name = "swap_usage")
  public StandardSeriesProvider getSwapUsage() {
    logger.debug("Instantiated swap_usage");
    List<String> list = new ArrayList<>();
    list.add("os.memory.swap");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the cpu usage.
   *
   * @return the cpu usage
   */
  @Bean(name = "cpu_usage")
  public StandardSeriesProvider getCpuUsage() {
    logger.debug("Instantiated cpu_usage");
    List<String> list = new ArrayList<>();
    list.add("os.cpu");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the fd usage.
   *
   * @return the fd usage
   */
  @Bean(name = "fd_usage")
  public StandardSeriesProvider getFdUsage() {
    logger.debug("Instantiated fd_usage");
    List<String> list = new ArrayList<>();
    list.add("os.fd.open");
    list.add("os.fd.max");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the app req.
   *
   * @return the app req
   */
  @Bean(name = "app_req")
  public StandardSeriesProvider getAppReq() {
    logger.debug("Instantiated app_req");
    List<String> list = new ArrayList<>();
    list.add("app.requests.{0}");
    list.add("app.errors.{0}");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the app avg proc time.
   *
   * @return the app avg proc time
   */
  @Bean(name = "app_avg_proc_time")
  public StandardSeriesProvider getAppAvgProcTime() {
    logger.debug("Instantiated app_avg_proc_time");
    List<String> list = new ArrayList<>();
    list.add("app.avg_proc_time.{0}");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the total avg proc time.
   *
   * @return the total avg proc time
   */
  @Bean(name = "total_avg_proc_time")
  public StandardSeriesProvider getTotalAvgProcTime() {
    logger.debug("Instantiated total_avg_proc_time");
    List<String> list = new ArrayList<>();
    list.add("total.avg_proc_time");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the total req.
   *
   * @return the total req
   */
  @Bean(name = "total_req")
  public StandardSeriesProvider getTotalReq() {
    logger.debug("Instantiated total_req");
    List<String> list = new ArrayList<>();
    list.add("total.requests");
    list.add("total.errors");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the datasource usage.
   *
   * @return the datasource usage
   */
  @Bean(name = "datasource_usage")
  public StandardSeriesProvider getDatasourceUsage() {
    logger.debug("Instantiated datasource_usage");
    List<String> list = new ArrayList<>();
    list.add("ds.est.{0}");
    list.add("ds.busy.{0}");

    StandardSeriesProvider provider = new StandardSeriesProvider();
    provider.setStatNames(list);
    return provider;
  }

  /**
   * Gets the all app avg proc time.
   *
   * @return the all app avg proc time
   */
  @Bean(name = "all_app_avg_proc_time")
  public MultipleSeriesProvider getAllAppAvgProcTime() {
    logger.debug("Instantiated all_app_avg_proc_time");
    MultipleSeriesProvider provider = new MultipleSeriesProvider();
    provider.setMovingAvgFrame(10);
    provider.setStatNamePrefix("app.avg_proc_time.");
    provider.setTop(4);
    return provider;
  }

  /**
   * Gets the all app req.
   *
   * @return the all app req
   */
  @Bean(name = "all_app_req")
  public MultipleSeriesProvider getAllAppReq() {
    logger.debug("Instantiated all_app_req");
    MultipleSeriesProvider provider = new MultipleSeriesProvider();
    provider.setMovingAvgFrame(10);
    provider.setStatNamePrefix("app.requests.");
    provider.setTop(4);
    return provider;
  }

}
