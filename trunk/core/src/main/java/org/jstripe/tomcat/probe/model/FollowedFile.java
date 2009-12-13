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
package org.jstripe.tomcat.probe.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class FollowedFile implements Serializable {
    private String fileName;
    private long lastKnowLength;
    private List lines;
    private long size;
    private Timestamp lastModified;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getLastKnowLength() {
        return lastKnowLength;
    }

    public void setLastKnowLength(long lastKnowLength) {
        this.lastKnowLength = lastKnowLength;
    }

    public List getLines() {
        return lines;
    }

    public void setLines(List lines) {
        this.lines = lines;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }
}
