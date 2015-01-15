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
 * @author Vlad Ilyushchenko
 * @author Christoph Geiss
 */
public class UpdateCommitLock {
    private int updateCount = 0;
    private int commitCount = 0;
    private int commitRequests = 0;

    public synchronized void lockForUpdate() throws InterruptedException {
        while (commitCount > 0 || commitRequests > 0) {
            wait();
        }
        updateCount++;
    }

    public synchronized void releaseUpdateLock() {
        updateCount--;
        notifyAll();
    }

    public synchronized void lockForCommit() throws InterruptedException {
        commitRequests++;
        while (updateCount > 0 || commitCount > 0) {
            wait();
        }
        commitRequests--;
        commitCount++;
    }

    public synchronized void releaseCommitLock() {
        commitCount--;
        notifyAll();
    }

}
