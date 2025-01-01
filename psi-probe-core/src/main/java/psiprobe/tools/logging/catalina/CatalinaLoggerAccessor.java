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
package psiprobe.tools.logging.catalina;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import psiprobe.tools.Instruments;
import psiprobe.tools.logging.AbstractLogDestination;

/**
 * The Class CatalinaLoggerAccessor.
 */
public class CatalinaLoggerAccessor extends AbstractLogDestination {

  @Override
  public boolean isContext() {
    return true;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getLogType() {
    return "catalina";
  }

  @Override
  public File getFile() {
    String dir = (String) invokeMethod(getTarget(), "getDirectory", null, null);
    String prefix = (String) invokeMethod(getTarget(), "getPrefix", null, null);
    String suffix = (String) invokeMethod(getTarget(), "getSuffix", null, null);
    boolean timestamp = Instruments.getField(getTarget(), "timestamp") != null;
    String date = timestamp ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : "";

    File file =
        notNull(date, dir, prefix, suffix) ? Path.of(dir, prefix + date + suffix).toFile() : null;
    if (file != null && !file.isAbsolute()) {
      return Path.of(System.getProperty("catalina.base"), file.getPath()).toFile();
    }
    return file;
  }

  /**
   * Not null.
   *
   * @param strings the strings
   *
   * @return true, if successful
   */
  private boolean notNull(String... strings) {
    for (String string : strings) {
      if (string == null) {
        return false;
      }
    }
    return true;
  }

}
