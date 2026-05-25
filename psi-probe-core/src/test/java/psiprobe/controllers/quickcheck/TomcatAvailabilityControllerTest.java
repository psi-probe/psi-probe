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
package psiprobe.controllers.quickcheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codebox.bean.JavaBeanTester;

import java.util.List;

import org.apache.catalina.Context;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.TomcatContainer;
import psiprobe.beans.ContainerWrapperBean;
import psiprobe.beans.ResourceResolver;
import psiprobe.model.ApplicationResource;
import psiprobe.model.DataSourceInfo;
import psiprobe.model.TomcatTestReport;

/**
 * The Class TomcatAvailabilityControllerTest.
 */
class TomcatAvailabilityControllerTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(TomcatAvailabilityController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  /**
   * Javabean tester xml.
   */
  @Test
  void javabeanTesterXml() {
    JavaBeanTester.builder(TomcatAvailabilityXmlController.class)
        .skip("applicationContext", "supportedMethods").test();
  }

  @Test
  void handleRequestBuildsReportUsingPrivateResources() throws Exception {
    TomcatAvailabilityController controller = new TomcatAvailabilityController();
    controller.setViewName("quickcheck");

    ContainerWrapperBean containerWrapper = mock(ContainerWrapperBean.class);
    ResourceResolver resourceResolver = mock(ResourceResolver.class);
    TomcatContainer tomcatContainer = mock(TomcatContainer.class);
    Context firstContext = mock(Context.class);
    Context secondContext = mock(Context.class);

    when(containerWrapper.getResourceResolver()).thenReturn(resourceResolver);
    when(containerWrapper.getTomcatContainer()).thenReturn(tomcatContainer);
    when(resourceResolver.supportsPrivateResources()).thenReturn(true);
    when(tomcatContainer.findContexts()).thenReturn(List.of(firstContext, secondContext));
    when(firstContext.getName()).thenReturn("/app1");
    when(secondContext.getName()).thenReturn("/app2");
    when(tomcatContainer.getAvailable(firstContext)).thenReturn(true);
    when(tomcatContainer.getAvailable(secondContext)).thenReturn(false);

    ApplicationResource firstResource = new ApplicationResource();
    firstResource.setName("jdbc/app1");
    DataSourceInfo firstInfo = new DataSourceInfo();
    firstInfo.setMaxConnections(100);
    firstInfo.setBusyConnections(10);
    firstResource.setDataSourceInfo(firstInfo);

    ApplicationResource secondResource = new ApplicationResource();
    secondResource.setName("jdbc/app2");
    DataSourceInfo secondInfo = new DataSourceInfo();
    secondInfo.setMaxConnections(100);
    secondInfo.setBusyConnections(70);
    secondResource.setDataSourceInfo(secondInfo);

    when(resourceResolver.getApplicationResources(firstContext, containerWrapper))
        .thenReturn(List.of(firstResource));
    when(resourceResolver.getApplicationResources(secondContext, containerWrapper))
        .thenReturn(List.of(secondResource));

    controller.setContainerWrapper(containerWrapper);

    ModelAndView modelAndView = controller.handleRequest(
        new MockHttpServletRequest("GET", "/quickcheck.htm"), new MockHttpServletResponse());

    assertEquals("quickcheck", modelAndView.getViewName());
    TomcatTestReport report = (TomcatTestReport) modelAndView.getModel().get("testReport");
    assertEquals("/app2", report.getContextName());
    assertEquals("jdbc/app2", report.getDataSourceName());
    assertEquals(70, report.getDatasourceUsageScore());
    assertEquals(TomcatTestReport.TEST_PASSED, report.getDatasourceTest());
    assertEquals(TomcatTestReport.TEST_PASSED, report.getMemoryTest());
    assertEquals(TomcatTestReport.TEST_PASSED, report.getFileTest());
    assertEquals(TomcatTestReport.TEST_FAILED, report.getWebappAvailabilityTest());
  }

  @Test
  void handleRequestBuildsReportUsingGlobalResourcesWhenPrivateResourcesUnsupported()
      throws Exception {
    TomcatAvailabilityXmlController controller = new TomcatAvailabilityXmlController();
    controller.setViewName("quickcheck.xml");

    ContainerWrapperBean containerWrapper = mock(ContainerWrapperBean.class);
    ResourceResolver resourceResolver = mock(ResourceResolver.class);

    when(containerWrapper.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.supportsPrivateResources()).thenReturn(false);

    ApplicationResource resource = new ApplicationResource();
    resource.setName("jdbc/global");
    DataSourceInfo info = new DataSourceInfo();
    info.setMaxConnections(100);
    info.setBusyConnections(25);
    resource.setDataSourceInfo(info);
    when(resourceResolver.getApplicationResources()).thenReturn(List.of(resource));

    controller.setContainerWrapper(containerWrapper);

    ModelAndView modelAndView = controller.handleRequest(
        new MockHttpServletRequest("GET", "/quickcheck.xml.htm"), new MockHttpServletResponse());

    assertEquals("quickcheck.xml", modelAndView.getViewName());
    TomcatTestReport report = (TomcatTestReport) modelAndView.getModel().get("testReport");
    assertEquals("jdbc/global", report.getDataSourceName());
    assertEquals(25, report.getDatasourceUsageScore());
    assertEquals(TomcatTestReport.TEST_UNKNOWN, report.getWebappAvailabilityTest());
    assertNull(report.getContextName());
  }

}
