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
 * The Class RuntimeInformation.
 */
public class RuntimeInformation {

  /** The vm vendor. */
  private String vmVendor;

  /** The committed virtual memory size. */
  private long committedVirtualMemorySize;

  /** The free physical memory size. */
  private long freePhysicalMemorySize;

  /** The free swap space size. */
  private long freeSwapSpaceSize;

  /** The process cpu time. */
  private long processCpuTime;

  /** The available processors. */
  private int availableProcessors = 1;

  /** The total physical memory size. */
  private long totalPhysicalMemorySize;

  /** The total swap space size. */
  private long totalSwapSpaceSize;

  /** The os name. */
  private String osName;

  /** The os version. */
  private String osVersion;

  /** The start time. */
  private long startTime;

  /** The uptime. */
  private long uptime;

  /** The open file descriptor count. */
  private long openFileDescriptorCount;

  /** The max file descriptor count. */
  private long maxFileDescriptorCount;

  /**
   * Gets the committed virtual memory size.
   *
   * @return the committed virtual memory size
   */
  public long getCommittedVirtualMemorySize() {
    return committedVirtualMemorySize;
  }

  /**
   * Sets the committed virtual memory size.
   *
   * @param committedVirtualMemorySize the new committed virtual memory size
   */
  public void setCommittedVirtualMemorySize(long committedVirtualMemorySize) {
    this.committedVirtualMemorySize = committedVirtualMemorySize;
  }

  /**
   * Gets the free physical memory size.
   *
   * @return the free physical memory size
   */
  public long getFreePhysicalMemorySize() {
    return freePhysicalMemorySize;
  }

  /**
   * Sets the free physical memory size.
   *
   * @param freePhysicalMemorySize the new free physical memory size
   */
  public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
    this.freePhysicalMemorySize = freePhysicalMemorySize;
  }

  /**
   * Gets the free swap space size.
   *
   * @return the free swap space size
   */
  public long getFreeSwapSpaceSize() {
    return freeSwapSpaceSize;
  }

  /**
   * Sets the free swap space size.
   *
   * @param freeSwapSpaceSize the new free swap space size
   */
  public void setFreeSwapSpaceSize(long freeSwapSpaceSize) {
    this.freeSwapSpaceSize = freeSwapSpaceSize;
  }

  /**
   * Gets the process cpu time.
   *
   * @return the process cpu time
   */
  public long getProcessCpuTime() {
    return processCpuTime;
  }

  /**
   * Sets the process cpu time.
   *
   * @param processCpuTime the new process cpu time
   */
  public void setProcessCpuTime(long processCpuTime) {
    this.processCpuTime = processCpuTime;
  }

  /**
   * Gets the available processors.
   *
   * @return the available processors
   */
  public int getAvailableProcessors() {
    return availableProcessors;
  }

  /**
   * Sets the available processors.
   *
   * @param availableProcessors the new available processors
   */
  public void setAvailableProcessors(int availableProcessors) {
    this.availableProcessors = availableProcessors;
  }

  /**
   * Gets the total physical memory size.
   *
   * @return the total physical memory size
   */
  public long getTotalPhysicalMemorySize() {
    return totalPhysicalMemorySize;
  }

  /**
   * Sets the total physical memory size.
   *
   * @param totalPhysicalMemorySize the new total physical memory size
   */
  public void setTotalPhysicalMemorySize(long totalPhysicalMemorySize) {
    this.totalPhysicalMemorySize = totalPhysicalMemorySize;
  }

  /**
   * Gets the total swap space size.
   *
   * @return the total swap space size
   */
  public long getTotalSwapSpaceSize() {
    return totalSwapSpaceSize;
  }

  /**
   * Sets the total swap space size.
   *
   * @param totalSwapSpaceSize the new total swap space size
   */
  public void setTotalSwapSpaceSize(long totalSwapSpaceSize) {
    this.totalSwapSpaceSize = totalSwapSpaceSize;
  }

  /**
   * Gets the os name.
   *
   * @return the os name
   */
  public String getOsName() {
    return osName;
  }

  /**
   * Sets the os name.
   *
   * @param osName the new os name
   */
  public void setOsName(String osName) {
    this.osName = osName;
  }

  /**
   * Gets the os version.
   *
   * @return the os version
   */
  public String getOsVersion() {
    return osVersion;
  }

  /**
   * Sets the os version.
   *
   * @param osVersion the new os version
   */
  public void setOsVersion(String osVersion) {
    this.osVersion = osVersion;
  }

  /**
   * Gets the start time.
   *
   * @return the start time
   */
  public long getStartTime() {
    return startTime;
  }

  /**
   * Sets the start time.
   *
   * @param startTime the new start time
   */
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  /**
   * Gets the uptime.
   *
   * @return the uptime
   */
  public long getUptime() {
    return uptime;
  }

  /**
   * Sets the uptime.
   *
   * @param uptime the new uptime
   */
  public void setUptime(long uptime) {
    this.uptime = uptime;
  }

  /**
   * Gets the vm vendor.
   *
   * @return the vm vendor
   */
  public String getVmVendor() {
    return vmVendor;
  }

  /**
   * Sets the vm vendor.
   *
   * @param vmVendor the new vm vendor
   */
  public void setVmVendor(String vmVendor) {
    this.vmVendor = vmVendor;
  }

  /**
   * Gets the open file descriptor count.
   *
   * @return the open file descriptor count
   */
  public long getOpenFileDescriptorCount() {
    return openFileDescriptorCount;
  }

  /**
   * Sets the open file descriptor count.
   *
   * @param openFileDescriptorCount the new open file descriptor count
   */
  public void setOpenFileDescriptorCount(long openFileDescriptorCount) {
    this.openFileDescriptorCount = openFileDescriptorCount;
  }

  /**
   * Gets the max file descriptor count.
   *
   * @return the max file descriptor count
   */
  public long getMaxFileDescriptorCount() {
    return maxFileDescriptorCount;
  }

  /**
   * Sets the max file descriptor count.
   *
   * @param maxFileDescriptorCount the new max file descriptor count
   */
  public void setMaxFileDescriptorCount(long maxFileDescriptorCount) {
    this.maxFileDescriptorCount = maxFileDescriptorCount;
  }

}
