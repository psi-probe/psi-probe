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
package psiprobe.beans.accessors;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.SQLException;

import psiprobe.model.DataSourceInfo;

/**
 * Abstraction layer for c3p0. Maps c3p0 datasource properties on our generic DataSourceInfo bean.
 */
public class C3P0DatasourceAccessor implements DatasourceAccessor {

  @Override
  public DataSourceInfo getInfo(Object resource) throws SQLException {
    DataSourceInfo dataSourceInfo = null;
    if (canMap(resource)) {
      ComboPooledDataSource source = (ComboPooledDataSource) resource;

      dataSourceInfo = new DataSourceInfo();
      dataSourceInfo.setBusyConnections(source.getNumBusyConnections());
      dataSourceInfo.setEstablishedConnections(source.getNumConnections());
      dataSourceInfo.setMaxConnections(source.getMaxPoolSize());
      dataSourceInfo.setJdbcUrl(source.getJdbcUrl());
      dataSourceInfo.setUsername(source.getUser());
      dataSourceInfo.setResettable(true);
      dataSourceInfo.setType("c3p0");
    }
    return dataSourceInfo;
  }

  @Override
  public boolean reset(Object resource) {
    if (canMap(resource)) {
      ((ComboPooledDataSource) resource).hardReset();
      return true;
    }
    return false;
  }

  @Override
  public boolean canMap(Object resource) {
    return "com.mchange.v2.c3p0.ComboPooledDataSource".equals(resource.getClass().getName())
        && resource instanceof ComboPooledDataSource;
  }
}
