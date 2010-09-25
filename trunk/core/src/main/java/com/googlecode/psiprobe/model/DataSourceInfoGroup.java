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
package com.googlecode.psiprobe.model;

/**
 * This POJO represents a group of datasources. It provides methods for adding
 * values to aggregated totals of the group.  The class is a part of the
 * ListAllJdbcResourceGroups controller model.
 *
 * @author Andy Shapoval
 */

public class DataSourceInfoGroup extends DataSourceInfo {
    private int dataSourceCount = 0;

    public DataSourceInfoGroup() {
        this.setJdbcURL(null);
        this.setBusyConnections(0);
        this.setEstablishedConnections(0);
        this.setMaxConnections(0);
    }

    public DataSourceInfoGroup(DataSourceInfo dataSourceInfo) {
        this.setJdbcURL(dataSourceInfo.getJdbcURL());
        this.setBusyConnections(dataSourceInfo.getBusyConnections());
        this.setEstablishedConnections(dataSourceInfo.getEstablishedConnections());
        this.setMaxConnections(dataSourceInfo.getMaxConnections());
        this.setDataSourceCount(1);
    }

    public int getDataSourceCount() {
        return dataSourceCount;
    }

    public void setDataSourceCount(int dataSourceCount) {
        this.dataSourceCount = dataSourceCount;
    }

    public void addBusyConnections(int busyConnectionsDelta) {
        setBusyConnections(getBusyConnections() + busyConnectionsDelta);
    }

    public void addEstablishedConnections(int establishedConnectionsDelta) {
        setEstablishedConnections(getEstablishedConnections() + establishedConnectionsDelta);
    }

    public void addMaxConnections(int maxConnectionsDelta) {
        setMaxConnections(getMaxConnections() + maxConnectionsDelta);
    }

    public void addDataSourceCount(int dataSourceCountDelta) {
        setDataSourceCount(getDataSourceCount() + dataSourceCountDelta);
    }

    public void addDataSourceInfo(DataSourceInfo dataSourceInfoDelta) {
        addBusyConnections(dataSourceInfoDelta.getBusyConnections());
        addEstablishedConnections(dataSourceInfoDelta.getEstablishedConnections());
        addMaxConnections(dataSourceInfoDelta.getMaxConnections());
        addDataSourceCount(1);
    }
}
