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
    private String name;
    private String type;
    private String conversionPattern;
    private File file;
    private String logClass;
    private long size;
    private Timestamp lastModified;

    public DisconnectedLogDestination(LogDestination destination) {
        this.application = destination.getApplication();
        this.name = destination.getName();
        this.type = destination.getType();
        this.conversionPattern = destination.getConversionPattern();
        this.file = destination.getFile();
        this.logClass = destination.getLogClass();
        this.size = destination.getSize();
        this.lastModified = destination.getLastModified();
    }

    public Application getApplication() {
        return application;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getConversionPattern() {
        return conversionPattern;
    }

    public File getFile() {
        return file;
    }

    public String getLogClass() {
        return logClass;
    }

    public long getSize() {
        return size;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

}
