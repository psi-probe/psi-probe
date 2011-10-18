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
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.lang.StringEscapeUtils;

public class OutTag extends BodyTagSupport {

    private int maxLength = -1;
    private boolean ellipsisRight = true;
    private Object value = null;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isEllipsisRight() {
        return ellipsisRight;
    }

    public void setEllipsisRight(boolean ellipsisRight) {
        this.ellipsisRight = ellipsisRight;
    }

    public int doStartTag() throws JspException {
        if (value != null) {
            print(value.toString(), pageContext.getOut());
            return SKIP_BODY;
        } else {
            return super.doStartTag();
        }
    }

    public int doAfterBody() throws JspException {
        print(getBodyContent().getString().trim(), getBodyContent().getEnclosingWriter());
        return SKIP_BODY;
    }

    private void print(String displayValue, JspWriter out) throws JspException {
        try {
            if (maxLength != -1 && displayValue.length() > maxLength) {
                String newValue;
                if (ellipsisRight) {
                    newValue = displayValue.substring(0, maxLength -3) + "...";
                } else {
                    newValue = "..." + displayValue.substring(displayValue.length() - maxLength + 3);
                }
                String title = StringEscapeUtils.escapeHtml(displayValue);
                out.print("<span title=\""+title+"\">"+newValue+"</span>");
            } else {
                out.print(displayValue);
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
    }
}
