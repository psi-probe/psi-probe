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
 * A model class representing an application initialization parameter.
 */
public class ApplicationParam {

  /** The name. */
  private String name;

  /** The value. */
  private Object value;

  /** denotes whether the value is taken from a deployment descriptor. */
  private boolean fromDeplDescr;

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
   * Gets the value.
   *
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value the new value
   */
  public void setValue(Object value) {
    this.value = value;
  }

  /**
   * Checks if is from depl descr.
   *
   * @return true, if is from depl descr
   */
  public boolean isFromDeplDescr() {
    return fromDeplDescr;
  }

  /**
   * Sets the from depl descr.
   *
   * @param fromDeplDescr the new from depl descr
   */
  public void setFromDeplDescr(boolean fromDeplDescr) {
    this.fromDeplDescr = fromDeplDescr;
  }

}
