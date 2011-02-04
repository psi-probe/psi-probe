/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.model;

import com.googlecode.psiprobe.tools.logging.LogDestination;
import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class holds attributes of any other LogDestination so that LogDestination can be serialized.
 * It is generally difficult to make just any LogDestination to be serializable as they more often then not
 * are connected to underlying Log implementation that are in many cases not serializable.
 */
public class DisconnectedLogDestination implements LogDestination, Serializable {

    private Application application;
    private boolean root;
    private boolean context;
    private String name;
    private String index;
    private String targetClass;
    private String conversionPattern;
    private File file;
    private String logType;
    private long size;
    private Timestamp lastModified;
    private String level;
    private String[] validLevels;

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

    public Application getApplication() {
        return application;
    }

    public boolean isRoot() {
        return root;
    }

    public boolean isContext() {
        return context;
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public String getConversionPattern() {
        return conversionPattern;
    }

    public File getFile() {
        return file;
    }

    public String getLogType() {
        return logType;
    }

    public long getSize() {
        return size;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public String getLevel() {
        return level;
    }

    public String[] getValidLevels() {
        return validLevels;
    }

}
