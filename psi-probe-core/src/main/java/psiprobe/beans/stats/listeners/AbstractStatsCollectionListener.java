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
package psiprobe.beans.stats.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The listener interface for receiving abstractStatsCollection events. The class that is interested
 * in processing a abstractStatsCollection event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * {@code addAbstractStatsCollectionListener} method. When the abstractStatsCollection event occurs,
 * that object's appropriate method is invoked.
 */
public abstract class AbstractStatsCollectionListener implements StatsCollectionListener {

  /** The logger. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /** The property category. */
  private String propertyCategory;

  /** The enabled. */
  private boolean enabled = true;

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Sets the enabled.
   *
   * @param enabled the new enabled
   */
  protected void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Gets the property value.
   *
   * @param name the name
   * @param attribute the attribute
   * @return the property value
   */
  protected String getPropertyValue(String name, String attribute) {
    String value = getPropertyValue(getPropertyKey(name, attribute));
    if (value == null) {
      value = getPropertyValue(getPropertyKey(null, attribute));
    }
    if (value == null) {
      value = getPropertyValue(getPropertyKey(null, null, attribute));
    }
    return value;
  }

  /**
   * Gets the property value.
   *
   * @param key the key
   * @return the property value
   */
  protected String getPropertyValue(String key) {
    return System.getProperty(key);
  }

  /**
   * Gets the property key.
   *
   * @param name the name
   * @param attribute the attribute
   * @return the property key
   */
  protected String getPropertyKey(String name, String attribute) {
    return getPropertyKey(getPropertyCategory(), name, attribute);
  }

  /**
   * Gets the property key.
   *
   * @param category the category
   * @param name the name
   * @param attribute the attribute
   * @return the property key
   */
  private String getPropertyKey(String category, String name, String attribute) {
    StringBuilder result = new StringBuilder().append(getClass().getPackage().getName());
    if (category != null) {
      result.append('.').append(category);
    }
    if (name != null) {
      result.append('.').append(name);
    }
    if (attribute == null) {
      throw new IllegalArgumentException("key cannot be null");
    }
    result.append('.').append(attribute);
    return result.toString();
  }

  /**
   * Reset.
   */
  public void reset() {
    // Not Implemented;
  }

  /**
   * Gets the property category.
   *
   * @return the property category
   */
  public String getPropertyCategory() {
    return propertyCategory;
  }

  /**
   * Sets the property category.
   *
   * @param propertyCategory the new property category
   */
  public void setPropertyCategory(String propertyCategory) {
    this.propertyCategory = propertyCategory;
  }

}
