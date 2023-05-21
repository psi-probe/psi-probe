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

import java.sql.SQLException;
import java.util.Properties;

import psiprobe.UtilsBase;
import psiprobe.model.DataSourceInfo;

import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;

/**
 * Accesses oracle.jdbc.pool.OracleDataSource.
 *
 * <p>
 * Oracle connection pool is quite different from any other available for Tomcat. Datasources are
 * run by static OracleConnectionCacheManager, whereby application context scope datasource would
 * have a named entry in OracleConnectionCacheManager.
 * </p>
 *
 * <p>
 * Datasources do not have information about pool as such, therefore this accessor has to do quite
 * tedious job of verifying whether the datasource has a record in the cache manager or not. The
 * pool information is subsequently retrieved from the relevant cache manager entry.
 * </p>
 */
public class OracleDatasourceAccessor implements DatasourceAccessor {

  @Override
  public DataSourceInfo getInfo(Object resource) throws SQLException {
    DataSourceInfo dataSourceInfo = null;

    if (canMap(resource)) {
      OracleDataSource source = (OracleDataSource) resource;
      OracleConnectionCacheManager occm =
          OracleConnectionCacheManager.getConnectionCacheManagerInstance();
      Properties cacheProperties = source.getConnectionCacheProperties();
      String cacheName = source.getConnectionCacheName();
      cacheName = cacheName != null && occm.existsCache(cacheName) ? cacheName : null;

      if (cacheProperties != null) {

        dataSourceInfo = new DataSourceInfo();
        if (cacheName != null) {
          dataSourceInfo.setBusyConnections(occm.getNumberOfActiveConnections(cacheName));
          dataSourceInfo.setEstablishedConnections(occm.getNumberOfAvailableConnections(cacheName)
              + dataSourceInfo.getBusyConnections());
        } else {
          dataSourceInfo.setBusyConnections(0);
          dataSourceInfo.setEstablishedConnections(0);
        }

        dataSourceInfo
            .setMaxConnections(UtilsBase.toInt(cacheProperties.getProperty("MaxLimit"), -1));
        dataSourceInfo.setJdbcUrl(source.getURL());
        dataSourceInfo.setUsername(source.getUser());
        dataSourceInfo.setResettable(true);
        dataSourceInfo.setType("oracle-jdbc");
      }
    }
    return dataSourceInfo;
  }

  @Override
  public boolean reset(Object resource) throws SQLException {
    if (canMap(resource)) {
      ((OracleDataSource) resource).close();
      return true;
    }
    return false;
  }

  @Override
  public boolean canMap(Object resource) {
    return "oracle.jdbc.pool.OracleDataSource".equals(resource.getClass().getName())
        && resource instanceof OracleDataSource;
  }
}
