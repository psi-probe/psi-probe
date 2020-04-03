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

/**
 * POJO representing thread pool.
 */
public class ThreadPool {

  /** The name. */
  private String name;

  /** The max threads. */
  private int maxThreads;

  /** The min spare threads. */
  private int minSpareThreads;

  /** The max spare threads. */
  private int maxSpareThreads;

  /** The current thread count. */
  private int currentThreadCount;

  /** The current threads busy. */
  private int currentThreadsBusy;

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
   * Gets the max threads.
   *
   * @return the max threads
   */
  public int getMaxThreads() {
    return maxThreads;
  }

  /**
   * Sets the max threads.
   *
   * @param maxThreads the new max threads
   */
  public void setMaxThreads(int maxThreads) {
    this.maxThreads = maxThreads;
  }

  /**
   * Gets the min spare threads.
   *
   * @return the min spare threads
   */
  public int getMinSpareThreads() {
    return minSpareThreads;
  }

  /**
   * Sets the min spare threads.
   *
   * @param minSpareThreads the new min spare threads
   */
  public void setMinSpareThreads(int minSpareThreads) {
    this.minSpareThreads = minSpareThreads;
  }

  /**
   * Gets the max spare threads.
   *
   * @return the max spare threads
   */
  public int getMaxSpareThreads() {
    return maxSpareThreads;
  }

  /**
   * Sets the max spare threads.
   *
   * @param maxSpareThreads the new max spare threads
   */
  public void setMaxSpareThreads(int maxSpareThreads) {
    this.maxSpareThreads = maxSpareThreads;
  }

  /**
   * Gets the current thread count.
   *
   * @return the current thread count
   */
  public int getCurrentThreadCount() {
    return currentThreadCount;
  }

  /**
   * Sets the current thread count.
   *
   * @param currentThreadCount the new current thread count
   */
  public void setCurrentThreadCount(int currentThreadCount) {
    this.currentThreadCount = currentThreadCount;
  }

  /**
   * Gets the current threads busy.
   *
   * @return the current threads busy
   */
  public int getCurrentThreadsBusy() {
    return currentThreadsBusy;
  }

  /**
   * Sets the current threads busy.
   *
   * @param currentThreadsBusy the new current threads busy
   */
  public void setCurrentThreadsBusy(int currentThreadsBusy) {
    this.currentThreadsBusy = currentThreadsBusy;
  }

}
