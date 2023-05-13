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
import java.util.LinkedList;
import java.util.Map;

import psiprobe.Utils;

/**
 * The listener interface for receiving flap events. The class that is interested in processing a
 * flap event implements this interface, and the object created with that class is registered with a
 * component using the component's {@code addFlapListener} method. When the flap event occurs, that
 * object's appropriate method is invoked.
 *
 * @see <a href="https://assets.nagios.com/downloads/nagioscore/docs/nagioscore/3/en/flapping.html">
 *      Detection and Handling of State Flapping (nagios)</a>
 */
public abstract class AbstractFlapListener extends AbstractThresholdListener {

  /** The default flap interval. */
  private int defaultFlapInterval;

  /** The default flap start threshold. */
  private float defaultFlapStartThreshold;

  /** The default flap stop threshold. */
  private float defaultFlapStopThreshold;

  /** The default flap low weight. */
  private float defaultFlapLowWeight;

  /** The default flap high weight. */
  private float defaultFlapHighWeight;

  /** The flaps. */
  private final Map<String, LinkedList<Boolean>> flaps = new HashMap<>();

  /** The flapping states. */
  private final Map<String, Boolean> flappingStates = new HashMap<>();

  /**
   * Flapping started.
   *
   * @param sce the sce
   */
  protected abstract void flappingStarted(StatsCollectionEvent sce);

  /**
   * Above threshold flapping stopped.
   *
   * @param sce the sce
   */
  protected abstract void aboveThresholdFlappingStopped(StatsCollectionEvent sce);

  /**
   * Below threshold flapping stopped.
   *
   * @param sce the sce
   */
  protected abstract void belowThresholdFlappingStopped(StatsCollectionEvent sce);

  /**
   * Above threshold not flapping.
   *
   * @param sce the sce
   */
  protected abstract void aboveThresholdNotFlapping(StatsCollectionEvent sce);

  /**
   * Below threshold not flapping.
   *
   * @param sce the sce
   */
  protected abstract void belowThresholdNotFlapping(StatsCollectionEvent sce);

  @Override
  protected void crossedAboveThreshold(StatsCollectionEvent sce) {
    statsCollected(sce, true, true);
  }

  @Override
  protected void crossedBelowThreshold(StatsCollectionEvent sce) {
    statsCollected(sce, true, false);
  }

  @Override
  protected void remainedAboveThreshold(StatsCollectionEvent sce) {
    statsCollected(sce, false, true);
  }

  @Override
  protected void remainedBelowThreshold(StatsCollectionEvent sce) {
    statsCollected(sce, false, false);
  }

  @Override
  public void reset() {
    flaps.clear();
    flappingStates.clear();
    super.reset();
  }

  /**
   * Stats collected.
   *
   * @param sce the sce
   * @param crossedThreshold the crossed threshold
   * @param above the above
   */
  protected void statsCollected(StatsCollectionEvent sce, boolean crossedThreshold, boolean above) {
    String name = sce.getName();
    boolean flappingStateChanged = checkFlappingStateChanged(name, crossedThreshold);
    boolean flappingState = getFlappingState(name);
    if (flappingStateChanged) {
      if (flappingState) {
        flappingStarted(sce);
      } else if (above) {
        aboveThresholdFlappingStopped(sce);
      } else {
        belowThresholdFlappingStopped(sce);
      }
    } else if (crossedThreshold) {
      if (above) {
        aboveThresholdNotFlapping(sce);
      } else {
        belowThresholdNotFlapping(sce);
      }
    }
  }

  /**
   * Check flapping state changed.
   *
   * @param name the name
   * @param crossedThreshold the crossed threshold
   *
   * @return true, if successful
   */
  protected boolean checkFlappingStateChanged(String name, boolean crossedThreshold) {
    addFlap(name, crossedThreshold);
    boolean oldFlappingState = getFlappingState(name);
    float transitionPercent = calculateStateTransitionPercentage(name, oldFlappingState);
    boolean newFlappingState;
    if (oldFlappingState) {
      newFlappingState = transitionPercent <= getFlapStopThreshold(name);
    } else {
      newFlappingState = transitionPercent > getFlapStartThreshold(name);
    }
    setFlappingState(name, newFlappingState);
    return oldFlappingState != newFlappingState;
  }

  /**
   * Calculate state transition percentage.
   *
   * @param name the name
   * @param flapping the flapping
   *
   * @return the float
   */
  protected float calculateStateTransitionPercentage(String name, boolean flapping) {
    int flapInterval = getFlapInterval(name);
    LinkedList<Boolean> list = getFlaps(name);
    float lowWeight = getFlapLowWeight(name);
    float highWeight = getFlapHighWeight(name);
    float weightRange = highWeight - lowWeight;
    float result = 0;
    for (int i = list.size() - 1; i >= 0; i--) {
      boolean thisFlap = list.get(i);
      if (flapping != thisFlap) {
        float weight = lowWeight + weightRange * i / (flapInterval - 1);
        result += weight;
      }
    }
    return result / flapInterval;
  }

