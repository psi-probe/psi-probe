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
package psiprobe.tools.logging.jdk;

import java.io.File;

import psiprobe.tools.Instruments;

/**
 * JDK 1.4 Logging File Handler Accessor Class.
 */
public class Jdk14FileHandlerAccessor extends Jdk14HandlerAccessor {

  /** The Constant LATEST_FILE_INDEX. */
  private static final int LATEST_FILE_INDEX = 0;

  /**
   * Currently, we only access the latest log file with index 0.
   */
  @Override
  public File getFile() {
    File[] files = (File[]) Instruments.getField(getTarget(), "files");
    if (files == null || files.length == 0) {
      throw new IllegalStateException("File handler does not manage any files");
    }
    return files[LATEST_FILE_INDEX];
  }

}
