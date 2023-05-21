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

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import java.lang.management.ManagementFactory;
import java.sql.SQLException;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import psiprobe.model.DataSourceInfo;

/**
 * The Class HikariCpDatasourceAccessor.
 */
public class HikariCpDatasourceAccessor implements DatasourceAccessor {

  @Override
  public DataSourceInfo getInfo(final Object resource) throws SQLException {
    DataSourceInfo dataSourceInfo = null;
    if (canMap(resource)) {
      HikariDataSource source = (HikariDataSource) resource;

      MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
      ObjectName poolName;
      try {
        poolName = new ObjectName("com.zaxxer.hikari:type=Pool (" + source.getPoolName() + ")");
      } catch (MalformedObjectNameException e) {
        throw new SQLException("MalformedObjectNameException for Hikari", e);
      }
      HikariPoolMXBean poolProxy =
          JMX.newMXBeanProxy(mbeanServer, poolName, HikariPoolMXBean.class);

      dataSourceInfo = new DataSourceInfo();
      dataSourceInfo.setBusyConnections(poolProxy.getActiveConnections());
      dataSourceInfo.setEstablishedConnections(poolProxy.getTotalConnections());
      dataSourceInfo.setMaxConnections(source.getMaximumPoolSize());
      dataSourceInfo.setJdbcUrl(source.getJdbcUrl());
      dataSourceInfo.setUsername(source.getUsername());
      dataSourceInfo.setResettable(false);
      dataSourceInfo.setType("hikari");
    }
    return dataSourceInfo;
  }

  @Override
  public boolean reset(final Object resource) {
    return false;
  }

  @Override
  public boolean canMap(final Object resource) {
    return "com.zaxxer.hikari.HikariDataSource".equals(resource.getClass().getName())
        && resource instanceof HikariDataSource;
  }

}
