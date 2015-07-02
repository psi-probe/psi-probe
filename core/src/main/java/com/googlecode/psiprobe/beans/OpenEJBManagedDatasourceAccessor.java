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
import org.apache.openejb.resource.jdbc.managed.local.ManagedDataSource;
import org.apache.tomcat.jdbc.pool.DataSourceProxy;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;

/**
 * Datasource accessor OpenEJB / TomEE
 *
 * @author Dusan Jakub
 */
public class OpenEJBManagedDatasourceAccessor implements DatasourceAccessor {

    public DataSourceInfo getInfo(Object resource) throws Exception {
        DataSourceInfo dataSourceInfo = null;
        if (canMap(resource)) {
            PoolConfiguration conf = (PoolConfiguration) unwrap(resource);
            DataSourceProxy proxy = (DataSourceProxy) conf;

            dataSourceInfo = new DataSourceInfo();
            dataSourceInfo.setBusyConnections(proxy.getNumActive());
            dataSourceInfo.setEstablishedConnections(proxy.getNumIdle() + proxy.getNumActive());
            dataSourceInfo.setMaxConnections(conf.getMaxActive());
            dataSourceInfo.setJdbcURL(conf.getUrl());
            dataSourceInfo.setUsername(conf.getUsername());
            dataSourceInfo.setResettable(false);
            dataSourceInfo.setType("tomee-jdbc");
        }
        return dataSourceInfo;
    }

    public boolean reset(Object resource) throws Exception {
        return false;
    }

    public boolean canMap(Object resource) {
        Object w;
        return resource.getClass().getName().equals("org.apache.openejb.resource.jdbc.managed.local.ManagedDataSource")
                && (w = unwrap(resource)) instanceof DataSourceProxy
                && w instanceof PoolConfiguration;
    }

    private Object unwrap(Object resource) {
        return ((ManagedDataSource) resource).getDelegate();
    }

}
