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
package org.jstripe.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.text.MessageFormat;

public class VisualScoreTag extends BodyTagSupport {

    private double value;
    private double minValue = 0;
    private double maxValue = 100;
    private int partialBlocks = 1;
    private int fullBlocks = 5;
    private boolean showEmptyBlocks=false;
    private boolean showA = false;
    private boolean showB = false;

    public int doAfterBody() throws JspException {

        try {
            double unitSize = (maxValue - minValue) / (fullBlocks * partialBlocks);

            int fullBlockCount = (int) Math.floor(value / (unitSize * partialBlocks));
            int partialBlockIndex = (int) Math.floor((value - fullBlockCount * unitSize * partialBlocks) / unitSize);

            BodyContent bc = getBodyContent();
            String body = bc.getString().trim();
            JspWriter out = bc.getEnclosingWriter();


            if (showA) {
                out.print(MessageFormat.format(body, new Object[]{fullBlockCount > 0 || partialBlockIndex > 0 ? "a" : "a0"}));
            }

            String fullBody = MessageFormat.format(body, new Object[]{new Integer(partialBlocks)});
            for (int i = 0; i < fullBlockCount; i++) {
                out.print(fullBody);
            }

            if (partialBlockIndex > 0) {
                String partialBody = MessageFormat.format(body, new Object[]{new Integer(partialBlockIndex)});
                out.print(partialBody);
            }

            int emptyBlocks = showEmptyBlocks ? fullBlocks - fullBlockCount - (partialBlockIndex > 0 ? 1 : 0) : 0;

            if (emptyBlocks > 0) {
                String emptyBody = MessageFormat.format(body, new Object[]{new Integer(0)});
                for (int i = 0; i < emptyBlocks; i++) {
                    out.print(emptyBody);
                }
            }

            if (showB) {
                out.print(MessageFormat.format(body, new Object[]{fullBlockCount == fullBlocks ? "b" : "b0"}));
            }



        } catch (IOException ioe) {
            throw new JspException("Error:IOException while writing to client" + ioe.getMessage());
        }

        return SKIP_BODY;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public int getPartialBlocks() {
        return partialBlocks;
    }

    public void setPartialBlocks(int partialBlocks) {
        this.partialBlocks = partialBlocks;
    }

    public int getFullBlocks() {
        return fullBlocks;
    }

    public void setFullBlocks(int fullBlocks) {
        this.fullBlocks = fullBlocks;
    }

    public boolean isShowEmptyBlocks() {
        return showEmptyBlocks;
    }

    public void setShowEmptyBlocks(boolean showEmptyBlocks) {
        this.showEmptyBlocks = showEmptyBlocks;
    }

    public boolean isShowA() {
        return showA;
    }

    public void setShowA(boolean showA) {
        this.showA = showA;
    }

    public boolean isShowB() {
        return showB;
    }

    public void setShowB(boolean showB) {
        this.showB = showB;
    }
}