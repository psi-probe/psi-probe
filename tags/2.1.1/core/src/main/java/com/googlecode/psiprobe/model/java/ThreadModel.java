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
package com.googlecode.psiprobe.model.java;

public class ThreadModel {
    private String name;
    private int priority;
    private boolean daemon;
    private boolean interrupted;
    private String runnableClassName;
    private String groupName;
    private String appName;
    private String threadClass;
    private String classLoader;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public String getRunnableClassName() {
        return runnableClassName;
    }

    public void setRunnableClassName(String runnableClassName) {
        this.runnableClassName = runnableClassName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getThreadClass() {
        return threadClass;
    }

    public void setThreadClass(String threadClass) {
        this.threadClass = threadClass;
    }

    public String getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(String classLoader) {
        this.classLoader = classLoader;
    }
}
