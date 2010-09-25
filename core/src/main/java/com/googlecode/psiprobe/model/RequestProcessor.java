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

import java.util.Locale;

/**
 * POJO representing a single http request processor thread.
 *
 * @author Vlad Ilyushchenko
 */
public class RequestProcessor {
    private String name;
    private int stage;
    private long processingTime;
    private long bytesSent;
    private long bytesReceived;
    private String remoteAddr;
    private Locale remoteAddrLocale;
    private String virtualHost;
    private String method;
    private String currentUri;
    private String currentQueryString;
    private String protocol;
    private String workerThreadName;
    private boolean workerThreadNameSupported = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }

    public long getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(long bytesSent) {
        this.bytesSent = bytesSent;
    }

    public long getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(long bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCurrentUri() {
        return currentUri;
    }

    public void setCurrentUri(String currentUri) {
        this.currentUri = currentUri;
    }

    public String getCurrentQueryString() {
        return currentQueryString;
    }

    public void setCurrentQueryString(String currentQueryString) {
        this.currentQueryString = currentQueryString;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Locale getRemoteAddrLocale() {
        return remoteAddrLocale;
    }

    public void setRemoteAddrLocale(Locale remoteAddrLocale) {
        this.remoteAddrLocale = remoteAddrLocale;
    }

    public String getWorkerThreadName() {
        return workerThreadName;
    }

    public void setWorkerThreadName(String workerThreadName) {
        this.workerThreadName = workerThreadName;
    }

    public boolean isWorkerThreadNameSupported() {
        return workerThreadNameSupported;
    }

    public void setWorkerThreadNameSupported(boolean workerThreadNameSupported) {
        this.workerThreadNameSupported = workerThreadNameSupported;
    }
}
