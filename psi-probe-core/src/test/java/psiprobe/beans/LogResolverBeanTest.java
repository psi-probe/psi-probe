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
package psiprobe.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import psiprobe.tools.logging.FileLogAccessor;
import psiprobe.tools.logging.LogDestination;

class LogResolverBeanTest {

  private LogResolverBean bean;
  private ContainerWrapperBean containerWrapper;

  @BeforeEach
  void setUp() {
    bean = new LogResolverBean();
    containerWrapper = mock(ContainerWrapperBean.class);
    bean.setContainerWrapper(containerWrapper);
  }

  @Test
  void testGetAndSetStdoutFiles() {
    List<String> files = Arrays.asList("catalina.out", "stdout.log");
    bean.setStdoutFiles(files);
    assertEquals(files, bean.getStdoutFiles());
  }

  @Test
  void testGetLogDestinations_Empty() {
    // Instruments.isInitialized() returns false, so should be empty
    bean.setStdoutFiles(Collections.emptyList());
    // Mock Instruments.isInitialized() to return false
    try (var mocked = org.mockito.Mockito.mockStatic(psiprobe.tools.Instruments.class)) {
      mocked.when(psiprobe.tools.Instruments::isInitialized).thenReturn(false);
      List<LogDestination> result = bean.getLogDestinations(true);
      assertTrue(result.isEmpty());
    }
  }

  @Test
  void testGetLogSourcesByFile() {
    Path file = Path.of("test.log");
    LogDestination dest = mock(LogDestination.class);
    when(dest.getFile()).thenReturn(file.toFile());

    LogResolverBean spyBean = Mockito.spy(bean);
    doReturn(List.of(dest)).when(spyBean).getLogSources();

    List<LogDestination> result = spyBean.getLogSources(file.toFile());
    assertEquals(1, result.size());
    assertEquals(dest, result.get(0));
  }

  @Test
  void testGetLogSources_Empty() {
    // Instruments.isInitialized() returns false, so should be empty
    try (var mocked = org.mockito.Mockito.mockStatic(psiprobe.tools.Instruments.class)) {
      mocked.when(psiprobe.tools.Instruments::isInitialized).thenReturn(false);
      List<LogDestination> result = bean.getLogSources();
      assertTrue(result.isEmpty());
    }
  }

  @Test
  void testGetLogDestination_Stdout() {
    bean.setStdoutFiles(List.of("stdout.log"));
    // Create a file that exists
    File file = mock(File.class);
    when(file.exists()).thenReturn(true);

    // Spy to override resolveStdoutLogDestination
    LogResolverBean spyBean = Mockito.spy(bean);
    FileLogAccessor accessor = new FileLogAccessor();
    accessor.setFile(file);
    accessor.setName("stdout.log");
    doReturn(accessor).when(spyBean).resolveStdoutLogDestination("stdout.log");

    LogDestination result =
        spyBean.getLogDestination("stdout", null, false, false, "stdout.log", null);
    assertNotNull(result);
    assertEquals("stdout.log", result.getName());
  }

  @Test
  void testGetLogDestination_UnknownType() {
    LogDestination result = bean.getLogDestination("unknown", null, false, false, null, null);
    assertNull(result);
  }

  /**
   * Test set and get container wrapper.
   */
  @Test
  void testSetAndGetContainerWrapper() {
    ContainerWrapperBean cw = mock(ContainerWrapperBean.class);
    bean.setContainerWrapper(cw);
    assertEquals(cw, bean.getContainerWrapper());
  }

}
