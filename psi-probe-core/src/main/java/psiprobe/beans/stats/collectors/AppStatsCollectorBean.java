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

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.catalina.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.ServletContextAware;

import psiprobe.TomcatContainer;
import psiprobe.beans.ContainerWrapperBean;
import psiprobe.model.Application;
import psiprobe.tools.ApplicationUtils;
import psiprobe.tools.TimeExpression;

/**
 * Collects application statistics.
 */
public class AppStatsCollectorBean extends AbstractStatsCollectorBean
    implements ServletContextAware {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AppStatsCollectorBean.class);

  /** The container wrapper. */
  @Inject
  private ContainerWrapperBean containerWrapper;

  /** The servlet context. */
  @Inject
  private ServletContext servletContext;

  /** The self ignored. */
  private boolean selfIgnored;

  /**
   * Gets the container wrapper.
   *
   * @return the container wrapper
   */
  public ContainerWrapperBean getContainerWrapper() {
    return containerWrapper;
  }

  /**
   * Sets the container wrapper.
   *
   * @param containerWrapper the new container wrapper
   */
  public void setContainerWrapper(ContainerWrapperBean containerWrapper) {
    this.containerWrapper = containerWrapper;
  }

  /**
   * Checks if is self ignored.
   *
   * @return true, if is self ignored
   */
  public boolean isSelfIgnored() {
    return selfIgnored;
  }

  /**
   * Sets the self ignored.
   *
   * @param selfIgnored the new self ignored
   */
  @Value("${psiprobe.beans.stats.collectors.app.selfIgnored}")
  public void setSelfIgnored(boolean selfIgnored) {
    this.selfIgnored = selfIgnored;
  }

  /**
   * Gets the servlet context.
   *
   * @return the servlet context
   */
  protected ServletContext getServletContext() {
    return servletContext;
  }

  @Override
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  @Override
  public void collect() throws InterruptedException {

    long currentTime = System.currentTimeMillis();

    if (containerWrapper == null) {
      logger.error("Cannot collect application stats. Container wrapper is not set.");
    } else {
      TomcatContainer tomcatContainer = getContainerWrapper().getTomcatContainer();

      // check if the containerWtapper has been initialized
      if (tomcatContainer != null) {
        long totalReqDelta = 0;
        long totalErrDelta = 0;
        long totalAvgProcTime = 0;
        int participatingAppCount = 0;

        for (Context ctx : tomcatContainer.findContexts()) {
          if (ctx != null && ctx.getName() != null) {
            Application app = new Application();
            ApplicationUtils.collectApplicationServletStats(ctx, app);

            String appName = ctx.getName().isEmpty() ? "/" : ctx.getName();

            long reqDelta =
                buildDeltaStats("app.requests." + appName, app.getRequestCount(), currentTime);
            long errDelta = buildDeltaStats("app.errors." + appName, app.getErrorCount());
            long procTimeDelta =
                buildDeltaStats("app.proc_time." + appName, app.getProcessingTime(), currentTime);

            long avgProcTime = reqDelta == 0 ? 0 : procTimeDelta / reqDelta;
            buildAbsoluteStats("app.avg_proc_time." + appName, avgProcTime, currentTime);

            /*
             * make sure applications that did not serve any requests do not participate in average
             * response time equation thus diluting the value
             */
            if (reqDelta > 0 && !excludeFromTotal(ctx)) {
              totalReqDelta += reqDelta;
              totalErrDelta += errDelta;
              totalAvgProcTime += avgProcTime;
              participatingAppCount++;
            }
          }
        }
        // build totals for all applications
        buildAbsoluteStats("total.requests", totalReqDelta, currentTime);
        buildAbsoluteStats("total.errors", totalErrDelta, currentTime);
        buildAbsoluteStats("total.avg_proc_time",
            participatingAppCount == 0 ? 0 : totalAvgProcTime / participatingAppCount, currentTime);
      }
      logger.debug("app stats collected in {}ms", System.currentTimeMillis() - currentTime);
    }
  }

  /**
   * Exclude from total.
   *
   * @param ctx the ctx
   *
   * @return true, if successful
   */
  private boolean excludeFromTotal(Context ctx) {
    return isSelfIgnored() && getServletContext().equals(ctx.getServletContext());
  }

  /**
   * Reset.
   */
  public void reset() {
    if (containerWrapper == null) {
      logger.error("Cannot reset application stats. Container wrapper is not set.");
    } else {
      TomcatContainer tomcatContainer = getContainerWrapper().getTomcatContainer();
      if (tomcatContainer != null) {
        for (Context ctx : tomcatContainer.findContexts()) {
          if (ctx != null && ctx.getName() != null) {
            String appName = ctx.getName().isEmpty() ? "/" : ctx.getName();
            reset(appName);
          }
        }
      }
    }
    resetStats("total.requests");
    resetStats("total.errors");
    resetStats("total.avg_proc_time");
  }

  /**
   * Reset.
   *
   * @param appName the app name
   */
  public void reset(String appName) {
    resetStats("app.requests." + appName);
    resetStats("app.proc_time." + appName);
    resetStats("app.errors." + appName);
    resetStats("app.avg_proc_time." + appName);
  }

  /**
   * Sets the max series expression.
   *
   * @param period the period
   * @param span the span
   */
  public void setMaxSeries(@Value("${psiprobe.beans.stats.collectors.app.period}") long period,
      @Value("${psiprobe.beans.stats.collectors.app.span}") long span) {
    super.setMaxSeries((int) TimeExpression.dataPoints(period, span));
  }

}
