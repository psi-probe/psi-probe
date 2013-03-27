/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.beans;

import com.googlecode.psiprobe.model.DataSourceInfo;
import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * Datasource accessor for the new
 * <a href="http://people.apache.org/~fhanik/jdbc-pool/jdbc-pool.html">Tomcat JDBC pool</a>.
 * 
 * <p>
 * Tomcat 6 can be configured to use the new pool for its datasources instead of
 * the old DBCP-based pool.  SpringSource tc Server uses this new pool
 * <a href="http://vigilbose.blogspot.com/2009/03/apache-commons-dbcp-and-tomcat-jdbc.html">by default</a>.
 * </p>
 *
 * @author chenwang
 */
public class TomcatJdbcPoolDatasourceAccessor implements DatasourceAccessor {

    public DataSourceInfo getInfo(Object resource) throws Exception {
        DataSourceInfo dataSourceInfo = null;
        if (canMap(resource)) {
            DataSource source = (DataSource) resource;
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
        return resource.getClass().getName().equals("org.apache.tomcat.jdbc.pool.DataSource") && resource instanceof DataSource;
    }

}
