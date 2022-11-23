/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.tools;

/**
 * Simple update-commit lock. This implementation assumes that unlimited number of updates can
 * happen concurrently. However if commit is in progress any update must wait for it to end.
 * Likewise commits must wait for any updates to finish before acquiring the lock.
 *
 * <p>
 * Commits themselves are not synchronized. It is allowed for two commits to run concurrently.
 * </p>
 */
public class UpdateCommitLock {

  /** The update count. */
  private int updateCount;

  /** The commit count. */
  private int commitCount;

  /** The commit requests. */
  private int commitRequests;

  /**
   * Lock for update.
   *
   * @throws InterruptedException the interrupted exception
   */
  public synchronized void lockForUpdate() throws InterruptedException {
    while (commitCount > 0 || commitRequests > 0) {
      wait();
    }
    updateCount++;
  }

  /**
   * Release update lock.
   */
  public synchronized void releaseUpdateLock() {
    updateCount--;
    notifyAll();
  }

  /**
   * Lock for commit.
   *
   * @throws InterruptedException the interrupted exception
   */
  public synchronized void lockForCommit() throws InterruptedException {
    commitRequests++;
    while (updateCount > 0 || commitCount > 0) {
      wait();
    }
    commitRequests--;
    commitCount++;
  }

  /**
   * Release commit lock.
   */
  public synchronized void releaseCommitLock() {
    commitCount--;
    notifyAll();
  }

}
