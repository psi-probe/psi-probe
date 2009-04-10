/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://probe.jstripe.com/d/license.shtml
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.jstripe.tomcat.probe.beans;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.jstripe.tomcat.probe.model.DataSourceInfo;

/**
 * Abstraction layer for c3p0. Maps c3p0 datasource properties on our generic DataSourceInfo bean.
 *
 * Author: Vlad Ilyushchenko
 */
public class C3P0DatasourceAccessor implements DatasourceAccessor {

    public DataSourceInfo getInfo(Object resource) throws Exception{
        DataSourceInfo dataSourceInfo = null;

        if (canMap(resource))
        {
            ComboPooledDataSource source = (ComboPooledDataSource) resource;

            dataSourceInfo = new DataSourceInfo();
            dataSourceInfo.setBusyConnections(source.getNumBusyConnections());
            dataSourceInfo.setEstablishedConnections(source.getNumConnections());
            dataSourceInfo.setMaxConnections(source.getMaxPoolSize());
            dataSourceInfo.setJdbcURL(source.getJdbcUrl());
            dataSourceInfo.setUsername(source.getUser());
            dataSourceInfo.setResettable(true);
        }
        return dataSourceInfo;
    }

    public boolean reset(Object resource) throws Exception {
        if (canMap(resource)) {
            ((ComboPooledDataSource) resource).hardReset();
            return true;
        }
        return false;
    }

    public boolean canMap(Object resource) {
        return "com.mchange.v2.c3p0.ComboPooledDataSource".equals(resource.getClass().getName()) && resource instanceof ComboPooledDataSource;
    }
}
