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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codebox.bean.JavaBeanTester;

import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * The Class ApplicationSessionTest.
 */
class ApplicationSessionTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(ApplicationSession.class).loadData().test();
  }

  @Test
  void testGetAgeWithCreationTime() {
    ApplicationSession session = new ApplicationSession();
    Date past = new Date(System.currentTimeMillis() - 5000);
    session.setCreationTime(past);
    assertTrue(session.getAge() >= 5000);
  }

  @Test
  void testGetAgeWithNullCreationTime() {
    ApplicationSession session = new ApplicationSession();
    // creationTime is null by default
    assertEquals(0, session.getAge());
  }

  @Test
  void testGetIdleTimeWithLastAccessTime() {
    ApplicationSession session = new ApplicationSession();
    Date past = new Date(System.currentTimeMillis() - 3000);
    session.setLastAccessTime(past);
    assertTrue(session.getIdleTime() >= 3000);
  }

  @Test
  void testGetIdleTimeWithNullLastAccessTime() {
    ApplicationSession session = new ApplicationSession();
    // No last access time - should fall back to getAge()
    assertEquals(0, session.getIdleTime());
  }

  @Test
  void testGetExpiryTimePositiveMaxIdleTime() {
    ApplicationSession session = new ApplicationSession();
    session.setMaxIdleTime(60000); // 60 seconds
    Date expiry = session.getExpiryTime();
    assertNotNull(expiry);
    assertTrue(expiry.getTime() > System.currentTimeMillis());
  }

  @Test
  void testGetExpiryTimeZeroMaxIdleTime() {
    ApplicationSession session = new ApplicationSession();
    session.setMaxIdleTime(0);
    assertNull(session.getExpiryTime());
  }

  @Test
  void testGetExpiryTimeNegativeMaxIdleTime() {
    ApplicationSession session = new ApplicationSession();
    session.setMaxIdleTime(-1);
    assertNull(session.getExpiryTime());
  }

  @Test
  void testGetCreationTimeReturnsDefensiveCopy() {
    ApplicationSession session = new ApplicationSession();
    Date original = new Date(1000L);
    session.setCreationTime(original);
    Date retrieved = session.getCreationTime();
    assertNotNull(retrieved);
    assertEquals(1000L, retrieved.getTime());
  }

  @Test
  void testGetCreationTimeWhenNull() {
    ApplicationSession session = new ApplicationSession();
    assertNull(session.getCreationTime());
  }

  @Test
  void testGetLastAccessTimeWhenNull() {
    ApplicationSession session = new ApplicationSession();
    assertNull(session.getLastAccessTime());
  }
}
