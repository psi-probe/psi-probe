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
package com.googlecode.psiprobe.model.wrapper;

import java.util.Set;

public class WrapperInfo {
    private String user;
    private String interactiveUser;
    private String version;
    private int wrapperPid;
    private int jvmPid;
    private String buildTime;
    private Set properties;
    private boolean controlledByWrapper;
    private boolean launchedAsService;
    private boolean debugEnabled;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getInteractiveUser() {
        return interactiveUser;
    }

    public void setInteractiveUser(String interactiveUser) {
        this.interactiveUser = interactiveUser;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getWrapperPid() {
        return wrapperPid;
    }

    public void setWrapperPid(int wrapperPid) {
        this.wrapperPid = wrapperPid;
    }

    public int getJvmPid() {
        return jvmPid;
    }

    public void setJvmPid(int jvmPid) {
        this.jvmPid = jvmPid;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public Set getProperties() {
        return properties;
    }

    public void setProperties(Set properties) {
        this.properties = properties;
    }

    public boolean isControlledByWrapper() {
        return controlledByWrapper;
    }

    public void setControlledByWrapper(boolean controlledByWrapper) {
        this.controlledByWrapper = controlledByWrapper;
    }

    public boolean isLaunchedAsService() {
        return launchedAsService;
    }

    public void setLaunchedAsService(boolean launchedAsService) {
        this.launchedAsService = launchedAsService;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}
