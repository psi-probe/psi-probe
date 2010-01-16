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
package com.googlecode.psiprobe.tools;

/**
 * Simple update-commit lock. This implementation assumes that unlimited number of updates can
 * happen concurrently. However if commit is in progress any update must wait for it to end. Likewise commits must
 * wait for any updates to finish before aquiring the lock.
 *
 * Commits themselves are not synchronized. It is allowed for two commits to run concurrently.
 *
 */
public class UpdateCommitLock {
    private volatile int updateCount = 0;
    private volatile boolean committing = false;
    private final Object updateLock = new Object();
    private final Object commitLock = new Object();

    public synchronized void lockForUpdate() throws InterruptedException {
        while (committing) {
            synchronized (commitLock) {
                commitLock.wait();
            }
        }
        updateCount++;
    }

    public synchronized void releaseUpdateLock() {
        if (updateCount > 0) {
            updateCount--;
            if (updateCount == 0) {
                synchronized (updateLock) {
                    updateLock.notifyAll();
                }
            }
        }
    }

    public synchronized void lockForCommit() throws InterruptedException {

        committing = true;

        while (updateCount > 0) {
            synchronized (updateLock) {
                updateLock.wait();
            }
        }

    }

    public synchronized void releaseCommitLock() {
        if (committing) {
            committing = false;
            synchronized(commitLock) {
                commitLock.notifyAll();
            }
        }
    }

}
