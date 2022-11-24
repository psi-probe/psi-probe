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
package psiprobe.model.jmx;

/**
 * The Class MemoryPool.
 */
public class MemoryPool {

  /** The name. */
  private String name;

  /** The init. */
  private long init;

  /** The max. */
  private long max;

  /** The used. */
  private long used;

  /** The committed. */
  private long committed;

  /** The type. */
  private String type;

  /** The id. */
  private String id;

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
    this.id = name != null ? name.replace(' ', '_').toLowerCase() : null;
  }

  /**
   * Gets the inits the.
   *
   * @return the inits the
   */
  public long getInit() {
    return init;
  }

  /**
   * Sets the inits the.
   *
   * @param init the new inits the
   */
  public void setInit(long init) {
    this.init = init;
  }

  /**
   * Gets the max.
   *
   * @return the max
   */
  public long getMax() {
    return max;
  }

  /**
   * Sets the max.
   *
   * @param max the new max
   */
  public void setMax(long max) {
    this.max = max;
  }

  /**
   * Gets the used.
   *
   * @return the used
   */
  public long getUsed() {
    return used;
  }

  /**
   * Sets the used.
   *
   * @param used the new used
   */
  public void setUsed(long used) {
    this.used = used;
  }

  /**
   * Gets the committed.
   *
   * @return the committed
   */
  public long getCommitted() {
    return committed;
  }

  /**
   * Sets the committed.
   *
   * @param committed the new committed
   */
  public void setCommitted(long committed) {
    this.committed = committed;
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
   * Gets the usage score.
   *
   * @return the usage score
   */
  public int getUsageScore() {
    long div;
    if (max == -1) {
      /*
       * Some memory pools have an undefined maximum size. In this case, report how much of the
       * currently allocated memory is used.
       */
      div = committed;
    } else {
      div = max;
    }
    return div == 0 ? 0 : (int) (used * 100 / div);
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

}
