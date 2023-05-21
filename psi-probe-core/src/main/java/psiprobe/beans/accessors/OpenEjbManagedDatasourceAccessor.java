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

import org.apache.openejb.resource.jdbc.managed.local.ManagedDataSource;
import org.apache.tomcat.jdbc.pool.DataSourceProxy;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;

import psiprobe.model.DataSourceInfo;

/**
 * Datasource accessor OpenEJB / TomEE.
 */
public class OpenEjbManagedDatasourceAccessor implements DatasourceAccessor {

  @Override
  public DataSourceInfo getInfo(Object resource) {
    DataSourceInfo dataSourceInfo = null;
    if (canMap(resource)) {
      PoolConfiguration conf = (PoolConfiguration) unwrap(resource);
      DataSourceProxy proxy = (DataSourceProxy) conf;

      dataSourceInfo = new DataSourceInfo();
      dataSourceInfo.setBusyConnections(proxy.getNumActive());
      dataSourceInfo.setEstablishedConnections(proxy.getNumIdle() + proxy.getNumActive());
      dataSourceInfo.setMaxConnections(conf.getMaxActive());
      dataSourceInfo.setJdbcUrl(conf.getUrl());
      dataSourceInfo.setUsername(conf.getUsername());
      dataSourceInfo.setResettable(false);
      dataSourceInfo.setType("tomee-jdbc");
    }
    return dataSourceInfo;
  }

  @Override
  public boolean reset(Object resource) {
    return false;
  }

  @Override
  public boolean canMap(Object resource) {
    if ("org.apache.openejb.resource.jdbc.managed.local.ManagedDataSource"
        .equals(resource.getClass().getName())) {
      Object wrapped = unwrap(resource);
      return wrapped instanceof DataSourceProxy && wrapped instanceof PoolConfiguration;
    }
    return false;
  }

  /**
   * Unwrap.
   *
   * @param resource the resource
   *
   * @return the object
   */
  private Object unwrap(Object resource) {
    return ((ManagedDataSource) resource).getDelegate();
  }

}
