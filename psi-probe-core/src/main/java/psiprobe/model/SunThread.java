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
 * The Class SunThread.
 */
public class SunThread {

  /** The id. */
  private long id;

  /** The name. */
  private String name;

  /** The state. */
  private String state;

  /** The deadlocked. */
  private boolean deadlocked;

  /** The suspended. */
  private boolean suspended;

  /** The in native. */
  private boolean inNative;

  /** The lock name. */
  private String lockName;

  /** The lock owner name. */
  private String lockOwnerName;

  /** The waited count. */
  private long waitedCount;

  /** The blocked count. */
  private long blockedCount;

  /** The execution point. */
  private ThreadStackElement executionPoint;

  /**
   * Gets the id.
   *
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(long id) {
    this.id = id;
  }

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
   * Gets the state.
   *
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * Sets the state.
   *
   * @param state the new state
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Checks if is deadlocked.
   *
   * @return true, if is deadlocked
   */
  public boolean isDeadlocked() {
    return deadlocked;
  }

  /**
   * Sets the deadlocked.
   *
   * @param deadlocked the new deadlocked
   */
  public void setDeadlocked(boolean deadlocked) {
    this.deadlocked = deadlocked;
  }

  /**
   * Checks if is suspended.
   *
   * @return true, if is suspended
   */
  public boolean isSuspended() {
    return suspended;
  }

  /**
   * Sets the suspended.
   *
   * @param suspended the new suspended
   */
  public void setSuspended(boolean suspended) {
    this.suspended = suspended;
  }

  /**
   * Checks if is in native.
   *
   * @return true, if is in native
   */
  public boolean isInNative() {
    return inNative;
  }

  /**
   * Sets the in native.
   *
   * @param inNative the new in native
   */
  public void setInNative(boolean inNative) {
    this.inNative = inNative;
  }

  /**
   * Gets the lock name.
   *
   * @return the lock name
   */
  public String getLockName() {
    return lockName;
  }

  /**
   * Sets the lock name.
   *
   * @param lockName the new lock name
   */
  public void setLockName(String lockName) {
    this.lockName = lockName;
  }

  /**
   * Gets the lock owner name.
   *
   * @return the lock owner name
   */
  public String getLockOwnerName() {
    return lockOwnerName;
  }

  /**
   * Sets the lock owner name.
   *
   * @param lockOwnerName the new lock owner name
   */
  public void setLockOwnerName(String lockOwnerName) {
    this.lockOwnerName = lockOwnerName;
  }

  /**
   * Gets the waited count.
   *
   * @return the waited count
   */
  public long getWaitedCount() {
    return waitedCount;
  }

  /**
   * Sets the waited count.
   *
   * @param waitedCount the new waited count
   */
  public void setWaitedCount(long waitedCount) {
    this.waitedCount = waitedCount;
  }

  /**
   * Gets the blocked count.
   *
   * @return the blocked count
   */
  public long getBlockedCount() {
    return blockedCount;
  }

  /**
   * Sets the blocked count.
   *
   * @param blockedCount the new blocked count
   */
  public void setBlockedCount(long blockedCount) {
    this.blockedCount = blockedCount;
  }

  /**
   * Gets the execution point.
   *
   * @return the execution point
   */
  public ThreadStackElement getExecutionPoint() {
    return executionPoint;
  }

  /**
   * Sets the execution point.
   *
   * @param executionPoint the new execution point
   */
  public void setExecutionPoint(ThreadStackElement executionPoint) {
    this.executionPoint = executionPoint;
  }

}
