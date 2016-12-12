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
 * A model class representing a servlet mapping item.
 */
public class ServletMapping {

  /** The application name. */
  private String applicationName;

  /** The url. */
  private String url;

  /** The servlet name. */
  private String servletName;

  /** The servlet class. */
  private String servletClass;

  /** The available. */
  private boolean available;

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
   * Gets the url.
   *
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the url.
   *
   * @param url the new url
   */
  public void setUrl(String url) {
    this.url = url;
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

}
