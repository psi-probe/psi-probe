/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://probe.jstripe.com/d/license.shtml
 *
 *  THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 *  WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jstripe.tomcat.probe.tools.logging;

import org.jstripe.tomcat.probe.model.Application;

import java.io.File;
import java.sql.Timestamp;

public interface LogDestination {
    Application getApplication();
    String getName();
    String getType();
    String getConversionPattern();
    File getFile();
    long getSize();
    Timestamp getLastModified();
    String getLogClass();
}
