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
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.catalina.util.ServerInfo;

/**
 * POJO representing system information for "system infromation" tab.
 */
public class SystemInformation implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The app base. */
  private String appBase;

  /** The config base. */
  private String configBase;

  /** The system properties. */
  private Map<String, String> systemProperties;

  /**
   * Gets the max memory.
   *
   * @return the max memory
   */
  public long getMaxMemory() {
    return Runtime.getRuntime().maxMemory();
  }

  /**
   * Gets the free memory.
   *
   * @return the free memory
   */
  public long getFreeMemory() {
    return Runtime.getRuntime().freeMemory();
  }

  /**
   * Gets the total memory.
   *
   * @return the total memory
   */
  public long getTotalMemory() {
    return Runtime.getRuntime().totalMemory();
  }

  /**
   * Gets the cpu count.
   *
   * @return the cpu count
   */
  public int getCpuCount() {
    return Runtime.getRuntime().availableProcessors();
  }

  /**
   * Gets the date.
   *
   * @return the date
   */
  public Date getDate() {
    return new Date();
  }

  /**
   * Gets the server info.
   *
   * @return the server info
   */
  public String getServerInfo() {
    return ServerInfo.getServerInfo();
  }

  /**
   * Gets the working dir.
   *
   * @return the working dir
   */
  public String getWorkingDir() {
    return Path.of("").toFile().getAbsolutePath();
  }

  /**
   * Gets the app base.
   *
   * @return the app base
   */
  public String getAppBase() {
    return appBase;
  }

  /**
   * Sets the app base.
   *
   * @param appBase the new app base
   */
  public void setAppBase(String appBase) {
    this.appBase = appBase;
  }

  /**
   * Gets the config base.
   *
   * @return the config base
   */
  public String getConfigBase() {
    return configBase;
  }

  /**
   * Sets the config base.
   *
   * @param configBase the new config base
   */
  public void setConfigBase(String configBase) {
    this.configBase = configBase;
  }

  /**
   * Gets the system properties.
   *
   * @return the system properties
   */
  public Map<String, String> getSystemProperties() {
    return systemProperties;
  }

  /**
   * Sets the system properties.
   *
   * @param systemProperties the system properties
   */
  public void setSystemProperties(Map<String, String> systemProperties) {
    this.systemProperties = systemProperties;
  }

  /**
   * Gets the system property set.
   *
   * @return the system property set
   */
  public Set<Map.Entry<String, String>> getSystemPropertySet() {
    return systemProperties.entrySet();
  }

}
