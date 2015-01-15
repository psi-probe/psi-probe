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

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.catalina.util.ServerInfo;

/**
 * POJO representing system information for "system infromation" tab.
 *
 * @author Vlad Ilyushchenko
 */
public class SystemInformation implements Serializable {

    private String appBase;
    private String configBase;
    private Map systemProperties;

    public long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public int getCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    public Date getDate() {
        return new Date();
    }

    public String getServerInfo() {
        return ServerInfo.getServerInfo();
    }

    public String getWorkingDir() {
        return new File("").getAbsolutePath();
    }

    public String getAppBase() {
        return appBase;
    }

    public void setAppBase(String appBase) {
        this.appBase = appBase;
    }

    public String getConfigBase() {
        return configBase;
    }

    public void setConfigBase(String configBase) {
        this.configBase = configBase;
    }

    public Map getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Map systemProperties) {
        this.systemProperties = systemProperties;
    }

    public Set getSystemPropertySet() {
        return systemProperties.entrySet();
    }
}
