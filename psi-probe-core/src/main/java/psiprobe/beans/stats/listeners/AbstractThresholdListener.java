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
package psiprobe.beans.stats.listeners;

import java.util.HashMap;

import psiprobe.Utils;
import psiprobe.tools.SizeExpression;

/**
 * The listener interface for receiving threshold events. The class that is interested in processing
 * a threshold event implements this interface, and the object created with that class is registered
 * with a component using the component's {@code addThresholdListener} method. When the threshold
 * event occurs, that object's appropriate method is invoked.
 */
public abstract class AbstractThresholdListener extends AbstractStatsCollectionListener {

  /** The Constant DEFAULT_THRESHOLD. */
  public static final long DEFAULT_THRESHOLD = Long.MAX_VALUE;

  /** The Constant DEFAULT_VALUE. */
  public static final long DEFAULT_VALUE = Long.MIN_VALUE;

  /** The previous values. */
  private final HashMap<String, Long> previousValues = new HashMap<>();

  /** The series disabled. */
  private final HashMap<String, Boolean> seriesDisabled = new HashMap<>();

  /**
   * Crossed above threshold.
   *
   * @param sce the sce
   */
  protected abstract void crossedAboveThreshold(StatsCollectionEvent sce);

  /**
   * Crossed below threshold.
   *
   * @param sce the sce
   */
  protected abstract void crossedBelowThreshold(StatsCollectionEvent sce);

  /**
   * Remained above threshold.
   *
   * @param sce the sce
   */
  protected abstract void remainedAboveThreshold(StatsCollectionEvent sce);

  /**
   * Remained below threshold.
   *
   * @param sce the sce
   */
  protected abstract void remainedBelowThreshold(StatsCollectionEvent sce);

  @Override
  public void statsCollected(StatsCollectionEvent sce) {
    String name = sce.getName();
    if (isSeriesDisabled(name)) {
      return;
    }
    long value = sce.getValue();
    if (isValueAboveThreshold(sce)) {
      if (isPreviousValueAboveThreshold(sce)) {
        remainedAboveThreshold(sce);
      } else {
        crossedAboveThreshold(sce);
      }
    } else if (isPreviousValueAboveThreshold(sce)) {
      crossedBelowThreshold(sce);
    } else {
      remainedBelowThreshold(sce);
    }
    setPreviousValue(name, value);
  }

  @Override
  public void reset() {
    previousValues.clear();
    super.reset();
  }

  /**
   * Checks if is previous value above threshold.
   *
   * @param sce the sce
   * @return true, if is previous value above threshold
   */
  protected boolean isPreviousValueAboveThreshold(StatsCollectionEvent sce) {
    String name = sce.getName();
    long threshold = getThreshold(name);
    long previousValue = getPreviousValue(name);
    return previousValue != DEFAULT_VALUE && previousValue > threshold;
  }

  /**
   * Checks if is value above threshold.
   *
   * @param sce the sce
   * @return true, if is value above threshold
   */
  protected boolean isValueAboveThreshold(StatsCollectionEvent sce) {
    String name = sce.getName();
    long value = sce.getValue();
    long threshold = getThreshold(name);
    return value > threshold;
  }

  /**
   * Gets the threshold.
   *
   * @param name the name
   * @return the threshold
   */
  protected long getThreshold(String name) {
    if (isSeriesDisabled(name)) {
      return DEFAULT_THRESHOLD;
    }
    String threshold = getPropertyValue(name, "threshold");
    if (threshold == null && !isSeriesDisabled(name)) {
      logger.info(
          "Required property '{}' is not defined or inherited.  Disabling listener for '{}' series",
          getPropertyKey(name, "threshold"), name);
      setSeriesDisabled(name, true);
      return DEFAULT_THRESHOLD;
    }
    try {
      return SizeExpression.parse(threshold);
    } catch (NumberFormatException ex) {
      logger.trace("", ex);
      return DEFAULT_THRESHOLD;
    }
  }

  /**
   * Gets the previous value.
   *
   * @param name the name
   * @return the previous value
   */
  protected long getPreviousValue(String name) {
    Long value = previousValues.get(name);
    return Utils.toLong(value, DEFAULT_VALUE);
  }

  /**
   * Sets the previous value.
   *
   * @param name the name
   * @param previousValue the previous value
   */
  protected void setPreviousValue(String name, long previousValue) {
    Long value = previousValue;
    previousValues.put(name, value);
  }

  /**
   * Checks if is series disabled.
   *
   * @param name the name
   * @return true, if is series disabled
   */
  protected boolean isSeriesDisabled(String name) {
    Boolean disabled = seriesDisabled.get(name);
    if (disabled == null) {
      disabled = Boolean.FALSE;
    }
    return disabled;
  }

  /**
   * Sets the series disabled.
   *
   * @param name the name
   * @param disabled the disabled
   */
  protected void setSeriesDisabled(String name, boolean disabled) {
    seriesDisabled.put(name, disabled);
  }

}
