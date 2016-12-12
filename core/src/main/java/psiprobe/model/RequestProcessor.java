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
package psiprobe.model;

import java.util.Locale;

/**
 * POJO representing a single http request processor thread.
 */
public class RequestProcessor {

  /** The name. */
  private String name;

  /** The stage. */
  private int stage;

  /** The processing time. */
  private long processingTime;

  /** The bytes sent. */
  private long bytesSent;

  /** The bytes received. */
  private long bytesReceived;

  /** The remote addr. */
  private String remoteAddr;

  /** The remote addr locale. */
  private Locale remoteAddrLocale;

  /** The virtual host. */
  private String virtualHost;

  /** The method. */
  private String method;

  /** The current uri. */
  private String currentUri;

  /** The current query string. */
  private String currentQueryString;

  /** The protocol. */
  private String protocol;

  /** The worker thread name. */
  private String workerThreadName;

  /** The worker thread name supported. */
  private boolean workerThreadNameSupported;

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
   * Gets the stage.
   *
   * @return the stage
   */
  public int getStage() {
    return stage;
  }

  /**
   * Sets the stage.
   *
   * @param stage the new stage
   */
  public void setStage(int stage) {
    this.stage = stage;
  }

  /**
   * Gets the processing time.
   *
   * @return the processing time
   */
  public long getProcessingTime() {
    return processingTime;
  }

  /**
   * Sets the processing time.
   *
   * @param processingTime the new processing time
   */
  public void setProcessingTime(long processingTime) {
    this.processingTime = processingTime;
  }

  /**
   * Gets the bytes sent.
   *
   * @return the bytes sent
   */
  public long getBytesSent() {
    return bytesSent;
  }

  /**
   * Sets the bytes sent.
   *
   * @param bytesSent the new bytes sent
   */
  public void setBytesSent(long bytesSent) {
    this.bytesSent = bytesSent;
  }

  /**
   * Gets the bytes received.
   *
   * @return the bytes received
   */
  public long getBytesReceived() {
    return bytesReceived;
  }

  /**
   * Sets the bytes received.
   *
   * @param bytesReceived the new bytes received
   */
  public void setBytesReceived(long bytesReceived) {
    this.bytesReceived = bytesReceived;
  }

  /**
   * Gets the remote addr.
   *
   * @return the remote addr
   */
  public String getRemoteAddr() {
    return remoteAddr;
  }

  /**
   * Sets the remote addr.
   *
   * @param remoteAddr the new remote addr
   */
  public void setRemoteAddr(String remoteAddr) {
    this.remoteAddr = remoteAddr;
  }

  /**
   * Gets the virtual host.
   *
   * @return the virtual host
   */
  public String getVirtualHost() {
    return virtualHost;
  }

  /**
   * Sets the virtual host.
   *
   * @param virtualHost the new virtual host
   */
  public void setVirtualHost(String virtualHost) {
    this.virtualHost = virtualHost;
  }

  /**
   * Gets the method.
   *
   * @return the method
   */
  public String getMethod() {
    return method;
  }

  /**
   * Sets the method.
   *
   * @param method the new method
   */
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   * Gets the current uri.
   *
   * @return the current uri
   */
  public String getCurrentUri() {
    return currentUri;
  }

  /**
   * Sets the current uri.
   *
   * @param currentUri the new current uri
   */
  public void setCurrentUri(String currentUri) {
    this.currentUri = currentUri;
  }

  /**
   * Gets the current query string.
   *
   * @return the current query string
   */
  public String getCurrentQueryString() {
    return currentQueryString;
  }

  /**
   * Sets the current query string.
   *
   * @param currentQueryString the new current query string
   */
  public void setCurrentQueryString(String currentQueryString) {
    this.currentQueryString = currentQueryString;
  }

  /**
   * Gets the protocol.
   *
   * @return the protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Sets the protocol.
   *
   * @param protocol the new protocol
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  /**
   * Gets the remote addr locale.
   *
   * @return the remote addr locale
   */
  public Locale getRemoteAddrLocale() {
    return remoteAddrLocale;
  }

  /**
   * Sets the remote addr locale.
   *
   * @param remoteAddrLocale the new remote addr locale
   */
  public void setRemoteAddrLocale(Locale remoteAddrLocale) {
    this.remoteAddrLocale = remoteAddrLocale;
  }

  /**
   * Gets the worker thread name.
   *
   * @return the worker thread name
   */
  public String getWorkerThreadName() {
    return workerThreadName;
  }

  /**
   * Sets the worker thread name.
   *
   * @param workerThreadName the new worker thread name
   */
  public void setWorkerThreadName(String workerThreadName) {
    this.workerThreadName = workerThreadName;
  }

  /**
   * Checks if is worker thread name supported.
   *
   * @return true, if is worker thread name supported
   */
  public boolean isWorkerThreadNameSupported() {
    return workerThreadNameSupported;
  }

  /**
   * Sets the worker thread name supported.
   *
   * @param workerThreadNameSupported the new worker thread name supported
   */
  public void setWorkerThreadNameSupported(boolean workerThreadNameSupported) {
    this.workerThreadNameSupported = workerThreadNameSupported;
  }

}
