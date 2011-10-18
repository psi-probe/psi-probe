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

public class AsyncClusterSender extends SyncClusterSender {
    private long inQueueCounter;
    private long outQueueCounter;
    private long queueSize;
    private long queuedNrOfBytes;

    public long getInQueueCounter() {
        return inQueueCounter;
    }

    public void setInQueueCounter(long inQueueCounter) {
        this.inQueueCounter = inQueueCounter;
    }

    public long getOutQueueCounter() {
        return outQueueCounter;
    }

    public void setOutQueueCounter(long outQueueCounter) {
        this.outQueueCounter = outQueueCounter;
    }

    public long getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(long queueSize) {
        this.queueSize = queueSize;
    }

    public long getQueuedNrOfBytes() {
        return queuedNrOfBytes;
    }

    public void setQueuedNrOfBytes(long queuedNrOfBytes) {
        this.queuedNrOfBytes = queuedNrOfBytes;
    }
}
