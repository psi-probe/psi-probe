/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.beans;

import com.googlecode.psiprobe.model.DataSourceInfo;

import oracle.ucp.jdbc.JDBCConnectionPoolStatistics;
import oracle.ucp.jdbc.PoolDataSource;

/**
 * Accesses an Oracle Universal Connection Pool (UCP) resource.
 * 
 * @author polaris.keith
 * @author Mark Lewis
 */
public class OracleUcpDatasourceAssessor implements DatasourceAccessor {

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.beans.DatasourceAccessor#getInfo(java.lang.Object)
   */
  public DataSourceInfo getInfo(Object resource) throws Exception {
    DataSourceInfo dataSourceInfo = null;
    if (canMap(resource)) {
      PoolDataSource source = (PoolDataSource) resource;
      JDBCConnectionPoolStatistics stats = source.getStatistics();

      dataSourceInfo = new DataSourceInfo();
      /*
       * If the pool starts with 0 instances, the JDBCConnectionPoolStatistics will be null. The
       * JDBCConnectionPoolStatistics only initializes when there is at least one connection
       * instance created.
       */
      if (stats != null) {
        dataSourceInfo.setBusyConnections(stats.getBorrowedConnectionsCount());
        dataSourceInfo.setEstablishedConnections(stats.getTotalConnectionsCount());
      } else {
        dataSourceInfo.setBusyConnections(0);
        dataSourceInfo.setEstablishedConnections(0);
      }
      dataSourceInfo.setMaxConnections(source.getMaxPoolSize());
      dataSourceInfo.setJdbcUrl(source.getURL());
      dataSourceInfo.setUsername(source.getUser());
      dataSourceInfo.setResettable(false);
      dataSourceInfo.setType("oracle-ucp");
    }
    return dataSourceInfo;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.beans.DatasourceAccessor#reset(java.lang.Object)
   */
  public boolean reset(Object resource) throws Exception {
    return false;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.beans.DatasourceAccessor#canMap(java.lang.Object)
   */
  public boolean canMap(Object resource) {
    return ("oracle.ucp.jdbc.PoolDataSourceImpl".equals(resource.getClass().getName())
        || "oracle.ucp.jdbc.PoolXADataSourceImpl".equals(resource.getClass().getName()))
        && resource instanceof PoolDataSource;
  }

}
