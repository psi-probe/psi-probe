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

package com.googlecode.psiprobe.model;

import com.googlecode.psiprobe.tools.logging.LogDestination;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class holds attributes of any other LogDestination so that LogDestination can be serialized.
 * It is generally difficult to make just any LogDestination to be serializable as they more often
 * than not are connected to underlying Log implementation that are in many cases not serializable.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class DisconnectedLogDestination implements LogDestination, Serializable {

  /** The application. */
  private Application application;
  
  /** The root. */
  private boolean root;
  
  /** The context. */
  private boolean context;
  
  /** The name. */
  private String name;
  
  /** The index. */
  private String index;
  
  /** The target class. */
  private String targetClass;
  
  /** The conversion pattern. */
  private String conversionPattern;
  
  /** The file. */
  private File file;
  
  /** The log type. */
  private String logType;
  
  /** The size. */
  private long size;
  
  /** The last modified. */
  private Timestamp lastModified;
  
  /** The level. */
  private String level;
  
  /** The valid levels. */
  private String[] validLevels;

  /**
   * Instantiates a new disconnected log destination.
   *
   * @param destination the destination
   */
  public DisconnectedLogDestination(LogDestination destination) {
    this.application = destination.getApplication();
    this.root = destination.isRoot();
    this.context = destination.isContext();
    this.name = destination.getName();
    this.index = destination.getIndex();
    this.targetClass = destination.getTargetClass();
    this.conversionPattern = destination.getConversionPattern();
    this.file = destination.getFile();
    this.logType = destination.getLogType();
    this.size = destination.getSize();
    this.lastModified = destination.getLastModified();
    this.level = destination.getLevel();
    this.validLevels = destination.getValidLevels();
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getApplication()
   */
  public Application getApplication() {
    return application;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#isRoot()
   */
  public boolean isRoot() {
    return root;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#isContext()
   */
  public boolean isContext() {
    return context;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getName()
   */
  public String getName() {
    return name;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getIndex()
   */
  public String getIndex() {
    return index;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getTargetClass()
   */
  public String getTargetClass() {
    return targetClass;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getConversionPattern()
   */
  public String getConversionPattern() {
    return conversionPattern;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getFile()
   */
  public File getFile() {
    return file;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getLogType()
   */
  public String getLogType() {
    return logType;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getSize()
   */
  public long getSize() {
    return size;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getLastModified()
   */
  public Timestamp getLastModified() {
    return lastModified;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getLevel()
   */
  public String getLevel() {
    return level;
  }

  /* (non-Javadoc)
   * @see com.googlecode.psiprobe.tools.logging.LogDestination#getValidLevels()
   */
  public String[] getValidLevels() {
    return validLevels;
  }

}
