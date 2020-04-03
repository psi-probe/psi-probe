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
 * This bean represents HttpSession attribute. It is a part of the display model for
 * ListSessionAttributesController.
 */
public class Attribute {

  /** The name. */
  private String name;

  /** The type. */
  private String type;

  /** The value. */
  private Object value;

  /** The serializable. */
  private boolean serializable;

  /** The size. */
  private long size;

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
   * Checks if is serializable.
   *
   * @return true, if is serializable
   */
  public boolean isSerializable() {
    return serializable;
  }

  /**
   * Sets the serializable.
   *
   * @param serializable the new serializable
   */
  public void setSerializable(boolean serializable) {
    this.serializable = serializable;
  }

  /**
   * Gets the size.
   *
   * @return the size
   */
  public long getSize() {
    return size;
  }

  /**
   * Sets the size.
   *
   * @param size the new size
   */
  public void setSize(long size) {
    this.size = size;
  }

}
