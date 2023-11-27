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

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper class to assist marshalling of ModelAndView.getModel() map to XML representation.
 */
public class TransportableModel {

  /** The items. */
  private Map<String, Object> items = new HashMap<>();

  /**
   * Gets the items.
   *
   * @return the items
   */
  public Map<String, Object> getItems() {
    return items == null ? null : new HashMap<>(items);
  }

  /**
   * Sets the items.
   *
   * @param items the items
   */
  public void setItems(Map<String, Object> items) {
    this.items = items == null ? null : new HashMap<>(items);
  }

  /**
   * Put all.
   *
   * @param map the map
   */
  public void putAll(Map<String, Object> map) {
    if (map != null) {
      items.putAll(new HashMap<>(map));
    }
  }

}
