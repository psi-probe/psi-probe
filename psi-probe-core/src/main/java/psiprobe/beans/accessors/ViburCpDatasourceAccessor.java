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

import java.lang.management.ManagementFactory;
import java.sql.SQLException;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.vibur.dbcp.ViburDBCPDataSource;
import org.vibur.dbcp.ViburMonitoringMBean;

import psiprobe.model.DataSourceInfo;

/**
 * The Class ViburCpDatasourceAccessor.
 */
public class ViburCpDatasourceAccessor implements DatasourceAccessor {

  @Override
  public DataSourceInfo getInfo(final Object resource) throws SQLException {
    DataSourceInfo dataSourceInfo = null;
    if (canMap(resource)) {
      ViburDBCPDataSource source = (ViburDBCPDataSource) resource;

      MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
      ObjectName poolName;
      try {
        poolName = new ObjectName(source.getJmxName());
      } catch (MalformedObjectNameException e) {
        throw new SQLException("MalformedObjectNameException for Vibur", e);
      }
      ViburMonitoringMBean poolProxy =
          JMX.newMXBeanProxy(mbeanServer, poolName, ViburMonitoringMBean.class);

      dataSourceInfo = new DataSourceInfo();
      dataSourceInfo.setBusyConnections(poolProxy.getPoolTaken());
      dataSourceInfo.setEstablishedConnections(
          poolProxy.getPoolRemainingCreated() + poolProxy.getPoolTaken());
      dataSourceInfo.setMaxConnections(source.getPoolMaxSize());
      dataSourceInfo.setJdbcUrl(source.getJdbcUrl());
      dataSourceInfo.setUsername(source.getUsername());
      dataSourceInfo.setResettable(false);
      dataSourceInfo.setType("vibur");
    }
    return dataSourceInfo;
  }

  @Override
  public boolean reset(final Object resource) {
    return false;
  }

  @Override
  public boolean canMap(final Object resource) {
    return "org.vibur.dbcp.ViburDBCPDataSource".equals(resource.getClass().getName())
        && resource instanceof ViburDBCPDataSource;
  }

}
