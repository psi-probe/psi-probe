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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Silly JSP tag to display duration in milliseconds as hours:minutes:seconds.milliseconds
 *
 * @author Vlad Ilyushchenko
 */
public class DurationTag extends TagSupport {

    private static Log logger = LogFactory.getLog(DurationTag.class);

    private long value;

    public void setValue(long value) {
        this.value = value;
    }

    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().write(duration(value));
        } catch (IOException e) {
            logger.debug(e.getMessage());
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    public static String duration(long value) {
        long millis = value % 1000;
        long sec = value / 1000;
        long mins = sec / 60;
        long hours = mins / 60;

        sec = sec % 60;
        mins = mins % 60;

        return hours + ":" + long2Str(mins) + ":" + long2Str(sec) + "." + long3Str(millis);
    }

    private static String long2Str(long l) {
        return l < 10 ? "0"+l : Long.toString(l);
    }

    private static String long3Str(long l) {
        return l < 10 ? "00"+l : l < 100 ? "0"+l : Long.toString(l);
    }

}
