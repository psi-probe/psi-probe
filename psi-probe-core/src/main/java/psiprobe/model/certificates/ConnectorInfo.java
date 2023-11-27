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
package psiprobe.model.certificates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class ConnectorInfo.
 */
public class ConnectorInfo implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 5927447793822367835L;

  /** The name. */
  private String name;

  /** The default SslHostConfig name. */
  private String defaultSslHostConfigName;

  /** SslHostConfig infos. */
  private List<SslHostConfigInfo> sslHostConfigInfos;

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
   * Gets the SslHostConfig infos.
   *
   * @return the SslHostConfig infos
   */
  public List<SslHostConfigInfo> getSslHostConfigInfos() {
    return sslHostConfigInfos == null ? null : new ArrayList<>(sslHostConfigInfos);
  }

  /**
   * Sets SslHostConfig infos.
   *
   * @param sslHostConfigInfos new SslHostConfig infos
   */
  public void setSslHostConfigInfos(List<SslHostConfigInfo> sslHostConfigInfos) {
    this.sslHostConfigInfos =
        sslHostConfigInfos == null ? null : new ArrayList<>(sslHostConfigInfos);
  }

  /**
   * Gets the default SslHostConfig name.
   *
   * @return the default SslHostConfig name
   */
  public String getDefaultSslHostConfigName() {
    return defaultSslHostConfigName;
  }

  /**
   * Sets the default SslHostConfig name.
   *
   * @param defaultSslHostConfigName the new default SslHostConfig name
   */
  public void setDefaultSslHostConfigName(String defaultSslHostConfigName) {
    this.defaultSslHostConfigName = defaultSslHostConfigName;
  }

}
