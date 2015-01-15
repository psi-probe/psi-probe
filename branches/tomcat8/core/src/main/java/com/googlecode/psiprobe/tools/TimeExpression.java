/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.tools;

/**
 *
 * @author Mark Lewis
 */
public class TimeExpression {

    public static String cronExpression(String periodExpression, String phaseExpression) {
        return cronExpression(inSeconds(periodExpression), inSeconds(phaseExpression));
    }

    public static long dataPoints(String periodExpression, String spanExpression) {
        return dataPoints(inSeconds(periodExpression), inSeconds(spanExpression));
    }

    public static long dataPoints(long period, long span) {
        return span / period;
    }

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
        String secondsCron = "*";
        String minutesCron = "*";
        String hoursCron = "*";
        String daysCron = "*";
        String monthsCron = "*";
        String dowCron = "?";
        secondsCron = cronSubexpression(secondsPeriod, secondsPhase);
        if (secondsPeriod == 0) {
            minutesCron = cronSubexpression(minutesPeriod, minutesPhase);
            if (minutesPeriod == 0) {
                hoursCron = cronSubexpression(hoursPeriod, hoursPhase);
            }
        }
        return secondsCron + " " + minutesCron + " " + hoursCron + " " + daysCron + " " + monthsCron + " " + dowCron;
    }

    private static String cronSubexpression(long period, long phase) {
        if (period == 0) {
            return Long.toString(phase);
        } else if (period == 1 && phase == 0) {
            return "*";
        } else {
            return phase + "/" + period;
        }
    }

    public static long inSeconds(String expression) {
        if (expression == null || expression.equals("")) {
            return 0;
        }
        if (expression.matches("[0-9]+[smhd]")) {
            int multiplier = multiplier(expression.charAt(expression.length() - 1));
            if (multiplier == 0) {
                throw new IllegalArgumentException("Invalid unit in expression: " + expression);
            }
            int value = Integer.parseInt(expression.substring(0, expression.length() - 1));
            if (value < 0) {
                throw new IllegalArgumentException("Invalid value in expression: " + expression);
            }
            return value * multiplier;
        } else {
            throw new IllegalArgumentException("Invalid expression format: " + expression);
        }
    }

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
