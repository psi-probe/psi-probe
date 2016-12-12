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
 * POJO representing application "resource" in Tomcat.
 */
public class ApplicationResource {

  /** The application name. */
  private String applicationName;

  /** The name. */
  private String name;

  /** The type. */
  private String type;

  /** The scope. */
  private String scope;

  /** The auth. */
  private String auth;

  /** The link to. */
  private String linkTo;

  /** The description. */
  private String description;

  /** The looked up. */
  private boolean lookedUp;

  /** The data source info. */
  private DataSourceInfo dataSourceInfo;

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
   * Gets the type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type the new type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets the scope.
   *
   * @return the scope
   */
  public String getScope() {
    return scope;
  }

  /**
   * Sets the scope.
   *
   * @param scope the new scope
   */
  public void setScope(String scope) {
    this.scope = scope;
  }

  /**
   * Gets the auth.
   *
   * @return the auth
   */
  public String getAuth() {
    return auth;
  }

  /**
   * Sets the auth.
   *
   * @param auth the new auth
   */
  public void setAuth(String auth) {
    this.auth = auth;
  }

  /**
   * Gets the description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description.
   *
   * @param description the new description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the data source info.
   *
   * @return the data source info
   */
  public DataSourceInfo getDataSourceInfo() {
    return dataSourceInfo;
  }

  /**
   * Sets the data source info.
   *
   * @param dataSourceInfo the new data source info
   */
  public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
    this.dataSourceInfo = dataSourceInfo;
  }

  /**
   * Checks if is looked up.
   *
   * @return true, if is looked up
   */
  public boolean isLookedUp() {
    return lookedUp;
  }

  /**
   * Sets the looked up.
   *
   * @param lookedUp the new looked up
   */
  public void setLookedUp(boolean lookedUp) {
    this.lookedUp = lookedUp;
  }

  /**
   * Gets the link to.
   *
   * @return the link to
   */
  public String getLinkTo() {
    return linkTo;
  }

  /**
   * Sets the link to.
   *
   * @param linkTo the new link to
   */
  public void setLinkTo(String linkTo) {
    this.linkTo = linkTo;
  }

}
