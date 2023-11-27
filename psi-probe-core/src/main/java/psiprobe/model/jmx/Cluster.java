/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.model.jmx;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Cluster.
 */
public class Cluster {

  /** The name. */
  private String name;

  /** The info. */
  private String info;

  /** The manager class name. */
  private String managerClassName;

  /** The mcast address. */
  private String mcastAddress;

  /** The mcast bind address. */
  private String mcastBindAddress;

  /** The mcast cluster domain. */
  private String mcastClusterDomain;

  /** The mcast drop time. */
  private long mcastDropTime;

  /** The mcast frequency. */
  private long mcastFrequency;

  /** The mcast port. */
  private int mcastPort;

  /** The mcast so timeout. */
  private int mcastSoTimeout;

  /** The mcast ttl. */
  private int mcastTtl;

  /** The members. */
  private List<ClusterSender> members = new ArrayList<>();

  /** The tcp thread count. */
  private int tcpThreadCount;

  /** The tcp listen address. */
  private String tcpListenAddress;

  /** The tcp listen port. */
  private int tcpListenPort;

  /** The tcp selector timeout. */
  private long tcpSelectorTimeout;

  /** The nr of msgs received. */
  private long nrOfMsgsReceived;

  /** The total received bytes. */
  private long totalReceivedBytes;

  /** The sender ack timeout. */
  private long senderAckTimeout;

  /** The sender auto connect. */
  private boolean senderAutoConnect;

  /** The sender failure counter. */
  private long senderFailureCounter;

  /** The sender nr of requests. */
  private long senderNrOfRequests;

  /** The sender replication mode. */
  private String senderReplicationMode;

  /** The sender total bytes. */
  private long senderTotalBytes;

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the info.
   *
   * @return the info
   */
  public String getInfo() {
    return info;
  }

  /**
   * Sets the info.
   *
   * @param info the new info
   */
  public void setInfo(String info) {
    this.info = info;
  }

  /**
   * Gets the manager class name.
   *
   * @return the manager class name
   */
  public String getManagerClassName() {
    return managerClassName;
  }

  /**
   * Sets the manager class name.
   *
   * @param managerClassName the new manager class name
   */
  public void setManagerClassName(String managerClassName) {
    this.managerClassName = managerClassName;
  }

  /**
   * Gets the mcast address.
   *
   * @return the mcast address
   */
  public String getMcastAddress() {
    return mcastAddress;
  }

  /**
   * Sets the mcast address.
   *
   * @param mcastAddress the new mcast address
   */
  public void setMcastAddress(String mcastAddress) {
    this.mcastAddress = mcastAddress;
  }

  /**
   * Gets the mcast bind address.
   *
   * @return the mcast bind address
   */
  public String getMcastBindAddress() {
    return mcastBindAddress;
  }

  /**
   * Sets the mcast bind address.
   *
   * @param mcastBindAddress the new mcast bind address
   */
  public void setMcastBindAddress(String mcastBindAddress) {
    this.mcastBindAddress = mcastBindAddress;
  }

  /**
   * Gets the mcast cluster domain.
   *
   * @return the mcast cluster domain
   */
  public String getMcastClusterDomain() {
    return mcastClusterDomain;
  }

  /**
   * Sets the mcast cluster domain.
   *
   * @param mcastClusterDomain the new mcast cluster domain
   */
  public void setMcastClusterDomain(String mcastClusterDomain) {
    this.mcastClusterDomain = mcastClusterDomain;
  }

  /**
   * Gets the members.
   *
   * @return the members
   */
  public List<ClusterSender> getMembers() {
    return members == null ? null : new ArrayList<>(members);
  }

  /**
   * Sets the members.
   *
   * @param members the new members
   */
  public void setMembers(List<ClusterSender> members) {
    this.members = members == null ? null : new ArrayList<>(members);
  }

  /**
   * Gets the tcp listen address.
   *
   * @return the tcp listen address
   */
  public String getTcpListenAddress() {
    return tcpListenAddress;
  }

  /**
   * Sets the tcp listen address.
   *
   * @param tcpListenAddress the new tcp listen address
   */
  public void setTcpListenAddress(String tcpListenAddress) {
    this.tcpListenAddress = tcpListenAddress;
  }

  /**
   * Gets the tcp listen port.
   *
   * @return the tcp listen port
   */
  public int getTcpListenPort() {
    return tcpListenPort;
  }

  /**
   * Sets the tcp listen port.
   *
   * @param tcpListenPort the new tcp listen port
   */
  public void setTcpListenPort(int tcpListenPort) {
    this.tcpListenPort = tcpListenPort;
  }

  /**
   * Gets the tcp selector timeout.
   *
   * @return the tcp selector timeout
   */
  public long getTcpSelectorTimeout() {
    return tcpSelectorTimeout;
  }

  /**
   * Sets the tcp selector timeout.
   *
   * @param tcpSelectorTimeout the new tcp selector timeout
   */
  public void setTcpSelectorTimeout(long tcpSelectorTimeout) {
    this.tcpSelectorTimeout = tcpSelectorTimeout;
  }

  /**
   * Gets the nr of msgs received.
   *
   * @return the nr of msgs received
   */
  public long getNrOfMsgsReceived() {
    return nrOfMsgsReceived;
  }

