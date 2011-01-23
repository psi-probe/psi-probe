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

/**
 * POJO representing thread pool
 *
 * @author Vlad Ilyushchenko
 */
public class ThreadPool {

    private String name;
    private int maxThreads;
    private int minSpareThreads;
    private int maxSpareThreads;
    private int currentThreadCount;
    private int currentThreadsBusy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getMinSpareThreads() {
        return minSpareThreads;
    }

    public void setMinSpareThreads(int minSpareThreads) {
        this.minSpareThreads = minSpareThreads;
    }

    public int getMaxSpareThreads() {
        return maxSpareThreads;
    }

    public void setMaxSpareThreads(int maxSpareThreads) {
        this.maxSpareThreads = maxSpareThreads;
    }

    public int getCurrentThreadCount() {
        return currentThreadCount;
    }

    public void setCurrentThreadCount(int currentThreadCount) {
        this.currentThreadCount = currentThreadCount;
    }

    public int getCurrentThreadsBusy() {
        return currentThreadsBusy;
    }

    public void setCurrentThreadsBusy(int currentThreadsBusy) {
        this.currentThreadsBusy = currentThreadsBusy;
    }

}
