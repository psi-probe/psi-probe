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

package psiprobe.model.java;

/**
 * The Class ThreadModel.
 *
 * @author Vlad Ilyushchenko
 */
public class ThreadModel {

  /** The name. */
  private String name;
  
  /** The priority. */
  private int priority;
  
  /** The daemon. */
  private boolean daemon;
  
  /** The interrupted. */
  private boolean interrupted;
  
  /** The runnable class name. */
  private String runnableClassName;
  
  /** The group name. */
  private String groupName;
  
  /** The app name. */
  private String appName;
  
  /** The thread class. */
  private String threadClass;
  
  /** The class loader. */
  private String classLoader;

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return this.name;
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
   * Gets the priority.
   *
   * @return the priority
   */
  public int getPriority() {
    return this.priority;
  }

  /**
   * Sets the priority.
   *
   * @param priority the new priority
   */
  public void setPriority(int priority) {
    this.priority = priority;
  }

  /**
   * Checks if is daemon.
   *
   * @return true, if is daemon
   */
  public boolean isDaemon() {
    return this.daemon;
  }

  /**
   * Sets the daemon.
   *
   * @param daemon the new daemon
   */
  public void setDaemon(boolean daemon) {
    this.daemon = daemon;
  }

  /**
   * Checks if is interrupted.
   *
   * @return true, if is interrupted
   */
  public boolean isInterrupted() {
    return this.interrupted;
  }

  /**
   * Sets the interrupted.
   *
   * @param interrupted the new interrupted
   */
  public void setInterrupted(boolean interrupted) {
    this.interrupted = interrupted;
  }

  /**
   * Gets the runnable class name.
   *
   * @return the runnable class name
   */
  public String getRunnableClassName() {
    return this.runnableClassName;
  }

  /**
   * Sets the runnable class name.
   *
   * @param runnableClassName the new runnable class name
   */
  public void setRunnableClassName(String runnableClassName) {
    this.runnableClassName = runnableClassName;
  }

  /**
   * Gets the group name.
   *
   * @return the group name
   */
  public String getGroupName() {
    return this.groupName;
  }

  /**
   * Sets the group name.
   *
   * @param groupName the new group name
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  /**
   * Gets the app name.
   *
   * @return the app name
   */
  public String getAppName() {
    return this.appName;
  }

  /**
   * Sets the app name.
   *
   * @param appName the new app name
   */
  public void setAppName(String appName) {
    this.appName = appName;
  }

  /**
   * Gets the thread class.
   *
   * @return the thread class
   */
  public String getThreadClass() {
    return this.threadClass;
  }

  /**
   * Sets the thread class.
   *
   * @param threadClass the new thread class
   */
  public void setThreadClass(String threadClass) {
    this.threadClass = threadClass;
  }

  /**
   * Gets the class loader.
   *
   * @return the class loader
   */
  public String getClassLoader() {
    return this.classLoader;
  }

  /**
   * Sets the class loader.
   *
   * @param classLoader the new class loader
   */
  public void setClassLoader(String classLoader) {
    this.classLoader = classLoader;
  }

}
