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
 * A model class representing a filter mapping item.
 */
public class FilterMapping {

  /** The url. */
  private String url;

  /** The servlet name. */
  private String servletName;

  /** The filter name. */
  private String filterName;

  /** The dispatcher map. */
  private String dispatcherMap;

  /** The filter class. */
  private String filterClass;

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
   * Gets the filter name.
   *
   * @return the filter name
   */
  public String getFilterName() {
    return filterName;
  }

  /**
   * Sets the filter name.
   *
   * @param filterName the new filter name
   */
  public void setFilterName(String filterName) {
    this.filterName = filterName;
  }

  /**
   * Gets the dispatcher map.
   *
   * @return the dispatcher map
   */
  public String getDispatcherMap() {
    return dispatcherMap;
  }

  /**
   * Sets the dispatcher map.
   *
   * @param dispatcherMap the new dispatcher map
   */
  public void setDispatcherMap(String dispatcherMap) {
    this.dispatcherMap = dispatcherMap;
  }

  /**
   * Gets the filter class.
   *
   * @return the filter class
   */
  public String getFilterClass() {
    return filterClass;
  }

  /**
   * Sets the filter class.
   *
   * @param filterClass the new filter class
   */
  public void setFilterClass(String filterClass) {
    this.filterClass = filterClass;
  }

}
