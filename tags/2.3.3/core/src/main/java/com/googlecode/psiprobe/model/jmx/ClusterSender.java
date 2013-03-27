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
package com.googlecode.psiprobe.model.jmx;

public class ClusterSender {
    private String address;
    private int port;
    private long avgMessageSize;
    private long avgProcessingTime;
    private long connectCounter;
    private long disconnectCounter;
    private boolean connected;
    private long keepAliveTimeout;
    private long nrOfRequests;
    private long totalBytes;
    private boolean resend;
    private boolean suspect;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getAvgMessageSize() {
        return avgMessageSize;
    }

    public void setAvgMessageSize(long avgMessageSize) {
        this.avgMessageSize = avgMessageSize;
    }

    public long getConnectCounter() {
        return connectCounter;
    }

    public void setConnectCounter(long connectCounter) {
        this.connectCounter = connectCounter;
    }

    public long getDisconnectCounter() {
        return disconnectCounter;
    }

    public void setDisconnectCounter(long disconnectCounter) {
        this.disconnectCounter = disconnectCounter;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public long getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public void setKeepAliveTimeout(long keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public long getNrOfRequests() {
        return nrOfRequests;
    }

    public void setNrOfRequests(long nrOfRequests) {
        this.nrOfRequests = nrOfRequests;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public boolean isResend() {
        return resend;
    }

    public void setResend(boolean resend) {
        this.resend = resend;
    }

    public boolean isSuspect() {
        return suspect;
    }

    public void setSuspect(boolean suspect) {
        this.suspect = suspect;
    }

    public long getAvgProcessingTime() {
        return avgProcessingTime;
    }

    public void setAvgProcessingTime(long avgProcessingTime) {
        this.avgProcessingTime = avgProcessingTime;
    }
}
