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

import java.io.Serializable;

/**
 * POJO representing Tomcat's web application.
 *
 * @author Vlad Ilyushchenko
 */
public class Application implements Serializable {
    private String name;
    private String displayName;
    private String docBase;
    private boolean available;
    private long sessionCount;
    private long sessionAttributeCount;
    private int contextAttributeCount;
    private int dataSourceBusyScore;
    private int dataSourceEstablishedScore;
    private boolean distributable;
    private int sessionTimeout;
    private String servletVersion;
    private boolean serializable;
    private long size;
    private int servletCount;
    private int requestCount;
    private long processingTime;
    private int errorCount;
    private long minTime;
    private long maxTime;
    private long avgTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDocBase() {
        return docBase;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(long sessionCount) {
        this.sessionCount = sessionCount;
    }

    public long getSessionAttributeCount() {
        return sessionAttributeCount;
    }

    public void setSessionAttributeCount(long sessionAttributeCount) {
        this.sessionAttributeCount = sessionAttributeCount;
    }

    public int getContextAttributeCount() {
        return contextAttributeCount;
    }

    public void setContextAttributeCount(int contextAttributeCount) {
        this.contextAttributeCount = contextAttributeCount;
    }

    public int getDataSourceBusyScore() {
        return dataSourceBusyScore;
    }

    public void setDataSourceBusyScore(int dataSourceBusyScore) {
        this.dataSourceBusyScore = dataSourceBusyScore;
    }

    public int getDataSourceEstablishedScore() {
        return dataSourceEstablishedScore;
    }

    public void setDataSourceEstablishedScore(int dataSourceEstablishedScore) {
        this.dataSourceEstablishedScore = dataSourceEstablishedScore;
    }

    public boolean isDistributable() {
        return distributable;
    }

    public void setDistributable(boolean distributable) {
        this.distributable = distributable;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getServletVersion() {
        return servletVersion;
    }

    public void setServletVersion(String servletVersion) {
        this.servletVersion = servletVersion;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void addSize(long size) {
        this.size += size;
    }

    public boolean isSerializable() {
        return serializable;
    }

    public void setSerializable(boolean serializable) {
        this.serializable = serializable;
    }

    public int getServletCount() {
        return servletCount;
    }

    public void setServletCount(int servletCount) {
        this.servletCount = servletCount;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public long getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(long avgTime) {
        this.avgTime = avgTime;
    }
}
