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
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPDataSource;
import java.lang.reflect.Field;

/**
 *
 * @author akhawatrah
 */
public class BoneCPDatasourceAccessor implements DatasourceAccessor {

    public DataSourceInfo getInfo(final Object resource) throws Exception {
        DataSourceInfo dataSourceInfo = null;
        if (canMap(resource)) {
            final BoneCPDataSource source = (BoneCPDataSource) resource;
            final Field poolField = BoneCPDataSource.class.getDeclaredField("pool");
            poolField.setAccessible(true);
            final BoneCP pool = (BoneCP) poolField.get(source);

            dataSourceInfo = new DataSourceInfo();
            dataSourceInfo.setBusyConnections(source.getTotalLeased());
            dataSourceInfo.setEstablishedConnections(pool.getTotalCreatedConnections());
            dataSourceInfo.setMaxConnections(source.getPartitionCount() * source.getMaxConnectionsPerPartition());
            dataSourceInfo.setJdbcURL(source.getJdbcUrl());
            dataSourceInfo.setUsername(source.getUsername());
            dataSourceInfo.setResettable(false);
        }
        return dataSourceInfo;
    }

    public boolean reset(final Object resource) throws Exception {
        return false;
    }

    public boolean canMap(final Object resource) {
        return "com.jolbox.bonecp.BoneCPDataSource".equals(resource.getClass().getName())
                && resource instanceof BoneCPDataSource;
    }

}