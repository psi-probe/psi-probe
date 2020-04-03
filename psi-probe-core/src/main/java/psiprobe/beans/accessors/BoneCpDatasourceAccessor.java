/**
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

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPDataSource;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import psiprobe.model.DataSourceInfo;

/**
 * The Class BoneCpDatasourceAccessor.
 */
public class BoneCpDatasourceAccessor implements DatasourceAccessor {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(BoneCpDatasourceAccessor.class);

  @Override
  public DataSourceInfo getInfo(final Object resource) throws Exception {
    DataSourceInfo dataSourceInfo = null;
    if (canMap(resource)) {
      final BoneCPDataSource source = (BoneCPDataSource) resource;
      BoneCP pool;
      try {
        pool = source.getPool();
      } catch (NoSuchMethodError ex) {
        logger.trace("", ex);
        // This is an older version of BoneCP (pre-0.8.0)
        final Field poolField = BoneCPDataSource.class.getDeclaredField("pool");
        poolField.setAccessible(true);
        pool = (BoneCP) poolField.get(source);
      }

      dataSourceInfo = new DataSourceInfo();
      dataSourceInfo.setBusyConnections(source.getTotalLeased());

      if (pool == null) {
        // pool is coming up null....
        dataSourceInfo.setEstablishedConnections(source.getMinConnectionsPerPartition());
        logger.warn("pool is null {}", source.getJdbcUrl());
      } else {
        dataSourceInfo.setEstablishedConnections(pool.getTotalCreatedConnections());
      }

      dataSourceInfo
          .setMaxConnections(source.getPartitionCount() * source.getMaxConnectionsPerPartition());
      dataSourceInfo.setJdbcUrl(source.getJdbcUrl());
      dataSourceInfo.setUsername(source.getUsername());
      dataSourceInfo.setResettable(false);
      dataSourceInfo.setType("bonecp");
    }
    return dataSourceInfo;
  }

  @Override
  public boolean reset(final Object resource) throws Exception {
    return false;
  }

  @Override
  public boolean canMap(final Object resource) {
    return "com.jolbox.bonecp.BoneCPDataSource".equals(resource.getClass().getName())
        && resource instanceof BoneCPDataSource;
  }

}
