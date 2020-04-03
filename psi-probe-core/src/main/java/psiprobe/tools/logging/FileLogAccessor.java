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

/**
 * The Class FileLogAccessor.
 */
public class FileLogAccessor extends AbstractLogDestination {

  /** The name. */
  private String name;

  /** The file. */
  private File file;

  @Override
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

  @Override
  public String getTargetClass() {
    return "stdout";
  }

  @Override
  public String getLogType() {
    return "stdout";
  }

  @Override
  public String getConversionPattern() {
    return "";
  }

  @Override
  public File getFile() {
    return file;
  }

  /**
   * Sets the file.
   *
   * @param file the new file
   */
  public void setFile(File file) {
    this.file = file;
  }

}
