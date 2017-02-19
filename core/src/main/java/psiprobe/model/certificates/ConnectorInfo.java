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
package psiprobe.model.certificates;

import java.io.Serializable;
import java.util.List;

/**
 * The Class ConnectorInfo.
 */
public class ConnectorInfo implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 5927447793822367835L;

  /** The name. */
  private String name;

  /** The default SSLHostConfig name. */
  private String defaultSSLHostConfigName;

  /** SSLHostConfig infos. */
  private List<SSLHostConfigInfo> sslHostConfigInfos;

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
   * Gets the SSLHostConfig infos.
   *
   * @return the SSLHostConfig infos
   */
  public List<SSLHostConfigInfo> getSslHostConfigInfos() {
    return sslHostConfigInfos;
  }

  /**
   * Sets SSLHostConfig infos.
   *
   * @param sslHostConfigInfos new SSLHostConfig infos
   */
  public void setSslHostConfigInfos(List<SSLHostConfigInfo> sslHostConfigInfos) {
    this.sslHostConfigInfos = sslHostConfigInfos;
  }

  /**
   * Gets the default SSLHostConfig name.
   *
   * @return the default SSLHostConfig name
   */
  public String getDefaultSSLHostConfigName() {
    return defaultSSLHostConfigName;
  }

  /**
   * Sets the default SSLHostConfig name.
   *
   * @param defaultSSLHostConfigName the new default SSLHostConfig name
   */
  public void setDefaultSSLHostConfigName(String defaultSSLHostConfigName) {
    this.defaultSSLHostConfigName = defaultSSLHostConfigName;
  }

}
