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

    public static final long MULTIPLIER_2 = 1024;
    public static final long MULTIPLIER_10 = 1000;
    public static final String UNIT_BASE = "B";
    public static final char PREFIX_KILO = 'K';
    public static final char PREFIX_MEGA = 'M';
    public static final char PREFIX_GIGA = 'G';
    public static final char PREFIX_TERA = 'T';
    public static final char PREFIX_PETA = 'P';

    public static long parse(String expression) {
        String prefixClass = "[" + PREFIX_KILO + PREFIX_MEGA + PREFIX_GIGA + PREFIX_TERA + PREFIX_PETA + "]";
        Pattern p = Pattern.compile("(\\d+|\\d*\\.\\d+)\\s*(" + prefixClass + ")?(" + UNIT_BASE + ")?", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(expression);
        if (m.matches()) {
            String value = m.group(1);
            String unitPrefix = m.group(2);
            String unitBase = m.group(3);
            long multiplier = 1;
            if (unitPrefix != null) {
                multiplier = multiplier(unitPrefix.charAt(0), unitBase != null);
            }
            double rawValue = Double.parseDouble(value);
            return (long) (rawValue * multiplier);
        } else {
            throw new IllegalArgumentException("Invalid expression format: " + expression);
        }
    }

    public static String format(long bytes, int decimalPlaces) {
        return format(bytes, decimalPlaces, true);
    }

    public static String format(long value, int decimalPlaces, boolean base2) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(decimalPlaces);

        double doubleResult;
        String unit = (base2 ? UNIT_BASE : "");
        long multiplierKilo = multiplier(PREFIX_KILO, base2);
        long multiplierMega = multiplier(PREFIX_MEGA, base2);
        long multiplierGiga = multiplier(PREFIX_GIGA, base2);
        long multiplierTera = multiplier(PREFIX_TERA, base2);
        long multiplierPeta = multiplier(PREFIX_PETA, base2);
        if (value < multiplierKilo) {
            doubleResult = value;
            nf.setMinimumFractionDigits(0);
        } else if (value >= multiplierKilo && value < multiplierMega) {
            doubleResult = round(value / multiplierKilo, decimalPlaces);
            unit = PREFIX_KILO + unit;
        } else if (value >= multiplierMega && value < multiplierGiga) {
            doubleResult = round(value / multiplierMega, decimalPlaces);
            unit = PREFIX_MEGA + unit;
        } else if (value >= multiplierGiga && value < multiplierTera) {
            doubleResult = round(value / multiplierGiga, decimalPlaces);
            unit = PREFIX_GIGA + unit;
        } else if (value >= multiplierTera && value < multiplierPeta) {
            doubleResult = round(value / multiplierTera, decimalPlaces);
            unit = PREFIX_TERA + unit;
        } else {
            doubleResult = round(value / multiplierPeta, decimalPlaces);
            unit = PREFIX_PETA + unit;
        }
        return nf.format(doubleResult) + (base2 ? " " : "") + unit;
    }

    private static double round(double value, int decimalPlaces) {
        return Math.round(value * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
    }

    private static long multiplier(char unitPrefix, boolean base2) {
        long result;
        long multiplier = (base2 ? MULTIPLIER_2 : MULTIPLIER_10);
        switch(Character.toUpperCase(unitPrefix)) {
            case PREFIX_KILO:
                result = multiplier;
                break;
            case PREFIX_MEGA:
                result = multiplier * multiplier;
                break;
            case PREFIX_GIGA:
                result = multiplier * multiplier * multiplier;
                break;
            case PREFIX_TERA:
                result = multiplier * multiplier * multiplier * multiplier;
                break;
            case PREFIX_PETA:
                result = multiplier * multiplier * multiplier * multiplier * multiplier;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit prefix: " + unitPrefix);
        }
        return result;
    }

}
