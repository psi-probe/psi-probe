/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package com.googlecode.psiprobe.jsp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class VisualScoreTag extends BodyTagSupport {

  private static final long serialVersionUID = -5653846466205838602L;

  private static final String WHITE_LEFT_BORDER = "a0";
  private static final String RED_LEFT_BORDER = "a1";
  private static final String BLUE_LEFT_BORDER = "a2";

  private static final String WHITE_RIGHT_BORDER = "b0";
  private static final String RED_RIGHT_BORDER = "b1";
  private static final String BLUE_RIGHT_BORDER = "b2";

  protected Log log = LogFactory.getLog(getClass());

  /**
   * Red value.
   */
  private double value = 0;

  /**
   * Blue value.
   */
  private double value2 = 0;

  private double minValue = 0;
  private double maxValue = 100;

  /**
   * Number of parts in one block.<br>
   * It always must be 5 to match the 5 different gifs available at
   * /src/main/webapp/css/classic/gifs
   */
  private int partialBlocks = 1;

  /**
   * Total number of blocks.<br>
   * fullBlocks + 2 img elements will be added to the page.<br>
   * The plus 2 is due the left and right border.
   */
  private int fullBlocks = 5;

  private boolean showEmptyBlocks = false;
  private boolean showA = false;
  private boolean showB = false;

  public int doAfterBody() throws JspException {
    BodyContent bc = getBodyContent();
    String body = bc.getString().trim();

    StringBuffer buf = calculateSuffix(body);

    try {
      JspWriter out = bc.getEnclosingWriter();
      out.print(buf.toString());
    } catch (IOException ioe) {
      throw new JspException("Error:IOException while writing to client" + ioe.getMessage());
    }

    return SKIP_BODY;
  }

  StringBuffer calculateSuffix(String body) {
    if (value < minValue) {
      log.info("value " + value + " is less than min value " + minValue);
      value = minValue;
    }
    if (value > maxValue) {
      log.info("value " + value + " is greater than max value " + maxValue);
      value = maxValue;
    }
    if (value + value2 < minValue || value2 < 0) {
      log.info("value2 " + value2 + " is less than min value");
      value2 = 0;
    }
    if (value + value2 > maxValue) {
      log.info("value2 " + value2 + " is greater than max value");
      value2 = maxValue - value;
    }

    double unitSize = (maxValue - minValue) / (fullBlocks * partialBlocks);
    double blockWidth = unitSize * partialBlocks;

    int fullRedBlockCount = (int) Math.floor(value / blockWidth);
    int partialRedBlockIndex =
        (int) Math.floor((value - fullRedBlockCount * blockWidth) / unitSize);
    int partialBlueBlockIndex1 =
        (partialRedBlockIndex > 0 ? Math.min((int) Math.floor(value2 / unitSize), partialBlocks
            - partialRedBlockIndex) : 0);
    int fullBlueBlockCount =
        Math.max(0, (int) Math.ceil(value2 / blockWidth) - (partialRedBlockIndex > 0 ? 1 : 0));
    int partialBlueBlockIndex2 =
        (int) Math
            .floor((value2 - (fullBlueBlockCount * blockWidth) - (partialBlueBlockIndex1 * unitSize))
                / unitSize);

    StringBuffer buf = new StringBuffer();

    // Beginning
    if (showA) {
      String format = WHITE_LEFT_BORDER;
      if (fullRedBlockCount > 0 || partialRedBlockIndex > 0) {
        format = RED_LEFT_BORDER;
      } else if (partialBlueBlockIndex1 == 0
          && (fullBlueBlockCount > 0 || partialBlueBlockIndex2 > 0)) {
        format = BLUE_LEFT_BORDER;
      }
      buf.append(MessageFormat.format(body, new Object[] {format}));
    }

    // Full red blocks
    String fullRedBody = MessageFormat.format(body, new Object[] {partialBlocks + "+0"});
    for (int i = 0; i < fullRedBlockCount; i++) {
      buf.append(fullRedBody);
    }

    // Mixed red/blue block (mid-block transition)
    if (partialRedBlockIndex > 0) {
      String partialBody =
          MessageFormat.format(body, new Object[] {partialRedBlockIndex + "+"
              + partialBlueBlockIndex1});
      buf.append(partialBody);
    }

    // Full blue blocks
    String fullBlueBody = MessageFormat.format(body, new Object[] {"0+" + partialBlocks});
    for (int i = 0; i < fullBlueBlockCount; i++) {
      buf.append(fullBlueBody);
    }

    // Partial blue block
    if (partialBlueBlockIndex2 > 0) {
      String partialBody = MessageFormat.format(body, new Object[] {"0+" + partialBlueBlockIndex2});
      buf.append(partialBody);
    }

    // Empty blocks
    int emptyBlocks =
        showEmptyBlocks ? fullBlocks
            - (fullRedBlockCount + fullBlueBlockCount + (partialRedBlockIndex > 0 ? 1 : 0) + (partialBlueBlockIndex2 > 0 ? 1
                : 0))
            : 0;
    if (emptyBlocks > 0) {
      String emptyBody = MessageFormat.format(body, new Object[] {"0+0"});
      for (int i = 0; i < emptyBlocks; i++) {
        buf.append(emptyBody);
      }
    }

    // End
    if (showB) {
      String format = WHITE_RIGHT_BORDER;
      if (fullRedBlockCount == fullBlocks) {
        format = RED_RIGHT_BORDER;
      } else if (fullRedBlockCount
          + (partialRedBlockIndex + partialBlueBlockIndex1 == partialBlocks ? 1 : 0)
          + fullBlueBlockCount == fullBlocks) {
        format = BLUE_RIGHT_BORDER;
      }
      buf.append(MessageFormat.format(body, new Object[] {format}));
    }

    return buf;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public double getValue2() {
    return value2;
  }

  public void setValue2(double value2) {
    this.value2 = value2;
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
