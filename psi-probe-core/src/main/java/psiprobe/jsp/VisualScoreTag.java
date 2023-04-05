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
package psiprobe.jsp;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.BodyContent;
import jakarta.servlet.jsp.tagext.BodyTagSupport;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * The Class VisualScoreTag.
 */
public class VisualScoreTag extends BodyTagSupport {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -5653846466205838602L;


  /** The Constant WHITE_LEFT_BORDER. */
  private static final String WHITE_LEFT_BORDER = "a0";

  /** The Constant RED_LEFT_BORDER. */
  private static final String RED_LEFT_BORDER = "a1";

  /** The Constant BLUE_LEFT_BORDER. */
  private static final String BLUE_LEFT_BORDER = "a2";

  /** The Constant WHITE_RIGHT_BORDER. */
  private static final String WHITE_RIGHT_BORDER = "b0";

  /** The Constant RED_RIGHT_BORDER. */
  private static final String RED_RIGHT_BORDER = "b1";

  /** The Constant BLUE_RIGHT_BORDER. */
  private static final String BLUE_RIGHT_BORDER = "b2";

  /** Red value. */
  private double value = 0;

  /** Blue value. */
  private double value2 = 0;

  /** The min value. */
  private double minValue = 0;

  /** The max value. */
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

  /** The show empty blocks. */
  private boolean showEmptyBlocks;

  /** The show a. */
  private boolean showA;

  /** The show b. */
  private boolean showB;

  @Override
  public int doAfterBody() throws JspException {
    try (BodyContent bc = getBodyContent()) {
      String body = bc.getString().trim();

      String buf = calculateSuffix(body);

      bc.getEnclosingWriter().print(buf);
    } catch (IOException e) {
      throw new JspException("Exception while writing to client", e);
    }

    return SKIP_BODY;
  }

  /**
   * Calculate suffix.
   *
   * @param body the body
   *
   * @return the string buffer
   */
  String calculateSuffix(String body) {
    if (value < minValue) {
      value = minValue;
    }
    if (value > maxValue) {
      value = maxValue;
    }
    if (value + value2 < minValue || value2 < 0) {
      value2 = 0;
    }
    if (value + value2 > maxValue) {
      value2 = maxValue - value;
    }

    double unitSize = (maxValue - minValue) / (fullBlocks * partialBlocks);
    double blockWidth = unitSize * partialBlocks;

    int redWhole = (int) Math.floor(value / blockWidth);
    int redPart = (int) Math.floor((value - redWhole * blockWidth) / unitSize);
    int bluePart1 =
        redPart > 0 ? Math.min((int) Math.floor(value2 / unitSize), partialBlocks - redPart) : 0;
    int blueWhole = (int) Math.max(0, Math.ceil(value2 / blockWidth) - (redPart > 0 ? 1 : 0));
    int bluePart2 =
        (int) Math.floor((value2 - blueWhole * blockWidth - bluePart1 * unitSize) / unitSize);

    StringBuilder buf = new StringBuilder();

    // Beginning
    if (showA) {
      String format = WHITE_LEFT_BORDER;
      if (redWhole > 0 || redPart > 0) {
        format = RED_LEFT_BORDER;
      } else if (bluePart1 == 0 && (blueWhole > 0 || bluePart2 > 0)) {

        format = BLUE_LEFT_BORDER;
      }
      buf.append(MessageFormat.format(body, format));
    }

    // Full red blocks
    String fullRedBody = MessageFormat.format(body, partialBlocks + "+0");
    for (int i = 0; i < redWhole; i++) {
      buf.append(fullRedBody);
    }

    // Mixed red/blue block (mid-block transition)
    if (redPart > 0) {
      String partialBody = MessageFormat.format(body, redPart + "+" + bluePart1);
      buf.append(partialBody);
    }

    // Full blue blocks
    String fullBlueBody = MessageFormat.format(body, "0+" + partialBlocks);
    for (int i = 0; i < blueWhole; i++) {
      buf.append(fullBlueBody);
    }

    // Partial blue block
    if (bluePart2 > 0) {
      String partialBody = MessageFormat.format(body, "0+" + bluePart2);
      buf.append(partialBody);
    }

    // Empty blocks
    int emptyBlocks = showEmptyBlocks
        ? fullBlocks - (redWhole + blueWhole + (redPart > 0 ? 1 : 0) + (bluePart2 > 0 ? 1 : 0))
        : 0;
    if (emptyBlocks > 0) {
      String emptyBody = MessageFormat.format(body, "0+0");
      for (int i = 0; i < emptyBlocks; i++) {
        buf.append(emptyBody);
      }
    }

    // End
    if (showB) {
      String format = WHITE_RIGHT_BORDER;
      if (redWhole == fullBlocks) {
        format = RED_RIGHT_BORDER;
      } else if (redWhole + (redPart + bluePart1 == partialBlocks ? 1 : 0)
          + blueWhole == fullBlocks) {
        format = BLUE_RIGHT_BORDER;
      }
      buf.append(MessageFormat.format(body, format));
    }

    return buf.toString();
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public double getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value the new value
   */
  public void setValue(double value) {
    this.value = value;
  }

  /**
   * Gets the value2.
   *
   * @return the value2
   */
  public double getValue2() {
    return value2;
  }

  /**
   * Sets the value2.
   *
   * @param value2 the new value2
   */
  public void setValue2(double value2) {
    this.value2 = value2;
  }

  /**
   * Gets the min value.
   *
   * @return the min value
   */
  public double getMinValue() {
    return minValue;
  }

  /**
   * Sets the min value.
   *
   * @param minValue the new min value
   */
  public void setMinValue(double minValue) {
    this.minValue = minValue;
  }

  /**
   * Gets the max value.
   *
   * @return the max value
   */
  public double getMaxValue() {
    return maxValue;
  }

  /**
   * Sets the max value.
   *
   * @param maxValue the new max value
   */
  public void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Gets the partial blocks.
   *
   * @return the partial blocks
   */
  public int getPartialBlocks() {
    return partialBlocks;
  }

  /**
   * Sets the partial blocks.
   *
   * @param partialBlocks the new partial blocks
   */
  public void setPartialBlocks(int partialBlocks) {
    this.partialBlocks = partialBlocks;
  }

  /**
   * Gets the full blocks.
   *
   * @return the full blocks
   */
  public int getFullBlocks() {
    return fullBlocks;
  }

  /**
   * Sets the full blocks.
   *
   * @param fullBlocks the new full blocks
   */
  public void setFullBlocks(int fullBlocks) {
    this.fullBlocks = fullBlocks;
  }

  /**
   * Checks if is show empty blocks.
   *
   * @return true, if is show empty blocks
   */
  public boolean isShowEmptyBlocks() {
    return showEmptyBlocks;
  }

  /**
   * Sets the show empty blocks.
   *
   * @param showEmptyBlocks the new show empty blocks
   */
  public void setShowEmptyBlocks(boolean showEmptyBlocks) {
    this.showEmptyBlocks = showEmptyBlocks;
  }

  /**
   * Checks if is show a.
   *
   * @return true, if is show a
   */
  public boolean isShowA() {
    return showA;
  }

  /**
   * Sets the show a.
   *
   * @param showA the new show a
   */
  public void setShowA(boolean showA) {
    this.showA = showA;
  }

  /**
   * Checks if is show b.
   *
   * @return true, if is show b
   */
  public boolean isShowB() {
    return showB;
  }

  /**
   * Sets the show b.
   *
   * @param showB the new show b
   */
  public void setShowB(boolean showB) {
    this.showB = showB;
  }

}
