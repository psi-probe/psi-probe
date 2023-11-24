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

import java.util.ArrayList;
import java.util.List;

/**
 * A model class representing a servlet.
 */
public class ServletInfo {

  /** The application name. */
  private String applicationName;

  /** The servlet name. */
  private String servletName;

  /** The servlet class. */
  private String servletClass;

  /** The available. */
  private boolean available;

  /** The load on startup. */
  private int loadOnStartup;

  /** The run as. */
  private String runAs;

  /** The error count. */
  private int errorCount;

  /** The load time. */
  private long loadTime;

  /** The max time. */
  private long maxTime;

  /** The min time. */
  private long minTime;

  /** The processing time. */
  private long processingTime;

  /** The request count. */
  private int requestCount;

  /** The single threaded. */
  private boolean singleThreaded;

  /** The allocation count. */
  private int allocationCount;

  /** The max instances. */
  private int maxInstances;

  /** The mappings. */
  private List<String> mappings;

  /**
   * ServletInfo Constructor.
   */
  public ServletInfo() {
    mappings = new ArrayList<>();
  }

  /**
   * Gets the application name.
   *
   * @return the application name
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * Sets the application name.
   *
   * @param applicationName the new application name
   */
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  /**
   * Gets the servlet name.
   *
   * @return the servlet name
   */
  public String getServletName() {
    return servletName;
  }

  /**
   * Sets the servlet name.
   *
   * @param servletName the new servlet name
   */
  public void setServletName(String servletName) {
    this.servletName = servletName;
  }

  /**
   * Gets the servlet class.
   *
   * @return the servlet class
   */
  public String getServletClass() {
    return servletClass;
  }

  /**
   * Sets the servlet class.
   *
   * @param servletClass the new servlet class
   */
  public void setServletClass(String servletClass) {
    this.servletClass = servletClass;
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
   * Gets the load on startup.
   *
   * @return the load on startup
   */
  public int getLoadOnStartup() {
    return loadOnStartup;
  }

  /**
   * Sets the load on startup.
   *
   * @param loadOnStartup the new load on startup
   */
  public void setLoadOnStartup(int loadOnStartup) {
    this.loadOnStartup = loadOnStartup;
  }

  /**
   * Gets the run as.
   *
   * @return the run as
   */
  public String getRunAs() {
    return runAs;
  }

  /**
   * Sets the run as.
   *
   * @param runAs the new run as
   */
  public void setRunAs(String runAs) {
    this.runAs = runAs;
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
   * Gets the load time.
   *
   * @return the load time
   */
  public long getLoadTime() {
    return loadTime;
  }

  /**
   * Sets the load time.
   *
   * @param loadTime the new load time
   */
  public void setLoadTime(long loadTime) {
    this.loadTime = loadTime;
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
   * Checks if is single threaded.
   *
   * @return true, if is single threaded
   */
  public boolean isSingleThreaded() {
    return singleThreaded;
  }

  /**
   * Sets the single threaded.
   *
   * @param singleThreaded the new single threaded
   */
  public void setSingleThreaded(boolean singleThreaded) {
    this.singleThreaded = singleThreaded;
  }

  /**
   * Gets the allocation count.
   *
   * @return the allocation count
   */
  public int getAllocationCount() {
    return allocationCount;
  }

  /**
   * Sets the allocation count.
   *
   * @param allocationCount the new allocation count
   */
  public void setAllocationCount(int allocationCount) {
    this.allocationCount = allocationCount;
  }

  /**
   * Gets the max instances.
   *
   * @return the max instances
   */
  public int getMaxInstances() {
    return maxInstances;
  }

  /**
   * Sets the max instances.
   *
   * @param maxInstances the new max instances
   */
  public void setMaxInstances(int maxInstances) {
    this.maxInstances = maxInstances;
  }

  /**
   * Gets the mappings.
   *
   * @return the mappings
   */
  public List<String> getMappings() {
    return new ArrayList<>(mappings);
  }

  /**
   * Sets the mappings.
   *
   * @param mappings the new mappings
   */
  public void setMappings(List<String> mappings) {
    this.mappings = new ArrayList<>(mappings);
  }

}
