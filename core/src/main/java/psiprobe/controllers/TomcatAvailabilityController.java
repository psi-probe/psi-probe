/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.controllers;

import org.apache.catalina.Context;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.beans.ContainerListenerBean;
import psiprobe.model.ApplicationResource;
import psiprobe.model.DataSourceInfo;
import psiprobe.model.TomcatTestReport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * "Quick check" controller.
 */
public class TomcatAvailabilityController extends AbstractTomcatContainerController {

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
  public ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    final long start = System.currentTimeMillis();
    TomcatTestReport tomcatTestReport = new TomcatTestReport();

    // check datasource status
    tomcatTestReport.setDatasourceUsageScore(0);

    boolean allContextsAvailable = true;
    if (getContainerWrapper().getResourceResolver().supportsPrivateResources()) {
      for (Context appContext : getContainerWrapper().getTomcatContainer().findContexts()) {
        allContextsAvailable = allContextsAvailable
            && getContainerWrapper().getTomcatContainer().getAvailable(appContext);
        
        List<ApplicationResource> applicationResources = getContainerWrapper().getResourceResolver()
            .getApplicationResources(appContext, getContainerWrapper());

        for (ApplicationResource appResource : applicationResources) {
          DataSourceInfo dsi = appResource.getDataSourceInfo();
          if (dsi != null && dsi.getBusyScore() > tomcatTestReport.getDatasourceUsageScore()) {
            tomcatTestReport.setContextName(appContext.getName());
            tomcatTestReport.setDatasourceUsageScore(dsi.getBusyScore());
            tomcatTestReport.setDataSourceName(appResource.getName());
          }
        }
      }

      tomcatTestReport.setWebappAvailabilityTest(allContextsAvailable
          ? TomcatTestReport.TEST_PASSED
          : TomcatTestReport.TEST_FAILED);

    } else {
      List<ApplicationResource> resources =
          getContainerWrapper().getResourceResolver().getApplicationResources();
      for (ApplicationResource resource : resources) {
        DataSourceInfo dsi = resource.getDataSourceInfo();
        if (dsi != null && dsi.getBusyScore() > tomcatTestReport.getDatasourceUsageScore()) {
          tomcatTestReport.setDatasourceUsageScore(dsi.getBusyScore());
          tomcatTestReport.setDataSourceName(resource.getName());
        }
      }
    }
    tomcatTestReport.setDatasourceTest(TomcatTestReport.TEST_PASSED);

    // try to allocate some memory
    String word = "hello";
    int count = TomcatTestReport.DEFAULT_MEMORY_SIZE / word.length();

    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      for (; count > 0; count--) {
        bos.write(word.getBytes());
      }
      tomcatTestReport.setMemoryTest(TomcatTestReport.TEST_PASSED);
    } catch (IOException e) {
      tomcatTestReport.setMemoryTest(TomcatTestReport.TEST_FAILED);
      logger.trace("", e);
    }

    // try to open some files
    File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    int fileCount = tomcatTestReport.getDefaultFileCount();
    List<File> files = new ArrayList<>();
    List<FileOutputStream> fileStreams = new ArrayList<>();

    try {
      for (; fileCount > 0; fileCount--) {
        File file = new File(tmpDir, "tctest_" + fileCount);
        FileOutputStream fos = new FileOutputStream(file);
        files.add(file);
        fileStreams.add(fos);
        fos.write("this is a test".getBytes());
      }
      tomcatTestReport.setFileTest(TomcatTestReport.TEST_PASSED);
    } catch (IOException e) {
      tomcatTestReport.setFileTest(TomcatTestReport.TEST_FAILED);
      logger.trace("", e);
    } finally {
      for (FileOutputStream fileStream : fileStreams) {
        try {
          fileStream.close();
        } catch (IOException e) {
          logger.trace("", e);
        }
      }
      for (File file : files) {
        file.delete();
      }
    }

    tomcatTestReport.setTestDuration(System.currentTimeMillis() - start);

    long maxServiceTime = 0;

    // check the maximum execution time
    /*
     * List pools = containerListenerBean.getThreadPools(); for (int iPool = 0; iPool <
     * pools.size(); iPool++) { ThreadPool threadPool = (ThreadPool) pools.get(iPool); List threads
     * = threadPool.getRequestProcessors(); for (int iThread = 0; iThread < threads.size();
     * iThread++) { RequestProcessor rp = (RequestProcessor) threads.get(iThread); if (rp.getStage()
     * == 3) { // // the request processor is in SERVICE state // maxServiceTime =
     * Math.max(maxServiceTime, rp.getProcessingTime()); } } }
     */

    tomcatTestReport.setMaxServiceTime(maxServiceTime);

    return new ModelAndView(getViewName(), "testReport", tomcatTestReport);
  }
}
