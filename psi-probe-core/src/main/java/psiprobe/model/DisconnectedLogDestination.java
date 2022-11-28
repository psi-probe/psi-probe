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
package psiprobe.model;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

import psiprobe.tools.logging.LogDestination;

/**
 * This class holds attributes of any other LogDestination so that LogDestination can be serialized.
 * It is generally difficult to make just any LogDestination to be serializable as they more often
 * than not are connected to underlying Log implementation that are in many cases not serializable.
 */
public class DisconnectedLogDestination implements LogDestination, Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

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

  /** The file encoding name. */
  private String encoding;

  /**
   * Loads and returns disconnected log destination.
   *
   * @param destination the destination
   *
   * @return the disconnected log destination
   */
  public DisconnectedLogDestination builder(LogDestination destination) {
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
    this.encoding = destination.getEncoding();
    return this;
  }

  @Override
  public Application getApplication() {
    return application;
  }

  @Override
  public boolean isRoot() {
    return root;
  }

  @Override
  public boolean isContext() {
    return context;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getIndex() {
    return index;
  }

  @Override
  public String getTargetClass() {
    return targetClass;
  }

  @Override
  public String getConversionPattern() {
    return conversionPattern;
  }

  @Override
  public File getFile() {
    return file;
  }

  @Override
  public String getLogType() {
    return logType;
  }

  @Override
  public long getSize() {
    return size;
  }

  @Override
  public Timestamp getLastModified() {
    return lastModified == null ? null : new Timestamp(lastModified.getTime());
  }

  @Override
  public String getLevel() {
    return level;
  }

  @Override
  public String[] getValidLevels() {
    return validLevels == null ? null : validLevels.clone();
  }

  @Override
  public String getEncoding() {
    return encoding;
  }
}
