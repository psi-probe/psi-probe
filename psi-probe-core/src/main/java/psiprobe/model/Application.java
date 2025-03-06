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
package psiprobe.model;

import java.io.Serializable;

/**
 * POJO representing Tomcat's web application.
 */
public class Application implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The name. */
  private String name;

  /** The display name. */
  private String displayName;

  /** The doc base. */
  private String docBase;

  /** The available. */
  private boolean available;

  /** The session count. */
  private long sessionCount;

  /** The session attribute count. */
  private long sessionAttributeCount;

  /** The context attribute count. */
  private int contextAttributeCount;

  /** The data source busy score. */
  private int dataSourceBusyScore;

  /** The data source established score. */
  private int dataSourceEstablishedScore;

  /** The distributable. */
  private boolean distributable;

  /** The session timeout. */
  private int sessionTimeout;

  /** The servlet version. */
  private String servletVersion;

  /** The serializable. */
  private boolean serializable;

  /** The size. */
  private long size;

  /** The servlet count. */
  private int servletCount;

  /** The request count. */
  private long requestCount;

  /** The processing time. */
  private long processingTime;

  /** The error count. */
  private long errorCount;

  /** The min time. */
  private long minTime;

  /** The max time. */
  private long maxTime;

  /** The avg time. */
  private long avgTime;

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
   * Gets the display name.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the display name.
   *
   * @param displayName the new display name
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Gets the doc base.
   *
   * @return the doc base
   */
  public String getDocBase() {
    return docBase;
  }

  /**
   * Sets the doc base.
   *
   * @param docBase the new doc base
   */
  public void setDocBase(String docBase) {
    this.docBase = docBase;
  }

  /**
   * Checks if is available.
   *
   * @return true, if is available
   */
  public boolean isAvailable() {
    return available;
  }

  /**
   * Sets the available.
   *
   * @param available the new available
   */
  public void setAvailable(boolean available) {
    this.available = available;
  }

  /**
   * Gets the session count.
   *
   * @return the session count
   */
  public long getSessionCount() {
    return sessionCount;
  }

  /**
   * Sets the session count.
   *
   * @param sessionCount the new session count
   */
  public void setSessionCount(long sessionCount) {
    this.sessionCount = sessionCount;
  }

  /**
   * Gets the session attribute count.
   *
   * @return the session attribute count
   */
  public long getSessionAttributeCount() {
    return sessionAttributeCount;
  }

  /**
   * Sets the session attribute count.
   *
   * @param sessionAttributeCount the new session attribute count
   */
  public void setSessionAttributeCount(long sessionAttributeCount) {
    this.sessionAttributeCount = sessionAttributeCount;
  }

  /**
   * Gets the context attribute count.
   *
   * @return the context attribute count
   */
  public int getContextAttributeCount() {
    return contextAttributeCount;
  }

  /**
   * Sets the context attribute count.
   *
   * @param contextAttributeCount the new context attribute count
   */
  public void setContextAttributeCount(int contextAttributeCount) {
    this.contextAttributeCount = contextAttributeCount;
  }

  /**
   * Gets the data source busy score.
   *
   * @return the data source busy score
   */
  public int getDataSourceBusyScore() {
    return dataSourceBusyScore;
  }

  /**
   * Sets the data source busy score.
   *
   * @param dataSourceBusyScore the new data source busy score
   */
  public void setDataSourceBusyScore(int dataSourceBusyScore) {
    this.dataSourceBusyScore = dataSourceBusyScore;
  }

  /**
   * Gets the data source established score.
   *
   * @return the data source established score
   */
  public int getDataSourceEstablishedScore() {
    return dataSourceEstablishedScore;
  }

  /**
   * Sets the data source established score.
   *
   * @param dataSourceEstablishedScore the new data source established score
   */
  public void setDataSourceEstablishedScore(int dataSourceEstablishedScore) {
    this.dataSourceEstablishedScore = dataSourceEstablishedScore;
  }

  /**
   * Checks if is distributable.
   *
   * @return true, if is distributable
   */
  public boolean isDistributable() {
    return distributable;
  }

  /**
   * Sets the distributable.
   *
   * @param distributable the new distributable
   */
  public void setDistributable(boolean distributable) {
    this.distributable = distributable;
  }

  /**
   * Gets the session timeout.
   *
   * @return the session timeout
   */
  public int getSessionTimeout() {
    return sessionTimeout;
  }

  /**
   * Sets the session timeout.
   *
   * @param sessionTimeout the new session timeout
   */
  public void setSessionTimeout(int sessionTimeout) {
    this.sessionTimeout = sessionTimeout;
  }

  /**
   * Gets the servlet version.
   *
   * @return the servlet version
   */
  public String getServletVersion() {
    return servletVersion;
  }

  /**
   * Sets the servlet version.
   *
   * @param servletVersion the new servlet version
   */
  public void setServletVersion(String servletVersion) {
    this.servletVersion = servletVersion;
  }

  /**
   * Gets the size.
   *
   * @return the size
   */
  public long getSize() {
    return size;
  }

  /**
   * Sets the size.
   *
   * @param size the new size
   */
  public void setSize(long size) {
    this.size = size;
  }

  /**
   * Adds the size.
   *
   * @param size the size
   */
  public void addSize(long size) {
    this.size += size;
  }

  /**
   * Checks if is serializable.
   *
   * @return true, if is serializable
   */
  public boolean isSerializable() {
    return serializable;
  }

  /**
   * Sets the serializable.
   *
   * @param serializable the new serializable
   */
  public void setSerializable(boolean serializable) {
    this.serializable = serializable;
  }

  /**
   * Gets the servlet count.
   *
   * @return the servlet count
   */
  public int getServletCount() {
    return servletCount;
  }

  /**
   * Sets the servlet count.
   *
   * @param servletCount the new servlet count
   */
  public void setServletCount(int servletCount) {
    this.servletCount = servletCount;
  }

  /**
   * Gets the request count.
   *
   * @return the request count
   */
  public long getRequestCount() {
    return requestCount;
  }

  /**
   * Sets the request count.
   *
   * @param requestCount the new request count
   */
  public void setRequestCount(long requestCount) {
    this.requestCount = requestCount;
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
   * Gets the error count.
   *
   * @return the error count
   */
  public long getErrorCount() {
    return errorCount;
  }

  /**
   * Sets the error count.
   *
   * @param errorCount the new error count
   */
  public void setErrorCount(long errorCount) {
    this.errorCount = errorCount;
  }

  /**
   * Gets the min time.
   *
   * @return the min time
   */
  public long getMinTime() {
    return minTime;
  }

  /**
   * Sets the min time.
   *
   * @param minTime the new min time
   */
  public void setMinTime(long minTime) {
    this.minTime = minTime;
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
   * Gets the avg time.
   *
   * @return the avg time
   */
  public long getAvgTime() {
    return avgTime;
  }

  /**
   * Sets the avg time.
   *
   * @param avgTime the new avg time
   */
  public void setAvgTime(long avgTime) {
    this.avgTime = avgTime;
  }

}
