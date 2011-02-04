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
package com.googlecode.psiprobe.tools.logging.catalina;

import com.googlecode.psiprobe.tools.Instruments;
import com.googlecode.psiprobe.tools.logging.AbstractLogDestination;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CatalinaLoggerAccessor extends AbstractLogDestination {

    public boolean isContext() {
        return true;
    }

    public String getName() {
        return null;
    }

    public String getLogType() {
        return "catalina";
    }

    public File getFile() {
        String dir = (String) invokeMethod(getTarget(), "getDirectory", null, null);
        String prefix = (String) invokeMethod(getTarget(), "getPrefix", null, null);
        String suffix = (String) invokeMethod(getTarget(), "getSuffix", null, null);
        boolean timestamp = ((Boolean)Instruments.getField(getTarget(), "timestamp")).booleanValue();
        String date = timestamp ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : "";

        File file = (date != null && dir != null && prefix != null && suffix != null ? new File(dir, prefix + date + suffix) : null);
        if (file != null && !file.isAbsolute()) {
            return new File(System.getProperty("catalina.base"), file.getPath());
        } else {
            return file;
        }
    }

}
