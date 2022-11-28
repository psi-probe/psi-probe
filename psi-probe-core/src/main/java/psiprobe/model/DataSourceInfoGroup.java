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

/**
 * This POJO represents a group of datasources. It provides methods for adding values to aggregated
 * totals of the group. The class is a part of the ListAllJdbcResourceGroupsController model.
 */
public class DataSourceInfoGroup extends DataSourceInfo {

  /** The data source count. */
  private int dataSourceCount;

  /**
   * Instantiates a new data source info group.
   */
  public DataSourceInfoGroup() {
    this.setJdbcUrl(null);
    this.setBusyConnections(0);
    this.setEstablishedConnections(0);
    this.setMaxConnections(0);
  }

  /**
   * Instantiates a new data source info group.
   *
   * @param dataSourceInfo the data source info
   *
   * @return the data source info group
   */
  public DataSourceInfoGroup builder(DataSourceInfo dataSourceInfo) {
    this.setJdbcUrl(dataSourceInfo.getJdbcUrl());
    this.setBusyConnections(dataSourceInfo.getBusyConnections());
    this.setEstablishedConnections(dataSourceInfo.getEstablishedConnections());
    this.setMaxConnections(dataSourceInfo.getMaxConnections());
    this.setDataSourceCount(1);
    return this;
  }

  /**
   * Gets the data source count.
   *
   * @return the data source count
   */
  public int getDataSourceCount() {
    return dataSourceCount;
  }

  /**
   * Sets the data source count.
   *
   * @param dataSourceCount the new data source count
   */
  public void setDataSourceCount(int dataSourceCount) {
    this.dataSourceCount = dataSourceCount;
  }

  /**
   * Adds the busy connections.
   *
   * @param busyConnectionsDelta the busy connections delta
   */
  public void addBusyConnections(int busyConnectionsDelta) {
    setBusyConnections(getBusyConnections() + busyConnectionsDelta);
  }

  /**
   * Adds the established connections.
   *
   * @param establishedConnectionsDelta the established connections delta
   */
  public void addEstablishedConnections(int establishedConnectionsDelta) {
    setEstablishedConnections(getEstablishedConnections() + establishedConnectionsDelta);
  }

  /**
   * Adds the max connections.
   *
   * @param maxConnectionsDelta the max connections delta
   */
  public void addMaxConnections(int maxConnectionsDelta) {
    setMaxConnections(getMaxConnections() + maxConnectionsDelta);
  }

  /**
   * Adds the data source count.
   *
   * @param dataSourceCountDelta the data source count delta
   */
  public void addDataSourceCount(int dataSourceCountDelta) {
    setDataSourceCount(getDataSourceCount() + dataSourceCountDelta);
  }

  /**
   * Adds the data source info.
   *
   * @param dataSourceInfoDelta the data source info delta
   */
  public void addDataSourceInfo(DataSourceInfo dataSourceInfoDelta) {
    addBusyConnections(dataSourceInfoDelta.getBusyConnections());
    addEstablishedConnections(dataSourceInfoDelta.getEstablishedConnections());
    addMaxConnections(dataSourceInfoDelta.getMaxConnections());
    addDataSourceCount(1);
  }

}
