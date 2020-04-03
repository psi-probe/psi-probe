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

import java.util.ArrayList;
import java.util.List;

/**
 * POJO representing a Connector and its RequestProcessors.
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

  /** The status. */
  private String status;

  /** The protocol. */
  private String protocol;

  /** The local port. */
  private Integer localPort;

  /** The port. */
  private Integer port;

  /** The schema. */
  private String schema;

  /** The secure. */
  private boolean secure;

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
   * @param protocolHandler the new protocol handler
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

  /**
   * get current connector status.
   *
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the status.
   *
   * @param status the new status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * get current connector protocol.
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
   * Gets the local port.
   *
   * @return the local port
   */
  public Integer getLocalPort() {
    return localPort;
  }

  /**
   * Sets the local port.
   *
   * @param localPort the new local port
   */
  public void setLocalPort(Integer localPort) {
    this.localPort = localPort;
  }

  /**
   * Gets the port.
   *
   * @return the port
   */
  public Integer getPort() {
    return port;
  }

  /**
   * Sets the port.
   *
   * @param port the new port
   */
  public void setPort(Integer port) {
    this.port = port;
  }

  /**
   * Gets the schema.
   *
   * @return the schema
   */
  public String getSchema() {
    return schema;
  }

  /**
   * Sets the schema.
   *
   * @param schema the new schema
   */
  public void setSchema(String schema) {
    this.schema = schema;
  }

  /**
   * Checks if is secure.
   *
   * @return true, if is secure
   */
  public boolean isSecure() {
    return secure;
  }

  /**
   * Sets the secure.
   *
   * @param secure the new secure
   */
  public void setSecure(boolean secure) {
    this.secure = secure;
  }

}
