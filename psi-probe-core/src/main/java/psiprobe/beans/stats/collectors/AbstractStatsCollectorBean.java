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
package psiprobe.beans.stats.collectors;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.jfree.data.xy.XYDataItem;

import psiprobe.Utils;
import psiprobe.beans.stats.listeners.StatsCollectionEvent;
import psiprobe.beans.stats.listeners.StatsCollectionListener;
import psiprobe.model.stats.StatsCollection;

/**
 * The Class AbstractStatsCollectorBean.
 */
public abstract class AbstractStatsCollectorBean {

  /** The stats collection. */
  @Inject
  private StatsCollection statsCollection;

  /** The max series. */
  private int maxSeries = 240;

  /** The listeners. */
  private List<StatsCollectionListener> listeners;

  /** The previous data. */
  private final Map<String, Long> previousData = new TreeMap<>();

  /** The previous data2 d. */
  private final Map<String, Entry> previousData2D = new TreeMap<>();

  /**
   * Gets the stats collection.
   *
   * @return the stats collection
   */
  public StatsCollection getStatsCollection() {
    return statsCollection;
  }

  /**
   * Sets the stats collection.
   *
   * @param statsCollection the new stats collection
   */
  public void setStatsCollection(StatsCollection statsCollection) {
    this.statsCollection = statsCollection;
  }

  /**
   * Gets the max series.
   *
   * @return the max series
   */
  public int getMaxSeries() {
    return maxSeries;
  }

  /**
   * Sets the max series.
   *
   * @param maxSeries the new max series
   */
  public void setMaxSeries(int maxSeries) {
    this.maxSeries = maxSeries;
  }

  /**
   * Gets the listeners.
   *
   * @return the listeners
   */
  public List<StatsCollectionListener> getListeners() {
    return listeners;
  }

  /**
   * Sets the listeners.
   *
   * @param listeners the new listeners
   */
  public void setListeners(List<StatsCollectionListener> listeners) {
    this.listeners = listeners;
  }

  /**
   * Collect.
   *
   * @throws Exception the exception
   */
  public abstract void collect() throws Exception;

  /**
   * Builds the delta stats.
   *
   * @param name the name
   * @param value the value
   *
   * @return the long
   *
   * @throws InterruptedException the interrupted exception
   */
  protected long buildDeltaStats(String name, long value) throws InterruptedException {
    return buildDeltaStats(name, value, System.currentTimeMillis());
  }

  /**
   * Builds the delta stats.
   *
   * @param name the name
   * @param value the value
   * @param time the time
   *
   * @return the long
   *
   * @throws InterruptedException the interrupted exception
   */
  protected long buildDeltaStats(String name, long value, long time) throws InterruptedException {
    long delta = 0;
    if (statsCollection != null) {
      long previousValue = Utils.toLong(previousData.get(name), 0);
      delta = value - previousValue;
      delta = delta > 0 ? delta : 0;
      buildAbsoluteStats(name, delta, time);
      previousData.put(name, value);
    }
    return delta;
  }

  /**
   * Builds the absolute stats.
   *
   * @param name the name
   * @param value the value
   *
   * @throws InterruptedException the interrupted exception
   */
  protected void buildAbsoluteStats(String name, long value) throws InterruptedException {
    buildAbsoluteStats(name, value, System.currentTimeMillis());
  }

  /**
   * Builds the absolute stats.
   *
   * @param name the name
   * @param value the value
   * @param time the time
   *
   * @throws InterruptedException the interrupted exception
   */
  protected void buildAbsoluteStats(String name, long value, long time)
      throws InterruptedException {

    List<XYDataItem> stats = statsCollection.getStats(name);
    if (stats == null) {
      statsCollection.newStats(name, maxSeries);
    } else {
      XYDataItem data = new XYDataItem(time, value);
      statsCollection.lockForUpdate();
      try {
        stats.add(data);
        houseKeepStats(stats);
      } finally {
        statsCollection.releaseLock();
      }
      if (listeners != null) {
        StatsCollectionEvent event = new StatsCollectionEvent(name, data);
        for (StatsCollectionListener listener : listeners) {
          if (listener.isEnabled()) {
            listener.statsCollected(event);
          }
        }
      }
    }
  }

  /**
   * The Class Entry.
   */
  private static class Entry {

    /** The time. */
    long time;

    /** The value. */
    long value;

    /**
     * Instantiates a new entry.
     */
    public Entry() {
      // Prevent Emulation by Synthetic Accessor
    }

  }

  /**
   * If there is a value indicating the accumulated amount of time spent on something it is possible
   * to build a series of values representing the percentage of time spent on doing something. For
   * example:
   *
   * <p>
   * at point T1 the system has spent A milliseconds performing tasks at point T2 the system has
   * spent B milliseconds performing tasks
   * </p>
   *
   * <p>
   * so between in a timeframe T2-T1 the system spent B-A milliseconds being busy. Thus (B - A)/(T2
   * - T1) * 100 is the percentage of all time the system spent being busy.
   * </p>
   *
   * @param name the name of the value, which allows the collector to be used for multiple values
   * @param value time spent on the task in milliseconds (A or B in the example above)
   * @param time system time in milliseconds (T1 or T2 in the example above)
   *
   * @throws InterruptedException if a lock could not be obtained
   */
  protected void buildTimePercentageStats(String name, long value, long time)
      throws InterruptedException {

    Entry entry = previousData2D.get(name);
    if (entry == null) {
      entry = new Entry();
      entry.value = value;
      entry.time = time;
      previousData2D.put(name, entry);
    } else {
      double valueDelta = (double) value - entry.value;
      double timeDelta = (double) time - entry.time;
      double statValue = valueDelta * 100 / timeDelta;
      statsCollection.lockForUpdate();
      try {
        List<XYDataItem> stats = statsCollection.getStats(name);
        if (stats == null) {
          stats = statsCollection.newStats(name, maxSeries);
        }
        stats.add(stats.size(), new XYDataItem(time, statValue));
        houseKeepStats(stats);
      } finally {
        statsCollection.releaseLock();
      }
    }
  }

  /**
   * Reset stats.
   *
   * @param name the name
   */
  protected void resetStats(String name) {
    statsCollection.resetStats(name);
  }

  /**
   * House keep stats.
   *
   * @param stats the stats
   */
  private void houseKeepStats(List<XYDataItem> stats) {
    while (stats.size() > maxSeries) {
      stats.remove(0);
    }
  }
}
