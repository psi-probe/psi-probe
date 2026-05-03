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
package psiprobe.jsp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;

/**
 * The Class DurationTagTest.
 */
class DurationTagTest {

  /**
   * Javabean tester.
   */
  @Test
  void javabeanTester() {
    JavaBeanTester.builder(DurationTag.class).loadData().skipStrictSerializable().test();
  }

  @Test
  void testDurationZero() {
    assertEquals("0:00:00.000", DurationTag.duration(0L));
  }

  @Test
  void testDurationOneSecond() {
    assertEquals("0:00:01.000", DurationTag.duration(1000L));
  }

  @Test
  void testDurationOneMinute() {
    assertEquals("0:01:00.000", DurationTag.duration(60000L));
  }

  @Test
  void testDurationOneHour() {
    assertEquals("1:00:00.000", DurationTag.duration(3600000L));
  }

  @Test
  void testDurationMillisLessThan10() {
    // 5 millis should give "005"
    assertEquals("0:00:00.005", DurationTag.duration(5L));
  }

  @Test
  void testDurationMillisBetween10And100() {
    // 50 millis should give "050"
    assertEquals("0:00:00.050", DurationTag.duration(50L));
  }

  @Test
  void testDurationMillisGte100() {
    // 500 millis should give "500"
    assertEquals("0:00:00.500", DurationTag.duration(500L));
  }

  @Test
  void testDurationComplex() {
    // 1h 23m 45s 678ms = 3600000 + 23*60000 + 45000 + 678 = 3600000 + 1380000 + 45000 + 678
    long millis = 3600000L + 23 * 60000L + 45000L + 678L;
    assertEquals("1:23:45.678", DurationTag.duration(millis));
  }

  @Test
  void testDurationSecsLessThan10() {
    // 5 seconds = "0:00:05.000"
    assertEquals("0:00:05.000", DurationTag.duration(5000L));
  }

  @Test
  void testDurationMinsLessThan10() {
    // 5 minutes = "0:05:00.000"
    assertEquals("0:05:00.000", DurationTag.duration(300000L));
  }
}
