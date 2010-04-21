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
package com.googlecode.psiprobe.model.jsp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Item implements Serializable {
    /**
     * Item is Out Of Date and requires recompilation.
     */
    public static final int STATE_OOD = 1;

    /**
     * Item is compiled and ready to use.
     */
    public static final int STATE_READY = 2;

    /**
     * Item failed to compile.
     */
    public static final int STATE_FAILED = 3;

    private String name;
    private Exception exception;
    private long compileTime = -1;
    private int state = STATE_OOD;
    private int level;
    private boolean missing = true;
    private long size;
    private long lastModified;
    private Date timestamp;
    private String encoding = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public long getCompileTime() {
        return compileTime;
    }

    public void setCompileTime(long compileTime) {
        this.compileTime = compileTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isMissing() {
        return missing;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
        this.timestamp = new Timestamp(lastModified);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
