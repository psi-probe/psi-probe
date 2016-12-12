/**
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

import org.jfree.data.xy.XYDataItem;

/**
 * The Class StatsCollectionEvent.
 */
public class StatsCollectionEvent {

  /** The name. */
  private String name;

  /** The data. */
  private XYDataItem data;

  /**
   * Instantiates a new stats collection event.
   */
  public StatsCollectionEvent() {
    // Required due to override
  }

  /**
   * Instantiates a new stats collection event.
   *
   * @param name the name
   * @param data the data
   */
  public StatsCollectionEvent(String name, XYDataItem data) {
    this.name = name;
    this.data = data;
  }

  /**
   * Instantiates a new stats collection event.
   *
   * @param name the name
   * @param time the time
   * @param data the data
   */
  public StatsCollectionEvent(String name, long time, long data) {
    this(name, new XYDataItem(time, data));
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the data.
   *
   * @return the data
   */
  public XYDataItem getData() {
    return data;
  }

  /**
   * Sets the data.
   *
   * @param data the new data
   */
  public void setData(XYDataItem data) {
    this.data = data;
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public long getValue() {
    return getData().getY().longValue();
  }

  /**
   * Gets the time.
   *
   * @return the time
   */
  public long getTime() {
    return getData().getX().longValue();
  }

}
