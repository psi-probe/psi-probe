/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.scheduler.jobs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 * The Class ConnectorStatsJobDetail.
 */
public class ClusterStatsJobDetail extends MethodInvokingJobDetailFactoryBean {

  @Override
  @Value("false")
  public void setConcurrent(boolean concurrent) {
    super.setConcurrent(concurrent);
  }

  @Override
  @Value("clusterStatsCollector")
  public void setTargetBeanName(String targetBeanName) {
    super.setTargetBeanName(targetBeanName);
  }

  @Override
  @Value("collect")
  public void setTargetMethod(String targetMethod) {
    super.setTargetMethod(targetMethod);
  }

}
