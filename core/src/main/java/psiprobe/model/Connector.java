/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package psiprobe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO representing a Connector and its RequestProcessors.
 *
 * @author Mark Lewis
 */
public class Connector {

  /** The protocolHandler. */
  private String protocolHandler;

  /** The max time. */
  private long maxTime;

  /** The processing time. */
  private long processingTime;

  /** The request count. */
  private int requestCount;

  /** The error count. */
  private int errorCount;

  /** The bytes received. */
  private long bytesReceived;

  /** The bytes sent. */
  private long bytesSent;

  /** The request processors. */
  private List<RequestProcessor> requestProcessors = new ArrayList<>();

  /**
   * Gets the protocol handler.
   *
   * @return the protocol handler
   */
  public String getProtocolHandler() {
    return protocolHandler;
  }

  /**
   * Sets the protocol handler.
   *
   * @param protocol handler the new protocol handler
   */
  public void setProtocolHandler(String protocolHandler) {
    this.protocolHandler = protocolHandler;
  }

  /**
   * Gets the max time.
   *
   * @return the max time
   */
  public long getMaxTime() {
    return maxTime;
  }

  /**
   * Sets the max time.
   *
   * @param maxTime the new max time
   */
  public void setMaxTime(long maxTime) {
    this.maxTime = maxTime;
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
   * Gets the request count.
   *
   * @return the request count
   */
  public int getRequestCount() {
    return requestCount;
  }

  /**
   * Sets the request count.
   *
   * @param requestCount the new request count
   */
  public void setRequestCount(int requestCount) {
    this.requestCount = requestCount;
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
   * Gets the error count.
   *
   * @return the error count
   */
  public int getErrorCount() {
    return errorCount;
  }

  /**
   * Sets the error count.
   *
   * @param errorCount the new error count
   */
  public void setErrorCount(int errorCount) {
    this.errorCount = errorCount;
  }

  /**
   * Gets the request processors.
   *
   * @return the request processors
   */
  public List<RequestProcessor> getRequestProcessors() {
    return requestProcessors;
  }

  /**
   * Sets the request processors.
   *
   * @param requestProcessors the new request processors
   */
  public void setRequestProcessors(List<RequestProcessor> requestProcessors) {
    this.requestProcessors = requestProcessors;
  }

  /**
   * Adds the request processor.
   *
   * @param rp the rp
   */
  public void addRequestProcessor(RequestProcessor rp) {
    requestProcessors.add(rp);
  }

}
