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

/**
 * The Class TimeExpression.
 */
public final class TimeExpression {

  /**
   * Prevent Instantiation.
   */
  private TimeExpression() {
    // Prevent Instantiation
  }

  /**
   * Data points.
   *
   * @param periodExpression the period expression
   * @param spanExpression the span expression
   *
   * @return the long
   */
  public static long dataPoints(String periodExpression, String spanExpression) {
    return dataPoints(inSeconds(periodExpression), inSeconds(spanExpression));
  }

  /**
   * Data points.
   *
   * @param period the period
   * @param span the span
   *
   * @return the long
   */
  public static long dataPoints(long period, long span) {
    if (period <= 0) {
      return 0;
    }
    return span / period;
  }

  /**
   * Cron expression.
   *
   * @param periodExpression the period expression
   * @param phaseExpression the phase expression
   *
   * @return the string
   */
  public static String cronExpression(String periodExpression, String phaseExpression) {
    return cronExpression(inSeconds(periodExpression), inSeconds(phaseExpression));
  }

  /**
   * Cron expression.
   *
   * @param period the period
   * @param phase the phase
   *
   * @return the string
   */
  public static String cronExpression(long period, long phase) {
    while (phase >= period) {
      phase = phase - period;
    }
    long secondsPeriod = 0;
    long minutesPeriod = 0;
    long hoursPeriod = 0;
    if (period < 60) {
      secondsPeriod = period;
    } else if (period < 60 * 60) {
      minutesPeriod = period / 60;
    } else if (period < 60 * 60 * 24) {
      hoursPeriod = period / (60 * 60);
    } else {
      throw new IllegalArgumentException("Period is too large: " + period);
    }
    long secondsPhase = 0;
    long minutesPhase = 0;
    long hoursPhase = 0;
    if (phase < 60) {
      secondsPhase = phase;
    } else if (phase < 60 * 60) {
      minutesPhase = phase / 60;
    } else if (phase < 60 * 60 * 24) {
      hoursPhase = phase / (60 * 60);
    } else {
      throw new IllegalArgumentException("Phase is too large: " + phase);
    }
    String secondsCron = cronSubexpression(secondsPeriod, secondsPhase);
    String minutesCron = "*";
    String hoursCron = "*";
    String daysCron = "*";
    String monthsCron = "*";
    String dowCron = "?";
    if (secondsPeriod == 0) {
      minutesCron = cronSubexpression(minutesPeriod, minutesPhase);
      if (minutesPeriod == 0) {
        hoursCron = cronSubexpression(hoursPeriod, hoursPhase);
      }
    }
    return secondsCron + " " + minutesCron + " " + hoursCron + " " + daysCron + " " + monthsCron
        + " " + dowCron;
  }

  /**
   * Cron subexpression.
   *
   * @param period the period
   * @param phase the phase
   *
   * @return the string
   */
  private static String cronSubexpression(long period, long phase) {
    if (period == 0) {
      return Long.toString(phase);
    }
    if (period == 1 && phase == 0) {
      return "*";
    }
    return phase + "/" + period;
  }

  /**
   * In seconds.
   *
   * @param expression the expression
   *
   * @return the long
   */
  public static long inSeconds(String expression) {
    if (expression == null || expression.isEmpty()) {
      return 0;
    }
    if (expression.matches("[0-9]+[smhd]")) {
      long multiplier = multiplier(expression.charAt(expression.length() - 1));
      if (multiplier == 0) {
        throw new IllegalArgumentException("Invalid unit in expression: " + expression);
      }
      long value = Integer.parseInt(expression.substring(0, expression.length() - 1));
      if (value < 0) {
        throw new IllegalArgumentException("Invalid value in expression: " + expression);
      }
      return value * multiplier;
    }
    throw new IllegalArgumentException("Invalid expression format: " + expression);
  }

  /**
   * Multiplier.
   *
   * @param unit the unit
   *
   * @return the int
   */
  private static int multiplier(char unit) {
    switch (unit) {
      case 's':
        return 1;
      case 'm':
        return 60;
      case 'h':
        return 60 * 60;
      default:
        throw new IllegalArgumentException("Invalid unit: " + unit);
    }
  }

}
