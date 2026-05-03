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
package psiprobe.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.codebox.bean.JavaBeanTester;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TimeExpression}.
 */
class TimeExpressionTest {

  @Test
  void testPrivateConstructor() {
    JavaBeanTester.builder(TimeExpression.class).testPrivateConstructor();
  }

  // --- inSeconds tests ---

  @Test
  void testInSecondsSeconds() {
    assertEquals(30, TimeExpression.inSeconds("30s"));
    assertEquals(1, TimeExpression.inSeconds("1s"));
    assertEquals(0, TimeExpression.inSeconds("0s"));
  }

  @Test
  void testInSecondsMinutes() {
    assertEquals(60, TimeExpression.inSeconds("1m"));
    assertEquals(300, TimeExpression.inSeconds("5m"));
  }

  @Test
  void testInSecondsHours() {
    assertEquals(3600, TimeExpression.inSeconds("1h"));
    assertEquals(7200, TimeExpression.inSeconds("2h"));
  }

  @Test
  void testInSecondsNullReturnsZero() {
    assertEquals(0, TimeExpression.inSeconds(null));
  }

  @Test
  void testInSecondsEmptyReturnsZero() {
    assertEquals(0, TimeExpression.inSeconds(""));
  }

  @Test
  void testInSecondsInvalidFormatThrows() {
    assertThrows(IllegalArgumentException.class, () -> TimeExpression.inSeconds("100"));
    assertThrows(IllegalArgumentException.class, () -> TimeExpression.inSeconds("100d"));
    assertThrows(IllegalArgumentException.class, () -> TimeExpression.inSeconds("abc"));
  }

  // --- dataPoints tests ---

  @Test
  void testDataPointsFromExpressions() {
    long result = TimeExpression.dataPoints("10s", "1m");
    assertEquals(6, result);
  }

  @Test
  void testDataPointsZeroPeriod() {
    assertEquals(0, TimeExpression.dataPoints(0L, 100L));
  }

  @Test
  void testDataPointsNegativePeriod() {
    assertEquals(0, TimeExpression.dataPoints(-1L, 100L));
  }

  @Test
  void testDataPointsExact() {
    assertEquals(10, TimeExpression.dataPoints(10L, 100L));
    assertEquals(6, TimeExpression.dataPoints(600L, 3600L));
  }

  // --- cronExpression tests ---

  @Test
  void testCronExpressionEverySecond() {
    String cron = TimeExpression.cronExpression(1L, 0L);
    assertEquals("* * * * * ?", cron);
  }

  @Test
  void testCronExpressionEvery30Seconds() {
    String cron = TimeExpression.cronExpression(30L, 0L);
    assertEquals("0/30 * * * * ?", cron);
  }

  @Test
  void testCronExpressionEvery30SecondsWithPhase() {
    String cron = TimeExpression.cronExpression(30L, 5L);
    assertEquals("5/30 * * * * ?", cron);
  }

  @Test
  void testCronExpressionEveryMinute() {
    // period=1 minute, phase=0: cronSubexpression(1, 0) returns "*"
    String cron = TimeExpression.cronExpression(60L, 0L);
    assertEquals("0 * * * * ?", cron);
  }

  @Test
  void testCronExpressionEvery5Minutes() {
    String cron = TimeExpression.cronExpression(300L, 0L);
    assertEquals("0 0/5 * * * ?", cron);
  }

  @Test
  void testCronExpressionEvery5MinutesWithPhase() {
    String cron = TimeExpression.cronExpression(300L, 60L);
    assertEquals("0 1/5 * * * ?", cron);
  }

  @Test
  void testCronExpressionEveryHour() {
    // period=1 hour, phase=0: cronSubexpression(1, 0) returns "*"
    String cron = TimeExpression.cronExpression(3600L, 0L);
    assertEquals("0 0 * * * ?", cron);
  }

  @Test
  void testCronExpressionEvery2Hours() {
    String cron = TimeExpression.cronExpression(7200L, 0L);
    assertEquals("0 0 0/2 * * ?", cron);
  }

  @Test
  void testCronExpressionPhaseWrapsAround() {
    // phase >= period: phase = phase - period
    String cron = TimeExpression.cronExpression(30L, 35L);
    // phase = 35 - 30 = 5
    assertEquals("5/30 * * * * ?", cron);
  }

  @Test
  void testCronExpressionTooLargeThrows() {
    assertThrows(IllegalArgumentException.class, () -> TimeExpression.cronExpression(86400L, 0L));
  }

  @Test
  void testCronExpressionFromExpressions() {
    String cron = TimeExpression.cronExpression("30s", "0s");
    assertEquals("0/30 * * * * ?", cron);
  }

  @Test
  void testCronExpressionSpecificSeconds() {
    // period=60s (1 min), phase=30s
    // minutesPeriod=1, minutesPhase=0; secondsPeriod=0, secondsPhase=30
    // secondsCron = "30", minutesCron = cronSubexpression(1,0) = "*"
    String cron = TimeExpression.cronExpression(60L, 30L);
    assertEquals("30 * * * * ?", cron);
  }
}
