/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.model.jmx;

/**
 * The Class AsyncClusterSender.
 */
public class AsyncClusterSender extends SyncClusterSender {

  /** The in queue counter. */
  private long inQueueCounter;

  /** The out queue counter. */
  private long outQueueCounter;

  /** The queue size. */
  private long queueSize;

  /** The queued nr of bytes. */
  private long queuedNrOfBytes;

  /**
   * Gets the in queue counter.
   *
   * @return the in queue counter
   */
  public long getInQueueCounter() {
    return inQueueCounter;
  }

  /**
   * Sets the in queue counter.
   *
   * @param inQueueCounter the new in queue counter
   */
  public void setInQueueCounter(long inQueueCounter) {
    this.inQueueCounter = inQueueCounter;
  }

  /**
   * Gets the out queue counter.
   *
   * @return the out queue counter
   */
  public long getOutQueueCounter() {
    return outQueueCounter;
  }

  /**
   * Sets the out queue counter.
   *
   * @param outQueueCounter the new out queue counter
   */
  public void setOutQueueCounter(long outQueueCounter) {
    this.outQueueCounter = outQueueCounter;
  }

  /**
   * Gets the queue size.
   *
   * @return the queue size
   */
  public long getQueueSize() {
    return queueSize;
  }

  /**
   * Sets the queue size.
   *
   * @param queueSize the new queue size
   */
  public void setQueueSize(long queueSize) {
    this.queueSize = queueSize;
  }

  /**
   * Gets the queued nr of bytes.
   *
   * @return the queued nr of bytes
   */
  public long getQueuedNrOfBytes() {
    return queuedNrOfBytes;
  }

  /**
   * Sets the queued nr of bytes.
   *
   * @param queuedNrOfBytes the new queued nr of bytes
   */
  public void setQueuedNrOfBytes(long queuedNrOfBytes) {
    this.queuedNrOfBytes = queuedNrOfBytes;
  }

}
