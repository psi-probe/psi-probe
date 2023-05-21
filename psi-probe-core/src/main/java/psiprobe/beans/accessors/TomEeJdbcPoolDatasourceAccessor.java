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

import org.apache.tomcat.jdbc.pool.DataSource;

import psiprobe.model.DataSourceInfo;

/**
 * Datasource accessor for tomEE. sames as {@link TomEeJdbcPoolDatasourceAccessor} except different
 * string in {@link #canMap(Object)}
 */
public class TomEeJdbcPoolDatasourceAccessor implements DatasourceAccessor {

  @Override
  public DataSourceInfo getInfo(Object resource) {
    DataSourceInfo dataSourceInfo = null;
    if (canMap(resource)) {
      DataSource source = (DataSource) resource;
      dataSourceInfo = new DataSourceInfo();
      dataSourceInfo.setBusyConnections(source.getNumActive());
      dataSourceInfo.setEstablishedConnections(source.getNumIdle() + source.getNumActive());
      dataSourceInfo.setMaxConnections(source.getMaxActive());
      dataSourceInfo.setJdbcUrl(source.getUrl());
      dataSourceInfo.setUsername(source.getUsername());
      dataSourceInfo.setResettable(false);
      dataSourceInfo.setType("tomcat-jdbc");
    }
    return dataSourceInfo;
  }

  @Override
  public boolean reset(Object resource) {
    return false;
  }

  @Override
  public boolean canMap(Object resource) {
    return "org.apache.tomee.jdbc.TomEEDataSourceCreator$TomEEDataSource"
        .equals(resource.getClass().getName()) && resource instanceof DataSource;
  }

}
