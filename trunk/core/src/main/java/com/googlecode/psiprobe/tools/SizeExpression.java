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

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Mark Lewis
 */
public class SizeExpression {

    public static final long MULTIPLIER = 1024;
    public static final long MULTIPLIER_KILO = MULTIPLIER;
    public static final long MULTIPLIER_MEGA = MULTIPLIER_KILO * MULTIPLIER;
    public static final long MULTIPLIER_GIGA = MULTIPLIER_MEGA * MULTIPLIER;
    public static final long MULTIPLIER_TERA = MULTIPLIER_GIGA * MULTIPLIER;
    public static final long MULTIPLIER_PETA = MULTIPLIER_TERA * MULTIPLIER;
    public static final String UNIT_BASE = "B";
    public static final char PREFIX_KILO = 'K';
    public static final char PREFIX_MEGA = 'M';
    public static final char PREFIX_GIGA = 'G';
    public static final char PREFIX_TERA = 'T';
    public static final char PREFIX_PETA = 'P';

    public static long inBytes(String expression) {
        String prefixClass = "[" + PREFIX_KILO + PREFIX_MEGA + PREFIX_GIGA + PREFIX_TERA + PREFIX_PETA + "]";
        Pattern p = Pattern.compile("(\\d+)\\s*(" + prefixClass + ")?" + UNIT_BASE + "?", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(expression);
        if (m.matches()) {
            String value = m.group(1);
            String unitPrefix = m.group(2);
            long multiplier = 1;
            if (unitPrefix != null) {
                multiplier = multiplier(unitPrefix.charAt(0));
            }
            long rawValue = Long.parseLong(value);
            return rawValue * multiplier;
        } else {
            throw new IllegalArgumentException("Invalid expression format: " + expression);
        }
    }

    public static String roundedExpression(long bytes, int decimalPlaces) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(decimalPlaces);

        double doubleResult;
        String unit = UNIT_BASE;
        if (bytes < MULTIPLIER_KILO) {
            doubleResult = bytes;
            nf.setMinimumFractionDigits(0);
        } else if (bytes >= MULTIPLIER_KILO && bytes < MULTIPLIER_MEGA) {
            doubleResult = round(bytes / MULTIPLIER_KILO, decimalPlaces);
            unit = PREFIX_KILO + unit;
        } else if (bytes >= MULTIPLIER_MEGA && bytes < MULTIPLIER_GIGA) {
            doubleResult = round(bytes / MULTIPLIER_MEGA, decimalPlaces);
            unit = PREFIX_MEGA + unit;
        } else if (bytes >= MULTIPLIER_GIGA && bytes < MULTIPLIER_TERA) {
            doubleResult = round(bytes / MULTIPLIER_GIGA, decimalPlaces);
            unit = PREFIX_GIGA + unit;
        } else if (bytes >= MULTIPLIER_TERA && bytes < MULTIPLIER_PETA) {
            doubleResult = round(bytes / MULTIPLIER_TERA, decimalPlaces);
            unit = PREFIX_TERA + unit;
        } else {
            doubleResult = round(bytes / MULTIPLIER_PETA, decimalPlaces);
            unit = PREFIX_PETA + unit;
        }
        return nf.format(doubleResult) + " " + unit;
    }

    private static double round(double value, int decimalPlaces) {
        return Math.round(value * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
    }

    private static long multiplier(char unitPrefix) {
        long result;
        switch(Character.toUpperCase(unitPrefix)) {
            case PREFIX_KILO:
                result = MULTIPLIER_KILO;
                break;
            case PREFIX_MEGA:
                result = MULTIPLIER_MEGA;
                break;
            case PREFIX_GIGA:
                result = MULTIPLIER_GIGA;
                break;
            case PREFIX_TERA:
                result = MULTIPLIER_TERA;
                break;
            case PREFIX_PETA:
                result = MULTIPLIER_PETA;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit prefix: " + unitPrefix);
        }
        return result;
    }

}
