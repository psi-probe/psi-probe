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
package psiprobe.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codebox.bean.JavaBeanTester;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * The Class SystemInformationTest.
 */
class SystemInformationTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(SystemInformation.class).loadData().skipStrictSerializable().test();
  }

  @Test
  void testGetMemoryValues() {
    SystemInformation si = new SystemInformation();
    assertTrue(si.getMaxMemory() > 0);
    assertTrue(si.getFreeMemory() >= 0);
    assertTrue(si.getTotalMemory() > 0);
  }

  @Test
  void testGetCpuCount() {
    SystemInformation si = new SystemInformation();
    assertTrue(si.getCpuCount() > 0);
  }

  @Test
  void testGetDate() {
    SystemInformation si = new SystemInformation();
    assertNotNull(si.getDate());
  }

  @Test
  void testGetWorkingDir() {
    SystemInformation si = new SystemInformation();
    assertNotNull(si.getWorkingDir());
  }

  @Test
  void testGetServerInfo() {
    SystemInformation si = new SystemInformation();
    // may return empty string if not in Tomcat, but should not throw
    assertNotNull(si.getServerInfo());
  }

  @Test
  void testSystemPropertySet() {
    SystemInformation si = new SystemInformation();
    Map<String, String> props = new HashMap<>();
    props.put("key1", "val1");
    si.setSystemProperties(props);
    assertNotNull(si.getSystemPropertySet());
    assertTrue(si.getSystemPropertySet().size() == 1);
  }
}
