package org.jstripe.tomcat.probe.beans;

import org.jstripe.tomcat.probe.model.DataSourceInfo;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: vlad
 * Date: 05-Jan-2006
 * Time: 00:49:42
 * To change this template use File | Settings | File Templates.
 */
public class TomcatDbcpDatasourceAccessor implements DatasourceAccessor {
    public DataSourceInfo getInfo(Object resource) throws Exception {
        DataSourceInfo dataSourceInfo = null;
        if (canMap(resource)) {
            BasicDataSource source = (BasicDataSource) resource;
            dataSourceInfo = new DataSourceInfo();
            dataSourceInfo.setBusyConnections(source.getNumActive());
            dataSourceInfo.setEstablishedConnections(source.getNumIdle() + source.getNumActive());
            dataSourceInfo.setMaxConnections(source.getMaxActive());
            dataSourceInfo.setJdbcURL(source.getUrl());
            dataSourceInfo.setUsername(source.getUsername());
            dataSourceInfo.setResettable(false);
        }
        return dataSourceInfo;
    }

    public boolean reset(Object resource) throws Exception {
        return false;
    }

    public boolean canMap(Object resource) {
        return "org.apache.tomcat.dbcp.dbcp.BasicDataSource".equals(resource.getClass().getName()) && resource instanceof BasicDataSource;
    }
}
