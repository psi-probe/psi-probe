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
package psiprobe.model.wrapper;

import java.util.Map.Entry;
import java.util.Set;

/**
 * The Class WrapperInfo.
 */
public class WrapperInfo {

  /** The user. */
  private String user;

  /** The interactive user. */
  private String interactiveUser;

  /** The version. */
  private String version;

  /** The wrapper pid. */
  private int wrapperPid;

  /** The jvm pid. */
  private int jvmPid;

  /** The build time. */
  private String buildTime;

  /** The properties. */
  private Set<Entry<Object, Object>> properties;

  /** The controlled by wrapper. */
  private boolean controlledByWrapper;

  /** The launched as service. */
  private boolean launchedAsService;

  /** The debug enabled. */
  private boolean debugEnabled;

  /**
   * Gets the user.
   *
   * @return the user
   */
  public String getUser() {
    return user;
  }

  /**
   * Sets the user.
   *
   * @param user the new user
   */
  public void setUser(String user) {
    this.user = user;
  }

  /**
   * Gets the interactive user.
   *
   * @return the interactive user
   */
  public String getInteractiveUser() {
    return interactiveUser;
  }

  /**
   * Sets the interactive user.
   *
   * @param interactiveUser the new interactive user
   */
  public void setInteractiveUser(String interactiveUser) {
    this.interactiveUser = interactiveUser;
  }

  /**
   * Gets the version.
   *
   * @return the version
   */
  public String getVersion() {
    return version;
  }

  /**
   * Sets the version.
   *
   * @param version the new version
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Gets the wrapper pid.
   *
   * @return the wrapper pid
   */
  public int getWrapperPid() {
    return wrapperPid;
  }

  /**
   * Sets the wrapper pid.
   *
   * @param wrapperPid the new wrapper pid
   */
  public void setWrapperPid(int wrapperPid) {
    this.wrapperPid = wrapperPid;
  }

  /**
   * Gets the jvm pid.
   *
   * @return the jvm pid
   */
  public int getJvmPid() {
    return jvmPid;
  }

  /**
   * Sets the jvm pid.
   *
   * @param jvmPid the new jvm pid
   */
  public void setJvmPid(int jvmPid) {
    this.jvmPid = jvmPid;
  }

  /**
   * Gets the builds the time.
   *
   * @return the builds the time
   */
  public String getBuildTime() {
    return buildTime;
  }

  /**
   * Sets the builds the time.
   *
   * @param buildTime the new builds the time
   */
  public void setBuildTime(String buildTime) {
    this.buildTime = buildTime;
  }

  /**
   * Gets the properties.
   *
   * @return the properties
   */
  public Set<Entry<Object, Object>> getProperties() {
    return properties;
  }

  /**
   * Sets the properties.
   *
   * @param properties the new properties
   */
  public void setProperties(Set<Entry<Object, Object>> properties) {
    this.properties = properties;
  }

  /**
   * Checks if is controlled by wrapper.
   *
   * @return true, if is controlled by wrapper
   */
  public boolean isControlledByWrapper() {
    return controlledByWrapper;
  }

  /**
   * Sets the controlled by wrapper.
   *
   * @param controlledByWrapper the new controlled by wrapper
   */
  public void setControlledByWrapper(boolean controlledByWrapper) {
    this.controlledByWrapper = controlledByWrapper;
  }

  /**
   * Checks if is launched as service.
   *
   * @return true, if is launched as service
   */
  public boolean isLaunchedAsService() {
    return launchedAsService;
  }

  /**
   * Sets the launched as service.
   *
   * @param launchedAsService the new launched as service
   */
  public void setLaunchedAsService(boolean launchedAsService) {
    this.launchedAsService = launchedAsService;
  }

  /**
   * Checks if is debug enabled.
   *
   * @return true, if is debug enabled
   */
  public boolean isDebugEnabled() {
    return debugEnabled;
  }

  /**
   * Sets the debug enabled.
   *
   * @param debugEnabled the new debug enabled
   */
  public void setDebugEnabled(boolean debugEnabled) {
    this.debugEnabled = debugEnabled;
  }

}