  /**
   * Sets the nr of msgs received.
   *
   * @param nrOfMsgsReceived the new nr of msgs received
   */
  public void setNrOfMsgsReceived(long nrOfMsgsReceived) {
    this.nrOfMsgsReceived = nrOfMsgsReceived;
  }

  /**
   * Gets the sender ack timeout.
   *
   * @return the sender ack timeout
   */
  public long getSenderAckTimeout() {
    return senderAckTimeout;
  }

  /**
   * Sets the sender ack timeout.
   *
   * @param senderAckTimeout the new sender ack timeout
   */
  public void setSenderAckTimeout(long senderAckTimeout) {
    this.senderAckTimeout = senderAckTimeout;
  }

  /**
   * Checks if is sender auto connect.
   *
   * @return true, if is sender auto connect
   */
  public boolean isSenderAutoConnect() {
    return senderAutoConnect;
  }

  /**
   * Sets the sender auto connect.
   *
   * @param senderAutoConnect the new sender auto connect
   */
  public void setSenderAutoConnect(boolean senderAutoConnect) {
    this.senderAutoConnect = senderAutoConnect;
  }

  /**
   * Gets the sender failure counter.
   *
   * @return the sender failure counter
   */
  public long getSenderFailureCounter() {
    return senderFailureCounter;
  }

  /**
   * Sets the sender failure counter.
   *
   * @param senderFailureCounter the new sender failure counter
   */
  public void setSenderFailureCounter(long senderFailureCounter) {
    this.senderFailureCounter = senderFailureCounter;
  }

  /**
   * Gets the sender nr of requests.
   *
   * @return the sender nr of requests
   */
  public long getSenderNrOfRequests() {
    return senderNrOfRequests;
  }

  /**
   * Sets the sender nr of requests.
   *
   * @param senderNrOfRequests the new sender nr of requests
   */
  public void setSenderNrOfRequests(long senderNrOfRequests) {
    this.senderNrOfRequests = senderNrOfRequests;
  }

  /**
   * Gets the sender replication mode.
   *
   * @return the sender replication mode
   */
  public String getSenderReplicationMode() {
    return senderReplicationMode;
  }

  /**
   * Sets the sender replication mode.
   *
   * @param senderReplicationMode the new sender replication mode
   */
  public void setSenderReplicationMode(String senderReplicationMode) {
    this.senderReplicationMode = senderReplicationMode;
  }

  /**
   * Gets the sender total bytes.
   *
   * @return the sender total bytes
   */
  public long getSenderTotalBytes() {
    return senderTotalBytes;
  }

  /**
   * Sets the sender total bytes.
   *
   * @param senderTotalBytes the new sender total bytes
   */
  public void setSenderTotalBytes(long senderTotalBytes) {
    this.senderTotalBytes = senderTotalBytes;
  }

  /**
   * Gets the mcast drop time.
   *
   * @return the mcast drop time
   */
  public long getMcastDropTime() {
    return mcastDropTime;
  }

  /**
   * Sets the mcast drop time.
   *
   * @param mcastDropTime the new mcast drop time
   */
  public void setMcastDropTime(long mcastDropTime) {
    this.mcastDropTime = mcastDropTime;
  }

  /**
   * Gets the mcast frequency.
   *
   * @return the mcast frequency
   */
  public long getMcastFrequency() {
    return mcastFrequency;
  }

  /**
   * Sets the mcast frequency.
   *
   * @param mcastFrequency the new mcast frequency
   */
  public void setMcastFrequency(long mcastFrequency) {
    this.mcastFrequency = mcastFrequency;
  }

  /**
   * Gets the mcast port.
   *
   * @return the mcast port
   */
  public int getMcastPort() {
    return mcastPort;
  }

  /**
   * Sets the mcast port.
   *
   * @param mcastPort the new mcast port
   */
  public void setMcastPort(int mcastPort) {
    this.mcastPort = mcastPort;
  }

  /**
   * Gets the mcast so timeout.
   *
   * @return the mcast so timeout
   */
  public int getMcastSoTimeout() {
    return mcastSoTimeout;
  }

  /**
   * Sets the mcast so timeout.
   *
   * @param mcastSoTimeout the new mcast so timeout
   */
  public void setMcastSoTimeout(int mcastSoTimeout) {
    this.mcastSoTimeout = mcastSoTimeout;
  }

  /**
   * Gets the mcast ttl.
   *
   * @return the mcast ttl
   */
  public int getMcastTtl() {
    return mcastTtl;
  }

  /**
   * Sets the mcast ttl.
   *
   * @param mcastTtl the new mcast ttl
   */
  public void setMcastTtl(int mcastTtl) {
    this.mcastTtl = mcastTtl;
  }

  /**
   * Gets the tcp thread count.
   *
   * @return the tcp thread count
   */
  public int getTcpThreadCount() {
    return tcpThreadCount;
  }

  /**
   * Sets the tcp thread count.
   *
   * @param tcpThreadCount the new tcp thread count
   */
  public void setTcpThreadCount(int tcpThreadCount) {
    this.tcpThreadCount = tcpThreadCount;
  }

  /**
   * Gets the total received bytes.
   *
   * @return the total received bytes
   */
  public long getTotalReceivedBytes() {
    return totalReceivedBytes;
  }

  /**
   * Sets the total received bytes.
   *
   * @param totalReceivedBytes the new total received bytes
   */
  public void setTotalReceivedBytes(long totalReceivedBytes) {
    this.totalReceivedBytes = totalReceivedBytes;
  }

}
