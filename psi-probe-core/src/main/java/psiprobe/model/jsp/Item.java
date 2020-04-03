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
package psiprobe.model.jsp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The Class Item.
 */
public class Item implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** Item is Out Of Date and requires recompilation. */
  public static final int STATE_OOD = 1;

  /** Item is compiled and ready to use. */
  public static final int STATE_READY = 2;

  /** Item failed to compile. */
  public static final int STATE_FAILED = 3;

  /** The name. */
  private String name;

  /** The exception. */
  private Exception exception;

  /** The compile time. */
  private long compileTime = -1;

  /** The state. */
  private int state = STATE_OOD;

  /** The level. */
  private int level;

  /** The missing. */
  private boolean missing = true;

  /** The size. */
  private long size;

  /** The last modified. */
  private long lastModified;

  /** The timestamp. */
  private Date timestamp;

  /** The encoding. */
  private String encoding;

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
   * Gets the exception.
   *
   * @return the exception
   */
  public Exception getException() {
    return exception;
  }

  /**
   * Sets the exception.
   *
   * @param exception the new exception
   */
  public void setException(Exception exception) {
    this.exception = exception;
  }

  /**
   * Gets the compile time.
   *
   * @return the compile time
   */
  public long getCompileTime() {
    return compileTime;
  }

  /**
   * Sets the compile time.
   *
   * @param compileTime the new compile time
   */
  public void setCompileTime(long compileTime) {
    this.compileTime = compileTime;
  }

  /**
   * Gets the state.
   *
   * @return the state
   */
  public int getState() {
    return state;
  }

  /**
   * Sets the state.
   *
   * @param state the new state
   */
  public void setState(int state) {
    this.state = state;
  }

  /**
   * Gets the level.
   *
   * @return the level
   */
  public int getLevel() {
    return level;
  }

  /**
   * Sets the level.
   *
   * @param level the new level
   */
  public void setLevel(int level) {
    this.level = level;
  }

  /**
   * Checks if is missing.
   *
   * @return true, if is missing
   */
  public boolean isMissing() {
    return missing;
  }

  /**
   * Sets the missing.
   *
   * @param missing the new missing
   */
  public void setMissing(boolean missing) {
    this.missing = missing;
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

  /**
   * Gets the last modified.
   *
   * @return the last modified
   */
  public long getLastModified() {
    return lastModified;
  }

  /**
   * Sets the last modified.
   *
   * @param lastModified the new last modified
   */
  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
    this.timestamp = new Timestamp(lastModified);
  }

  /**
   * Gets the timestamp.
   *
   * @return the timestamp
   */
  public Date getTimestamp() {
    return timestamp == null ? null : new Date(timestamp.getTime());
  }

  /**
   * Gets the encoding.
   *
   * @return the encoding
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * Sets the encoding.
   *
   * @param encoding the new encoding
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

}
