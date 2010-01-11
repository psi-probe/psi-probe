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
package com.googlecode.psiprobe.model.jmx;

public class SyncClusterSender extends ClusterSender {
    private long dataFailureCounter;
    private long dataResendCounter;
    private long socketOpenCounter;
    private long socketCloseCounter;
    private long socketOpenFailureCounter;

    public long getDataFailureCounter() {
        return dataFailureCounter;
    }

    public void setDataFailureCounter(long dataFailureCounter) {
        this.dataFailureCounter = dataFailureCounter;
    }

    public long getDataResendCounter() {
        return dataResendCounter;
    }

    public void setDataResendCounter(long dataResendCounter) {
        this.dataResendCounter = dataResendCounter;
    }

    public long getSocketOpenCounter() {
        return socketOpenCounter;
    }

    public void setSocketOpenCounter(long socketOpenCounter) {
        this.socketOpenCounter = socketOpenCounter;
    }

    public long getSocketCloseCounter() {
        return socketCloseCounter;
    }

    public void setSocketCloseCounter(long socketCloseCounter) {
        this.socketCloseCounter = socketCloseCounter;
    }

    public long getSocketOpenFailureCounter() {
        return socketOpenFailureCounter;
    }

    public void setSocketOpenFailureCounter(long socketOpenFailureCounter) {
        this.socketOpenFailureCounter = socketOpenFailureCounter;
    }
}
