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
package com.googlecode.psiprobe.model;

import com.googlecode.psiprobe.Utils;

/**
 * POJO representing a datasource.
 *
 * @author Vlad Ilyushchenko
 */
public class DataSourceInfo {
    private String jdbcURL;
    private int busyConnections;
    private int establishedConnections;
    private int maxConnections;
    private boolean resettable;
    private String username;

    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public int getBusyConnections() {
        return busyConnections;
    }

    public void setBusyConnections(int busyConnections) {
        this.busyConnections = busyConnections;
    }

    public int getEstablishedConnections() {
        return establishedConnections;
    }

    public void setEstablishedConnections(int establishedConnections) {
        this.establishedConnections = establishedConnections;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public boolean isResettable() {
        return resettable;
    }

    public void setResettable(boolean resettable) {
        this.resettable = resettable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBusyScore() {
        return Utils.calcPoolUsageScore(getMaxConnections(), getBusyConnections());
    }

    public int getEstablishedScore() {
        return Utils.calcPoolUsageScore(getMaxConnections(), getEstablishedConnections());
    }

}
