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
package psiprobe.scheduler.triggers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import psiprobe.tools.TimeExpression;

/**
 * The Class AppStatsTrigger.
 */
public class AppStatsTrigger extends CronTriggerFactoryBean {

  /**
   * Sets the cron expression.
   *
   * @param periodExpression the period expression
   * @param phaseExpression the phase expression
   */
  @Autowired
  public void setCronExpression(
      @Value("${psiprobe.beans.stats.collectors.app.period}") String periodExpression,
      @Value("${psiprobe.beans.stats.collectors.app.phase}") String phaseExpression) {
    super.setCronExpression(TimeExpression.cronExpression(periodExpression, phaseExpression));
  }

}
