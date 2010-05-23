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

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private String name;
    private String info;
    private String managerClassName;
    private String mcastAddress;
    private String mcastBindAddress;
    private String mcastClusterDomain;
    private long mcastDropTime;
    private long mcastFrequency;
    private int mcastPort;
    private int mcastSoTimeout;
    private int mcastTTL;
    private List members = new ArrayList();
    private int tcpThreadCount;
    private String tcpListenAddress;
    private int tcpListenPort;
    private long tcpSelectorTimeout;
    private long nrOfMsgsReceived;
    private long totalReceivedBytes;
    private long senderAckTimeout;
    private boolean senderAutoConnect;
    private long senderFailureCounter;
    private long senderNrOfRequests;
    private String senderReplicationMode;
    private long senderTotalBytes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getManagerClassName() {
        return managerClassName;
    }

    public void setManagerClassName(String managerClassName) {
        this.managerClassName = managerClassName;
    }

    public String getMcastAddress() {
        return mcastAddress;
    }

    public void setMcastAddress(String mcastAddress) {
        this.mcastAddress = mcastAddress;
    }

    public String getMcastBindAddress() {
        return mcastBindAddress;
    }

    public void setMcastBindAddress(String mcastBindAddress) {
        this.mcastBindAddress = mcastBindAddress;
    }

    public String getMcastClusterDomain() {
        return mcastClusterDomain;
    }

    public void setMcastClusterDomain(String mcastClusterDomain) {
        this.mcastClusterDomain = mcastClusterDomain;
    }


    public List getMembers() {
        return members;
    }

    public void setMembers(List members) {
        this.members = members;
    }

    public String getTcpListenAddress() {
        return tcpListenAddress;
    }

    public void setTcpListenAddress(String tcpListenAddress) {
        this.tcpListenAddress = tcpListenAddress;
    }

    public int getTcpListenPort() {
        return tcpListenPort;
    }

    public void setTcpListenPort(int tcpListenPort) {
        this.tcpListenPort = tcpListenPort;
    }

    public long getTcpSelectorTimeout() {
        return tcpSelectorTimeout;
    }

    public void setTcpSelectorTimeout(long tcpSelectorTimeout) {
        this.tcpSelectorTimeout = tcpSelectorTimeout;
    }

    public long getNrOfMsgsReceived() {
        return nrOfMsgsReceived;
    }

    public void setNrOfMsgsReceived(long nrOfMsgsReceived) {
        this.nrOfMsgsReceived = nrOfMsgsReceived;
    }

    public long getSenderAckTimeout() {
        return senderAckTimeout;
    }

    public void setSenderAckTimeout(long senderAckTimeout) {
        this.senderAckTimeout = senderAckTimeout;
    }

    public boolean isSenderAutoConnect() {
        return senderAutoConnect;
    }

    public void setSenderAutoConnect(boolean senderAutoConnect) {
        this.senderAutoConnect = senderAutoConnect;
    }

    public long getSenderFailureCounter() {
        return senderFailureCounter;
    }

    public void setSenderFailureCounter(long senderFailureCounter) {
        this.senderFailureCounter = senderFailureCounter;
    }

    public long getSenderNrOfRequests() {
        return senderNrOfRequests;
    }

    public void setSenderNrOfRequests(long senderNrOfRequests) {
        this.senderNrOfRequests = senderNrOfRequests;
    }

    public String getSenderReplicationMode() {
        return senderReplicationMode;
    }

    public void setSenderReplicationMode(String senderReplicationMode) {
        this.senderReplicationMode = senderReplicationMode;
    }

    public long getSenderTotalBytes() {
        return senderTotalBytes;
    }

    public void setSenderTotalBytes(long senderTotalBytes) {
        this.senderTotalBytes = senderTotalBytes;
    }

    public long getMcastDropTime() {
        return mcastDropTime;
    }

    public void setMcastDropTime(long mcastDropTime) {
        this.mcastDropTime = mcastDropTime;
    }

    public long getMcastFrequency() {
        return mcastFrequency;
    }

    public void setMcastFrequency(long mcastFrequency) {
        this.mcastFrequency = mcastFrequency;
    }

    public int getMcastPort() {
        return mcastPort;
    }

    public void setMcastPort(int mcastPort) {
        this.mcastPort = mcastPort;
    }

    public int getMcastSoTimeout() {
        return mcastSoTimeout;
    }

    public void setMcastSoTimeout(int mcastSoTimeout) {
        this.mcastSoTimeout = mcastSoTimeout;
    }

    public int getMcastTTL() {
        return mcastTTL;
    }

    public void setMcastTTL(int mcastTTL) {
        this.mcastTTL = mcastTTL;
    }

    public int getTcpThreadCount() {
        return tcpThreadCount;
    }

    public void setTcpThreadCount(int tcpThreadCount) {
        this.tcpThreadCount = tcpThreadCount;
    }

    public long getTotalReceivedBytes() {
        return totalReceivedBytes;
    }

    public void setTotalReceivedBytes(long totalReceivedBytes) {
        this.totalReceivedBytes = totalReceivedBytes;
    }
}
