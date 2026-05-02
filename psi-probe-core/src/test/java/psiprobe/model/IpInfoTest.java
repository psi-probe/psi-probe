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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * The Class IpInfoTest.
 */
class IpInfoTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(IpInfo.class).loadData().test();
  }

  @Test
  void testBuilderWithDirectConnection() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteAddr("192.168.1.1");
    // No X-Forwarded-For header

    IpInfo info = new IpInfo().builder(request);
    assertEquals("192.168.1.1", info.getAddress());
    assertFalse(info.isForwarded());
  }

  @Test
  void testBuilderWithForwardedHeader() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteAddr("10.0.0.1");
    request.addHeader("X-Forwarded-For", "203.0.113.5, 10.0.0.1");

    IpInfo info = new IpInfo().builder(request);
    assertEquals("203.0.113.5", info.getAddress());
    assertTrue(info.isForwarded());
  }

  @Test
  void testGetClientAddressWithoutHeader() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteAddr("172.16.0.1");

    String addr = IpInfo.getClientAddress(request);
    assertEquals("172.16.0.1", addr);
  }

  @Test
  void testGetClientAddressWithForwardedHeader() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteAddr("10.0.0.1");
    request.addHeader("X-Forwarded-For", "1.2.3.4");

    String addr = IpInfo.getClientAddress(request);
    assertEquals("1.2.3.4", addr);
  }

  @Test
  void testToString() {
    IpInfo info = new IpInfo();
    info.setAddress("10.10.10.10");
    assertEquals("10.10.10.10", info.toString());
  }
}
