package com.googlecode.psiprobe.beans;

import oracle.ucp.jdbc.JDBCConnectionPoolStatistics;
import oracle.ucp.jdbc.PoolDataSource;

import com.googlecode.psiprobe.model.DataSourceInfo;

/**
 * Accesses an Oracle Universal Connection Pool (UCP) resource.
 * 
 * @author polaris.keith
 * @author Mark Lewis
 */
public class OracleUcpDatasourceAssessor implements DatasourceAccessor {

    public DataSourceInfo getInfo(Object resource) throws Exception {
        DataSourceInfo dataSourceInfo = null;
        if (canMap(resource)) {
            PoolDataSource source = (PoolDataSource) resource;
            JDBCConnectionPoolStatistics stats = source.getStatistics();

            dataSourceInfo = new DataSourceInfo();
            /*
             * If the pool starts with 0 instances, the JDBCConnectionPoolStatistics will be null.
             * The JDBCConnectionPoolStatistics only initializes when there is
             * at least one  connection instance created.
             */
            if (stats != null) {
                dataSourceInfo.setBusyConnections(stats.getBorrowedConnectionsCount());
                dataSourceInfo.setEstablishedConnections(stats.getTotalConnectionsCount());
            } else {
                dataSourceInfo.setBusyConnections(0);
                dataSourceInfo.setEstablishedConnections(0);
            }
            dataSourceInfo.setMaxConnections(source.getMaxPoolSize());
            dataSourceInfo.setJdbcURL(source.getURL());
            dataSourceInfo.setUsername(source.getUser());
            dataSourceInfo.setResettable(false);
        }
        return dataSourceInfo;
    }

    public boolean reset(Object resource) throws Exception {
        return false;
    }

    public boolean canMap(Object resource) {
        return "oracle.ucp.jdbc.PoolDataSource".equals(resource.getClass().getName())
                && resource instanceof PoolDataSource;
    }

}
