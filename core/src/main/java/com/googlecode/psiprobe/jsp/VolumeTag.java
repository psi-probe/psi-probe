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
package com.googlecode.psiprobe.jsp;

import java.io.IOException;
import java.text.NumberFormat;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JSP tag to convert size from bytes into human readable form: KB, MB, GB or TB
 * depending on how large the value in bytes is.
 *
 * @author Vlad Ilyushchenko
 */
public class VolumeTag extends TagSupport {

    public static final double KB = 1024;
    public static final double MB = KB * 1024;
    public static final double GB = MB * 1024;
    public static final double TB = GB * 1024;

    private Log logger = LogFactory.getLog(getClass());

    private long value;
    private int fractions = 0;

    public void setValue(long value) {
        this.value = value;
    }

    public int getFractions() {
        return fractions;
    }

    public void setFractions(int fractions) {
        this.fractions = fractions;
    }

    public int doStartTag() throws JspException {
        double doubleResult;
        String suffix;

        if (value < KB) {
            doubleResult = value;
            suffix = "B";
        } else if (value >= KB && value < MB) {
            doubleResult = round(value / KB);
            suffix = "KB";
        } else if (value >= MB && value < GB) {
            doubleResult = round(value / MB);
            suffix = "MB";
        } else if (value >= GB && value < TB) {
            doubleResult = round(value / GB);
            suffix = "GB";
        } else {
            doubleResult = round(value / TB);
            suffix = "TB";
        }

        try {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumFractionDigits(fractions);
            String title = Long.toString(value);
            String newValue = nf.format(doubleResult) + " " + suffix;
            pageContext.getOut().write("<span title=\"" + title + "\">" + newValue + "</span>");
        } catch (IOException e) {
            logger.debug(e);
            throw new JspException(e);
        }

        return EVAL_BODY_INCLUDE;
    }

    private double round(double value) {
        return Math.round(value * Math.pow(10, fractions)) / Math.pow(10, fractions);
    }
}
