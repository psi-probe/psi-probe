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

public class FileLogAccessor extends AbstractLogDestination {

    private File file;

    public String getName() {
        return "stdout";
    }

    public String getType() {
        return "stdout";
    }

    public String getLogClass() {
        return "stdout";
    }

    public String getConversionPattern() {
        return "";
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getLevel() {
        return null;
    }

    public String[] getValidLevels() {
        return null;
    }

}
