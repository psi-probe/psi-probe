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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UpdateCommitLock}.
 */
class UpdateCommitLockTest {

  @Test
  void testLockAndReleaseUpdate() throws InterruptedException {
    UpdateCommitLock lock = new UpdateCommitLock();
    lock.lockForUpdate();
    lock.releaseUpdateLock();
    // No exception means basic lock/release works
  }

  @Test
  void testLockAndReleaseCommit() throws InterruptedException {
    UpdateCommitLock lock = new UpdateCommitLock();
    lock.lockForCommit();
    lock.releaseCommitLock();
  }

  @Test
  void testMultipleUpdatesCanProceedConcurrently() throws InterruptedException {
    UpdateCommitLock lock = new UpdateCommitLock();
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch bothLocked = new CountDownLatch(2);
    AtomicBoolean bothAcquired = new AtomicBoolean(false);

    Runnable task = () -> {
      try {
        startLatch.await();
        lock.lockForUpdate();
        bothLocked.countDown();
        bothLocked.await();
        bothAcquired.set(true);
        lock.releaseUpdateLock();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    };

    Thread t1 = new Thread(task);
    Thread t2 = new Thread(task);
    t1.start();
    t2.start();

    startLatch.countDown();
    t1.join(3000);
    t2.join(3000);

    assertTrue(bothAcquired.get(), "Both threads should acquire update lock simultaneously");
  }

  @Test
  void testCommitWaitsForUpdate() throws InterruptedException {
    UpdateCommitLock lock = new UpdateCommitLock();
    lock.lockForUpdate(); // hold an update lock

    AtomicBoolean commitAcquired = new AtomicBoolean(false);
    CountDownLatch commitStarted = new CountDownLatch(1);

    Thread commitThread = new Thread(() -> {
      try {
        commitStarted.countDown();
        lock.lockForCommit();
        commitAcquired.set(true);
        lock.releaseCommitLock();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });
    commitThread.start();

    commitStarted.await();
    // Give the commit thread time to block
    Thread.sleep(100);
    assertFalse(commitAcquired.get(), "Commit should be waiting for update lock");

    lock.releaseUpdateLock();
    commitThread.join(3000);
    assertTrue(commitAcquired.get(), "Commit should have acquired lock after update released");
  }

  @Test
  void testUpdateWaitsForCommit() throws InterruptedException {
    UpdateCommitLock lock = new UpdateCommitLock();
    lock.lockForCommit(); // hold commit lock

    AtomicBoolean updateAcquired = new AtomicBoolean(false);
    CountDownLatch updateStarted = new CountDownLatch(1);

    Thread updateThread = new Thread(() -> {
      try {
        updateStarted.countDown();
        lock.lockForUpdate();
        updateAcquired.set(true);
        lock.releaseUpdateLock();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });
    updateThread.start();

    updateStarted.await();
    Thread.sleep(100);
    assertFalse(updateAcquired.get(), "Update should wait for commit to finish");

    lock.releaseCommitLock();
    updateThread.join(3000);
    assertTrue(updateAcquired.get(), "Update should have acquired lock after commit released");
  }

  @Test
  void testUpdateWaitsForCommitRequest() throws InterruptedException {
    UpdateCommitLock lock = new UpdateCommitLock();
    // Start a commit request that will block
    lock.lockForUpdate();

    CountDownLatch commitRequesting = new CountDownLatch(1);
    AtomicBoolean updateAcquiredAfterCommitRequest = new AtomicBoolean(false);

    Thread commitRequestThread = new Thread(() -> {
      try {
        commitRequesting.countDown();
        lock.lockForCommit();
        Thread.sleep(200);
        lock.releaseCommitLock();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });
    commitRequestThread.start();

    commitRequesting.await();
    Thread.sleep(50);
    lock.releaseUpdateLock(); // let commit through

    Thread updateAfterCommitReq = new Thread(() -> {
      try {
        lock.lockForUpdate();
        updateAcquiredAfterCommitRequest.set(true);
        lock.releaseUpdateLock();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    // Wait for commit to finish then update should work
    commitRequestThread.join(3000);
    updateAfterCommitReq.start();
    updateAfterCommitReq.join(3000);

    assertTrue(updateAcquiredAfterCommitRequest.get());
  }
}
