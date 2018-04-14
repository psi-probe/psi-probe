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
package psiprobe.tools.logging;

import java.io.File;
import java.sql.Timestamp;
import psiprobe.model.Application;

/**
 * The Interface LogDestination.
 */
public interface LogDestination {

  /**
   * Gets the application.
   *
   * @return the application
   */
  Application getApplication();

  /**
   * Checks if is root.
   *
   * @return true, if is root
   */
  boolean isRoot();

  /**
   * Checks if is context.
   *
   * @return true, if is context
   */
  boolean isContext();

  /**
   * Gets the name.
   *
   * @return the name
   */
  String getName();

  /**
   * Gets the index.
   *
   * @return the index
   */
  String getIndex();

  /**
   * Gets the target class.
   *
   * @return the target class
   */
  String getTargetClass();

  /**
   * Gets the conversion pattern.
   *
   * @return the conversion pattern
   */
  String getConversionPattern();

  /**
   * Gets the file.
   *
   * @return the file
   */
  File getFile();

  /**
   * Gets the size.
   *
   * @return the size
   */
  long getSize();

  /**
   * Gets the last modified.
   *
   * @return the last modified
   */
  Timestamp getLastModified();

  /**
   * Type of the log, e.g. "log4j", "jdk", "commons-" etc.
   *
   * @return the class of the log
   */
  String getLogType();

  /**
   * Gets the level.
   *
   * @return the level
   */
  String getLevel();

  /**
   * Gets the valid levels.
   *
   * @return the valid levels
   */
  String[] getValidLevels();

  /**
   * Gets the encoding of the file.
   * 
   * @return the encoding name
   */
  String getEncoding();
}
