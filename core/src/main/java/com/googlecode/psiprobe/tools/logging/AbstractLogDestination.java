/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.tools.logging;

import java.io.File;
import java.sql.Timestamp;

/**
 * The Class AbstractLogDestination.
 *
 * @author Mark Lewis
 */
public abstract class AbstractLogDestination extends DefaultAccessor implements LogDestination {

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#isRoot()
   */
  public boolean isRoot() {
    return false;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#isContext()
   */
  public boolean isContext() {
    return false;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getIndex()
   */
  public String getIndex() {
    return null;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getConversionPattern()
   */
  public String getConversionPattern() {
    return null;
  }

  /**
   * Gets the stdout file.
   *
   * @return the stdout file
   */
  protected File getStdoutFile() {
    File file = new File(System.getProperty("catalina.base"), "logs/catalina.out");
    return file.exists() ? file : new File("stdout");
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getFile()
   */
  public File getFile() {
    return getStdoutFile();
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getSize()
   */
  public long getSize() {
    File file = getFile();
    return file != null && file.exists() ? file.length() : 0;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getLastModified()
   */
  public Timestamp getLastModified() {
    File file = getFile();
    return file != null && file.exists() ? new Timestamp(file.lastModified()) : null;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getLevel()
   */
  public String getLevel() {
    return null;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getValidLevels()
   */
  public String[] getValidLevels() {
    return null;
  }

}
