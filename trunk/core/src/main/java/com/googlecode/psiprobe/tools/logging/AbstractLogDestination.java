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
package com.googlecode.psiprobe.tools.logging;

import java.io.File;
import java.sql.Timestamp;

/**
 *
 * @author Mark Lewis
 */
public abstract class AbstractLogDestination extends DefaultAccessor implements LogDestination {

    public boolean isRoot() {
        return false;
    }

    public boolean isContext() {
        return false;
    }

    public String getIndex() {
        return null;
    }

    public String getConversionPattern() {
        return null;
    }

    protected File getStdoutFile() {
        File f = new File(System.getProperty("catalina.base"), "logs/catalina.out");
        return f.exists() ? f : new File("stdout");
    }

    public File getFile() {
        return getStdoutFile();
    }

    public long getSize() {
        File f = getFile();
        return f != null && f.exists() ? f.length() : 0;
    }

    public Timestamp getLastModified() {
        File f = getFile();
        return f != null && f.exists() ? new Timestamp(f.lastModified()) : null;
    }

    public String getLevel() {
        return null;
    }

    public String[] getValidLevels() {
        return null;
    }

}
