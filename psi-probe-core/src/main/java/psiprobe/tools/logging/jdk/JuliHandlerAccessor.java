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
import java.nio.file.Path;

import psiprobe.tools.Instruments;

/**
 * The Class JuliHandlerAccessor.
 */
public class JuliHandlerAccessor extends Jdk14HandlerAccessor {

  @Override
  public File getFile() {
    String dir = (String) Instruments.getField(getTarget(), "directory");
    String prefix = (String) Instruments.getField(getTarget(), "prefix");
    String suffix = (String) Instruments.getField(getTarget(), "suffix");
    String date = (String) Instruments.getField(getTarget(), "date");
    return dir != null && prefix != null && suffix != null && date != null
        ? Path.of(dir, prefix + date + suffix).toFile()
        : getStdoutFile();
  }

}
