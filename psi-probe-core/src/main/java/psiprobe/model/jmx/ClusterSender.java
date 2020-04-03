/**
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

/**
 * The Class ClusterSender.
 */
public class ClusterSender {

  /** The address. */
  private String address;

  /** The port. */
  private int port;

  /** The avg message size. */
  private long avgMessageSize;

  /** The avg processing time. */
  private long avgProcessingTime;

  /** The connect counter. */
  private long connectCounter;

  /** The disconnect counter. */
  private long disconnectCounter;

  /** The connected. */
  private boolean connected;

  /** The keep alive timeout. */
  private long keepAliveTimeout;

  /** The nr of requests. */
  private long nrOfRequests;

  /** The total bytes. */
  private long totalBytes;

  /** The resend. */
  private boolean resend;

  /** The suspect. */
  private boolean suspect;

  /**
   * Gets the address.
   *
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address.
   *
   * @param address the new address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Gets the port.
   *
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   * Sets the port.
   *
   * @param port the new port
   */
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * Gets the avg message size.
   *
   * @return the avg message size
   */
  public long getAvgMessageSize() {
    return avgMessageSize;
  }

  /**
   * Sets the avg message size.
   *
   * @param avgMessageSize the new avg message size
   */
  public void setAvgMessageSize(long avgMessageSize) {
    this.avgMessageSize = avgMessageSize;
  }

  /**
   * Gets the connect counter.
   *
   * @return the connect counter
   */
  public long getConnectCounter() {
    return connectCounter;
  }

  /**
   * Sets the connect counter.
   *
   * @param connectCounter the new connect counter
   */
  public void setConnectCounter(long connectCounter) {
    this.connectCounter = connectCounter;
  }

  /**
   * Gets the disconnect counter.
   *
   * @return the disconnect counter
   */
  public long getDisconnectCounter() {
    return disconnectCounter;
  }

  /**
   * Sets the disconnect counter.
   *
   * @param disconnectCounter the new disconnect counter
   */
  public void setDisconnectCounter(long disconnectCounter) {
    this.disconnectCounter = disconnectCounter;
  }

  /**
   * Checks if is connected.
   *
   * @return true, if is connected
   */
  public boolean isConnected() {
    return connected;
  }

  /**
   * Sets the connected.
   *
   * @param connected the new connected
   */
  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  /**
   * Gets the keep alive timeout.
   *
   * @return the keep alive timeout
   */
  public long getKeepAliveTimeout() {
    return keepAliveTimeout;
  }

  /**
   * Sets the keep alive timeout.
   *
   * @param keepAliveTimeout the new keep alive timeout
   */
  public void setKeepAliveTimeout(long keepAliveTimeout) {
    this.keepAliveTimeout = keepAliveTimeout;
  }

  /**
   * Gets the nr of requests.
   *
   * @return the nr of requests
   */
  public long getNrOfRequests() {
    return nrOfRequests;
  }

  /**
   * Sets the nr of requests.
   *
   * @param nrOfRequests the new nr of requests
   */
  public void setNrOfRequests(long nrOfRequests) {
    this.nrOfRequests = nrOfRequests;
  }

  /**
   * Gets the total bytes.
   *
   * @return the total bytes
   */
  public long getTotalBytes() {
    return totalBytes;
  }

  /**
   * Sets the total bytes.
   *
   * @param totalBytes the new total bytes
   */
  public void setTotalBytes(long totalBytes) {
    this.totalBytes = totalBytes;
  }

  /**
   * Checks if is resend.
   *
   * @return true, if is resend
   */
  public boolean isResend() {
    return resend;
  }

  /**
   * Sets the resend.
   *
   * @param resend the new resend
   */
  public void setResend(boolean resend) {
    this.resend = resend;
  }

  /**
   * Checks if is suspect.
   *
   * @return true, if is suspect
   */
  public boolean isSuspect() {
    return suspect;
  }

  /**
   * Sets the suspect.
   *
   * @param suspect the new suspect
   */
  public void setSuspect(boolean suspect) {
    this.suspect = suspect;
  }

  /**
   * Gets the avg processing time.
   *
   * @return the avg processing time
   */
  public long getAvgProcessingTime() {
    return avgProcessingTime;
  }

  /**
   * Sets the avg processing time.
   *
   * @param avgProcessingTime the new avg processing time
   */
  public void setAvgProcessingTime(long avgProcessingTime) {
    this.avgProcessingTime = avgProcessingTime;
  }

}
