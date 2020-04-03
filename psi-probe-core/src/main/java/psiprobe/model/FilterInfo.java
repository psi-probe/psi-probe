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
 * A model class representing a filter.
 */
public class FilterInfo {

  /** The filter name. */
  private String filterName;

  /** The filter class. */
  private String filterClass;

  /** The filter desc. */
  private String filterDesc;

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

  /**
   * Gets the filter desc.
   *
   * @return the filter desc
   */
  public String getFilterDesc() {
    return filterDesc;
  }

  /**
   * Sets the filter desc.
   *
   * @param filterDesc the new filter desc
   */
  public void setFilterDesc(String filterDesc) {
    this.filterDesc = filterDesc;
  }

}
