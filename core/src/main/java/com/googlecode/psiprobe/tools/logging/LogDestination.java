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

import com.googlecode.psiprobe.model.Application;
import java.io.File;
import java.sql.Timestamp;

public interface LogDestination {
    Application getApplication();
    boolean isRoot();
    boolean isContext();
    String getName();
    String getIndex();
    String getTargetClass();
    String getConversionPattern();
    File getFile();
    long getSize();
    Timestamp getLastModified();

    /**
     * Type of the log, e.g. "log4j", "jdk", "commons-" etc.
     *
     * @return the class of the log
     */
    String getLogType();
    String getLevel();
    String[] getValidLevels();
}
