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
/*
fgetSchedulerFactoryBean * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import psiprobe.scheduler.jobs.AppStatsJobDetail;
import psiprobe.scheduler.jobs.ClusterStatsJobDetail;
import psiprobe.scheduler.jobs.ConnectorStatsJobDetail;
import psiprobe.scheduler.jobs.DatasourceStatsJobDetail;
import psiprobe.scheduler.jobs.MemoryStatsJobDetail;
import psiprobe.scheduler.jobs.RuntimeStatsJobDetail;
import psiprobe.scheduler.jobs.StatsSerializerJobDetail;
import psiprobe.scheduler.triggers.AppStatsTrigger;
import psiprobe.scheduler.triggers.ClusterStatsTrigger;
import psiprobe.scheduler.triggers.ConnectorStatsTrigger;
import psiprobe.scheduler.triggers.DatasourceStatsTrigger;
import psiprobe.scheduler.triggers.MemoryStatsTrigger;
import psiprobe.scheduler.triggers.RuntimeStatsTrigger;
import psiprobe.scheduler.triggers.StatsSerializerTrigger;

/**
 * The Class ProbeConfigScheduler.
 */
@Configuration
public class ProbeConfigScheduler {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ProbeConfigScheduler.class);

  /**
   * Gets the connector stats job detail.
   *
   * @return the connector stats job detail
   */
  @Bean(name = "connectorStatsJobDetail")
  public ConnectorStatsJobDetail getConnectorStatsJobDetail() {
    logger.debug("Instantiated connectorStatsJobDetail");
    return new ConnectorStatsJobDetail();
  }

  /**
   * Gets the cluster stats job detail.
   *
   * @return the cluster stats job detail
   */
  @Bean(name = "clusterStatsJobDetail")
  public ClusterStatsJobDetail getClusterStatsJobDetail() {
    logger.debug("Instantiated clusterStatsJobDetail");
    return new ClusterStatsJobDetail();
  }

  /**
   * Gets the memory stats job detail.
   *
   * @return the memory stats job detail
   */
  @Bean(name = "memoryStatsJobDetail")
  public MemoryStatsJobDetail getMemoryStatsJobDetail() {
    logger.debug("Instantiated memoryStatsJobDetail");
    return new MemoryStatsJobDetail();
  }

  /**
   * Gets the runtime stats job detail.
   *
   * @return the runtime stats job detail
   */
  @Bean(name = "runtimeStatsJobDetail")
  public RuntimeStatsJobDetail getRuntimeStatsJobDetail() {
    logger.debug("Instantiated runtimeStatsJobDetail");
    return new RuntimeStatsJobDetail();
  }

  /**
   * Gets the app stats job detail.
   *
   * @return the app stats job detail
   */
  @Bean(name = "appStatsJobDetail")
  public AppStatsJobDetail getAppStatsJobDetail() {
    logger.debug("Instantiated appStatsJobDetail");
    return new AppStatsJobDetail();
  }

  /**
   * Gets the datasource stats job detail.
   *
   * @return the datasource stats job detail
   */
  @Bean(name = "datasourceStatsJobDetail")
  public DatasourceStatsJobDetail getDatasourceStatsJobDetail() {
    logger.debug("Instantiated datasourceStatsJobDetail");
    return new DatasourceStatsJobDetail();
  }

  /**
   * Gets the stats serializer job detail.
   *
   * @return the stats serializer job detail
   */
  @Bean(name = "statsSerializerJobDetail")
  public StatsSerializerJobDetail getStatsSerializerJobDetail() {
    logger.debug("Instantiated statsSerializerJobDetail");
    return new StatsSerializerJobDetail();
  }

  /**
   * Gets the connector stats trigger.
   *
   * @return the connector stats trigger
   */
  @Bean(name = "connectorStatsTrigger")
  public ConnectorStatsTrigger getConnectorStatsTrigger() {
    logger.debug("Instantiated connectorStatsTrigger");
    ConnectorStatsTrigger trigger = new ConnectorStatsTrigger();
    trigger.setJobDetail(getConnectorStatsJobDetail().getObject());
    return trigger;
  }

  /**
   * Gets the cluster stats trigger.
   *
   * @return the cluster stats trigger
   */
  @Bean(name = "clusterStatsTrigger")
  public ClusterStatsTrigger getClusterStatsTrigger() {
    logger.debug("Instantiated clusterStatsTrigger");
    ClusterStatsTrigger trigger = new ClusterStatsTrigger();
    trigger.setJobDetail(getClusterStatsJobDetail().getObject());
    return trigger;
  }

  /**
   * Gets the memory stats trigger.
   *
   * @return the memory stats trigger
   */
  @Bean(name = "memoryStatsTrigger")
  public MemoryStatsTrigger getMemoryStatsTrigger() {
    logger.debug("Instantiated memoryStatsTrigger");
    MemoryStatsTrigger trigger = new MemoryStatsTrigger();
    trigger.setJobDetail(getMemoryStatsJobDetail().getObject());
    return trigger;
  }

  /**
   * Gets the runtime stats trigger.
   *
   * @return the runtime stats trigger
   */
  @Bean(name = "runtimeStatsTrigger")
  public RuntimeStatsTrigger getRuntimeStatsTrigger() {
    logger.debug("Instantiated runtimeStatsTrigger");
    RuntimeStatsTrigger trigger = new RuntimeStatsTrigger();
    trigger.setJobDetail(getRuntimeStatsJobDetail().getObject());
    return trigger;
  }

  /**
   * Gets the app stats trigger.
   *
   * @return the app stats trigger
   */
  @Bean(name = "appStatsTrigger")
  public AppStatsTrigger getAppStatsTrigger() {
    logger.debug("Instantiated appStatsTrigger");
    AppStatsTrigger trigger = new AppStatsTrigger();
    trigger.setJobDetail(getAppStatsJobDetail().getObject());
    return trigger;
  }

  /**
   * Gets the datasource stats trigger.
   *
   * @return the datasource stats trigger
   */
  @Bean(name = "datasourceStatsTrigger")
  public DatasourceStatsTrigger getDatasourceStatsTrigger() {
    logger.debug("Instantiated datasourceStatsTrigger");
    DatasourceStatsTrigger trigger = new DatasourceStatsTrigger();
    trigger.setJobDetail(getDatasourceStatsJobDetail().getObject());
    return trigger;
  }

  /**
   * Gets the stats serializer trigger.
   *
   * @return the stats serializer trigger
   */
  @Bean(name = "statsSerializerTrigger")
  public StatsSerializerTrigger getStatsSerializerTrigger() {
    logger.debug("Instantiated statsSerializerTrigger");
    StatsSerializerTrigger trigger = new StatsSerializerTrigger();
    trigger.setJobDetail(getStatsSerializerJobDetail().getObject());
    return trigger;
  }

  /**
   * Gets the scheduler factory bean.
   *
   * @param appStatsTrigger the app stats trigger
   * @param clusterStatsTrigger the cluster stats trigger
   * @param connectorStatsTrigger the connector stats trigger
   * @param datasourceStatsTrigger the datasource stats trigger
   * @param memoryStatsTrigger the memory stats trigger
   * @param runtimeStatsTrigger the runtime stats trigger
   * @param statsSerializerTrigger the stats serializer trigger
   *
   * @return the scheduler factory bean
   */
  @Bean(name = "scheduler")
  public SchedulerFactoryBean getSchedulerFactoryBean(@Autowired AppStatsTrigger appStatsTrigger,
      @Autowired ClusterStatsTrigger clusterStatsTrigger,
      @Autowired ConnectorStatsTrigger connectorStatsTrigger,
      @Autowired DatasourceStatsTrigger datasourceStatsTrigger,
      @Autowired MemoryStatsTrigger memoryStatsTrigger,
      @Autowired RuntimeStatsTrigger runtimeStatsTrigger,
      @Autowired StatsSerializerTrigger statsSerializerTrigger) {

    logger.debug("Instantiated scheduler");
    SchedulerFactoryBean bean = new SchedulerFactoryBean();

    // Add Triggers
    bean.setTriggers(appStatsTrigger.getObject(), clusterStatsTrigger.getObject(),
        connectorStatsTrigger.getObject(), datasourceStatsTrigger.getObject(),
        memoryStatsTrigger.getObject(), runtimeStatsTrigger.getObject(),
        statsSerializerTrigger.getObject());

    // Add Properties
    Properties properties = new Properties();
    properties.setProperty("org.quartz.scheduler.instanceName", "ProbeScheduler");
    properties.setProperty("org.quartz.threadPool.threadCount", "5");
    properties.setProperty("org.quartz.threadPool.threadNamePrefix", "Probe_Quartz");
    bean.setQuartzProperties(properties);

    return bean;
  }

}
