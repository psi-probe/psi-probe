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

import com.googlecode.psiprobe.tools.SizeExpression;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JSP tag to convert size from bytes into human readable form: KB, MB, GB or TB
 * depending on how large the value in bytes is.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class VolumeTag extends TagSupport {

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
        String title = Long.toString(value);
        String newValue = SizeExpression.roundedExpression(value, fractions);
        try {
            pageContext.getOut().write("<span title=\"" + title + "\">" + newValue + "</span>");
        } catch (IOException e) {
            logger.debug("Exception writing value to JspWriter", e);
            throw new JspException(e);
        }

        return EVAL_BODY_INCLUDE;
    }

}
