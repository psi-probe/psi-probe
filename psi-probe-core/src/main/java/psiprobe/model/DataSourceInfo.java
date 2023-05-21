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
package psiprobe.model;

import psiprobe.UtilsBase;

/**
 * POJO representing a datasource.
 */
public class DataSourceInfo {

  /** The jdbc url. */
  private String jdbcUrl;

  /** The busy connections. */
  private int busyConnections;

  /** The established connections. */
  private int establishedConnections;

  /** The max connections. */
  private int maxConnections;

  /** The resettable. */
  private boolean resettable;

  /** The username. */
  private String username;

  /** The type. */
  private String type;

  /**
   * Gets the jdbc url.
   *
   * @return the jdbc url
   */
  public String getJdbcUrl() {
    return jdbcUrl;
  }

  /**
   * Sets the jdbc url.
   *
   * @param jdbcUrl the new jdbc url
   */
  public void setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  /**
   * Gets the busy connections.
   *
   * @return the busy connections
   */
  public int getBusyConnections() {
    return busyConnections;
  }

  /**
   * Sets the busy connections.
   *
   * @param busyConnections the new busy connections
   */
  public void setBusyConnections(int busyConnections) {
    this.busyConnections = busyConnections;
  }

  /**
   * Gets the established connections.
   *
   * @return the established connections
   */
  public int getEstablishedConnections() {
    return establishedConnections;
  }

  /**
   * Sets the established connections.
   *
   * @param establishedConnections the new established connections
   */
  public void setEstablishedConnections(int establishedConnections) {
    this.establishedConnections = establishedConnections;
  }

  /**
   * Gets the max connections.
   *
   * @return the max connections
   */
  public int getMaxConnections() {
    return maxConnections;
  }

  /**
   * Sets the max connections.
   *
   * @param maxConnections the new max connections
   */
  public void setMaxConnections(int maxConnections) {
    this.maxConnections = maxConnections;
  }

  /**
   * Checks if is resettable.
   *
   * @return true, if is resettable
   */
  public boolean isResettable() {
    return resettable;
  }

  /**
   * Sets the resettable.
   *
   * @param resettable the new resettable
   */
  public void setResettable(boolean resettable) {
    this.resettable = resettable;
  }

  /**
   * Gets the username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username.
   *
   * @param username the new username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type the new type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets the busy score.
   *
   * @return the busy score
   */
  public int getBusyScore() {
    return UtilsBase.calcPoolUsageScore(getMaxConnections(), getBusyConnections());
  }

  /**
   * Gets the established score.
   *
   * @return the established score
   */
  public int getEstablishedScore() {
    return UtilsBase.calcPoolUsageScore(getMaxConnections(), getEstablishedConnections());
  }

}