  /**
   * Adds the flap.
   *
   * @param name the name
   * @param flap the flap
   */
  protected void addFlap(String name, boolean flap) {
    int flapInterval = getFlapInterval(name);
    LinkedList<Boolean> list = getFlaps(name);
    Boolean value = flap;
    list.addLast(value);
    while (list.size() > flapInterval) {
      list.removeFirst();
    }
  }

  /**
   * Gets the flapping state.
   *
   * @param name the name
   *
   * @return the flapping state
   */
  protected boolean getFlappingState(String name) {
    Boolean flapping = flappingStates.get(name);
    if (flapping == null) {
      flapping = Boolean.FALSE;
      setFlappingState(name, false);
    }
    return flapping;
  }

  /**
   * Sets the flapping state.
   *
   * @param name the name
   * @param flapping the flapping
   */
  protected void setFlappingState(String name, boolean flapping) {
    flappingStates.put(name, flapping);
  }

  /**
   * Gets the flaps.
   *
   * @param name the name
   *
   * @return the flaps
   */
  protected LinkedList<Boolean> getFlaps(String name) {
    LinkedList<Boolean> list = flaps.get(name);
    if (list == null) {
      list = new LinkedList<>();
      flaps.put(name, list);
    }
    return list;
  }

  /**
   * Gets the flap interval.
   *
   * @param name the name
   *
   * @return the flap interval
   */
  protected int getFlapInterval(String name) {
    String interval = getPropertyValue(name, "flapInterval");
    return Utils.toInt(interval, getDefaultFlapInterval());
  }

  /**
   * Gets the flap start threshold.
   *
   * @param name the name
   *
   * @return the flap start threshold
   */
  protected float getFlapStartThreshold(String name) {
    String startThreshold = getPropertyValue(name, "flapStartThreshold");
    return Utils.toFloat(startThreshold, getDefaultFlapStartThreshold());
  }

  /**
   * Gets the flap stop threshold.
   *
   * @param name the name
   *
   * @return the flap stop threshold
   */
  protected float getFlapStopThreshold(String name) {
    String stopThreshold = getPropertyValue(name, "flapStopThreshold");
    return Utils.toFloat(stopThreshold, getDefaultFlapStopThreshold());
  }

  /**
   * Gets the flap low weight.
   *
   * @param name the name
   *
   * @return the flap low weight
   */
  protected float getFlapLowWeight(String name) {
    String lowWeight = getPropertyValue(name, "flapLowWeight");
    return Utils.toFloat(lowWeight, getDefaultFlapLowWeight());
  }

  /**
   * Gets the flap high weight.
   *
   * @param name the name
   *
   * @return the flap high weight
   */
  protected float getFlapHighWeight(String name) {
    String highWeight = getPropertyValue(name, "flapHighWeight");
    return Utils.toFloat(highWeight, getDefaultFlapHighWeight());
  }

  /**
   * Gets the default flap interval.
   *
   * @return the default flap interval
   */
  public int getDefaultFlapInterval() {
    return defaultFlapInterval;
  }

  /**
   * Sets the default flap interval.
   *
   * @param defaultFlapInterval the new default flap interval
   */
  public void setDefaultFlapInterval(int defaultFlapInterval) {
    this.defaultFlapInterval = defaultFlapInterval;
  }

  /**
   * Gets the default flap start threshold.
   *
   * @return the default flap start threshold
   */
  public float getDefaultFlapStartThreshold() {
    return defaultFlapStartThreshold;
  }

  /**
   * Sets the default flap start threshold.
   *
   * @param defaultFlapStartThreshold the new default flap start threshold
   */
  public void setDefaultFlapStartThreshold(float defaultFlapStartThreshold) {
    this.defaultFlapStartThreshold = defaultFlapStartThreshold;
  }

  /**
   * Gets the default flap stop threshold.
   *
   * @return the default flap stop threshold
   */
  public float getDefaultFlapStopThreshold() {
    return defaultFlapStopThreshold;
  }

  /**
   * Sets the default flap stop threshold.
   *
   * @param defaultFlapStopThreshold the new default flap stop threshold
   */
  public void setDefaultFlapStopThreshold(float defaultFlapStopThreshold) {
    this.defaultFlapStopThreshold = defaultFlapStopThreshold;
  }

  /**
   * Gets the default flap low weight.
   *
   * @return the default flap low weight
   */
  public float getDefaultFlapLowWeight() {
    return defaultFlapLowWeight;
  }

  /**
   * Sets the default flap low weight.
   *
   * @param defaultFlapLowWeight the new default flap low weight
   */
  public void setDefaultFlapLowWeight(float defaultFlapLowWeight) {
    this.defaultFlapLowWeight = defaultFlapLowWeight;
  }

  /**
   * Gets the default flap high weight.
   *
   * @return the default flap high weight
   */
  public float getDefaultFlapHighWeight() {
    return defaultFlapHighWeight;
  }

  /**
   * Sets the default flap high weight.
   *
   * @param defaultFlapHighWeight the new default flap high weight
   */
  public void setDefaultFlapHighWeight(float defaultFlapHighWeight) {
    this.defaultFlapHighWeight = defaultFlapHighWeight;
  }

}
